package com.example.calendar.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.calendar.EditProfileActivity
import com.example.calendar.R
import com.example.calendar.WelcomeActivity
import com.example.calendar.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var root: View
    private lateinit var avatar: CircleImageView
    private lateinit var tvEmail: TextView
    private lateinit var tvFullName: TextView
    private lateinit var btnEdit: AppCompatButton
    private lateinit var logout: LinearLayout
    private lateinit var auth : FirebaseAuth
    private lateinit var acc : GoogleSignInAccount
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var type:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        initView()
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun initView(){
        avatar = root.findViewById(R.id.user_avatar)
        tvEmail = root.findViewById(R.id.user_email)
        tvFullName = root.findViewById(R.id.user_full_name)
        btnEdit = root.findViewById(R.id.btn_edit)
        logout = root.findViewById(R.id.logout)

        type = requireActivity().intent.getStringExtra("type").toString()
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        val user = auth.currentUser
        if(type=="email"){
            if(user!=null) {
                firebaseDatabase.getReference("Users").child(user.uid).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val mUser = snapshot.getValue(User::class.java)
                        if(mUser != null){
                            tvEmail.text = mUser.email
                            tvFullName.text = mUser.fullName
                            Glide.with(requireContext()).load(mUser.imgUrl)
                                .error(R.drawable.default_avatar).into(avatar)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //TODO("Not yet implemented")
                    }
                })
            }
        }
        else if(type=="GG"){
            if(GoogleSignIn.getLastSignedInAccount(requireContext()) != null){
                acc = GoogleSignIn.getLastSignedInAccount(requireContext())!!
                Glide.with(requireContext()).load(acc.photoUrl).into(avatar)
                tvEmail.text = acc.email
                tvFullName.text = acc.displayName
                btnEdit.visibility = View.GONE
            }
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),WelcomeActivity::class.java))
            requireActivity().finish()
        }

        btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(),EditProfileActivity::class.java))
        }
    }
}