package com.example.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.calendar.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity() {
    private lateinit var avatar: CircleImageView
    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var save: AppCompatButton

    private lateinit var fullNamError: TextView
    private lateinit var phoneNumberError: TextView
    private lateinit var cancel: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        firebaseStorage = FirebaseStorage.getInstance()
        user = auth.currentUser!!
        initView()


        firebaseDatabase.getReference("Users").child(user.uid)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val mUser = snapshot.getValue(User::class.java)
                    if (mUser != null) {
                        edtEmail.text = Editable.Factory.getInstance().newEditable(mUser.email)
                        edtFullName.text =
                            Editable.Factory.getInstance().newEditable(mUser.fullName)
                        if(mUser.phoneNumber!=null) {
                            edtPhoneNumber.text =
                                Editable.Factory.getInstance().newEditable(mUser.phoneNumber)
                        }
                        Glide.with(this@EditProfileActivity).load(mUser.imgUrl)
                            .error(R.drawable.default_avatar).into(avatar)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }
            })
    }

    private fun initView(){
        avatar = findViewById(R.id.user_avatar)
        cancel = findViewById(R.id.tvCancel)
        edtFullName = findViewById(R.id.edt_full_name)
        edtEmail = findViewById(R.id.edt_email)
        edtEmail.isEnabled = false
        edtPhoneNumber = findViewById(R.id.edt_phone_num)
        save = findViewById(R.id.btnSave)

        fullNamError = findViewById(R.id.full_name_error)
        phoneNumberError = findViewById(R.id.phone_num_error)

        setOnClick()
    }

    @SuppressLint("SetTextI18n")
    private fun setOnClick(){
        avatar.setOnClickListener {
            val intent = Intent()
            intent.action =Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,99999)
        }

        cancel.setOnClickListener {
            finish()
        }

        save.setOnClickListener {
            val fullName = edtFullName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val phoneNumber = edtPhoneNumber.text.toString().trim()
            if(fullName.isBlank()){
                fullNamError.text = "Full Name is required!"
                fullNamError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(phoneNumber.isBlank()){
                phoneNumberError.text = "Phone number is required!"
                phoneNumberError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val info = mapOf(
                "fullName" to fullName,
                "email" to email,
                "phoneNumber" to phoneNumber
            )
            firebaseDatabase.getReference("Users").child(user.uid).updateChildren(info)
                .addOnSuccessListener {
                    Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }

        }

        edtFullName.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                fullNamError.visibility = View.GONE
            }
        }

        edtPhoneNumber.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                phoneNumberError.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==99999){
            if(data!=null){
                val profileUri: Uri? = data.data
                if(profileUri!=null){
                    avatar.setImageURI(profileUri)
                    val reference: StorageReference = firebaseStorage.reference

                    val taskUpLoad = reference.child("profile_picture")
                        .child(user.uid).putFile(profileUri)

                    taskUpLoad.addOnSuccessListener {
                        reference.child("profile_picture").child(user.uid).downloadUrl
                            .addOnSuccessListener {it2->
                            firebaseDatabase.getReference("Users").child(user.uid)
                                .child("imgUrl").setValue(it2.toString())
                        }
                        Toast.makeText(this, "UpLoad Success", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Toast.makeText(this, "UpLoad Fail", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}