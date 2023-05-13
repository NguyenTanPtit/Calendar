package com.example.calendar.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.calendar.ConvertDateActivity
import com.example.calendar.R
import com.example.calendar.StopWatchActivity
import com.example.calendar.TimerActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LitFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var view: View
    private lateinit var convertDate: CardView
    private lateinit var timer: CardView
    private lateinit var stopWatch: CardView

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
        view =  inflater.inflate(R.layout.fragment_lit, container, false)
        initView()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LitFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initView(){
        convertDate = view.findViewById(R.id.convert)
        timer = view.findViewById(R.id.timer)
        stopWatch = view.findViewById(R.id.stopWatch)
        nextActivity()
    }

    private fun nextActivity(){
        convertDate.setOnClickListener{
            val i = Intent(context, ConvertDateActivity::class.java)
            startActivity(i)
        }

        timer.setOnClickListener {
            val i = Intent(context, TimerActivity::class.java)
            startActivity(i)
        }

        stopWatch.setOnClickListener {
            val i = Intent(context, StopWatchActivity::class.java)
            startActivity(i)
        }
    }
}