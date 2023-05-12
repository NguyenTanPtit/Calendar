package com.example.calendar

import android.content.SharedPreferences.Editor
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewRoot: View
    private lateinit var location:Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_REQUEST_CODE = 9999
    private lateinit var edtSearch: EditText
    private val apiKey = "a684a608819bf86f1d174077e7fcec4b"

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
    ): View{
        // Inflate the layout for this fragment
        viewRoot =inflater.inflate(R.layout.fragment_weather, container, false)
        return viewRoot
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WeatherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initView(){
        edtSearch = viewRoot.findViewById(R.id.search_city)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity())

        getCurrentLocation()
//        edtSearch.setOnEditorActionListener { textView, i, keyEvent ->
////                if(i == EditorInfo.IME_ACTION_SEARCH){
//////                    getCityWeather(edtSearch.text.toString())
//////                    val view =
//////                    true
//////                }else{
//////                    false
////                }
//        }

    }

    private fun getCurrentLocation(){

    }

    private fun getCityWeather(city: String){

    }
}