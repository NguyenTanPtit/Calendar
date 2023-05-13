package com.example.calendar.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.calendar.R
import com.example.calendar.api.APIObject
import com.example.calendar.model.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnFailureListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone
import kotlin.math.roundToInt


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
    private lateinit var currentLocation: ImageView
    private lateinit var dateTime: TextView
    private lateinit var maxTemp: TextView
    private lateinit var minTemp: TextView
    private lateinit var temp: TextView
    private lateinit var weatherTitle: TextView
    private lateinit var sunriseValue: TextView
    private lateinit var sunsetValue: TextView
    private lateinit var pressureValue: TextView
    private lateinit var humidityValue: TextView
    private lateinit var tempFValue: TextView
    private lateinit var feelsLike: TextView
    private lateinit var groundValue: TextView
    private lateinit var seaValue: TextView
    private lateinit var countryValue: TextView
    private lateinit var windValue: TextView
    private lateinit var weatherImg :ImageView
    private lateinit var mainLayout : ConstraintLayout

    private val apiKey = "a684a608819bf86f1d174077e7fcec4b"

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


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
        initView()
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
        currentLocation = viewRoot.findViewById(R.id.current_location)
        dateTime = viewRoot.findViewById(R.id.weather_date_time)
        maxTemp = viewRoot.findViewById(R.id.max_temp)
        minTemp = viewRoot.findViewById(R.id.min_temp)
        temp = viewRoot.findViewById(R.id.temp)
        weatherTitle = viewRoot.findViewById(R.id.weather_title)
        sunriseValue = viewRoot.findViewById(R.id.sunrise_value)
        sunsetValue = viewRoot.findViewById(R.id.sunset_value)
        pressureValue = viewRoot.findViewById(R.id.pressure_value)
        humidityValue = viewRoot.findViewById(R.id.humidity_value)
        tempFValue = viewRoot.findViewById(R.id.temp_f_value)
        feelsLike = viewRoot.findViewById(R.id.feels_like)
        groundValue = viewRoot.findViewById(R.id.ground_value)
        seaValue = viewRoot.findViewById(R.id.sea_value)
        countryValue = viewRoot.findViewById(R.id.country_value)
        windValue = viewRoot.findViewById(R.id.wind_value)
        weatherImg = viewRoot.findViewById(R.id.weather_img)
        mainLayout = viewRoot.findViewById(R.id.mainLayout)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocationUpdates()
        getCurrentLocation()
        edtSearch.setOnEditorActionListener { textView, i, keyEvent ->
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    getCityWeather(edtSearch.text.toString())
                    val view = activity?.currentFocus
                    if(view!= null){
                        val imm:InputMethodManager = activity?.getSystemService(INPUT_METHOD_SERVICE)
                                as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken,0)
                        edtSearch.clearFocus()
                    }
                    true
                }else{
                    false
                }
        }

        currentLocation.setOnClickListener {
            Log.d("ClickTest", "Yes")
            getCurrentLocation()
        }

    }

    private fun getCurrentLocation(){
        if (checkPermissions()){
            if (isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                Log.d("fusedLocationProviderClient", fusedLocationProviderClient.lastLocation.toString())
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                    Looper.getMainLooper())
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
//                        Log.d("location check", location.toString())
                        if (location != null) {
                            this.location = location
                            fetchCurrentLocationWeather(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        }else{
                            Toast.makeText(context,"Null",Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener(OnFailureListener {
                        Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                    })
            }
            else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermission()
        }
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf( android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager =activity?.getSystemService(Context.LOCATION_SERVICE)
                as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun checkPermissions(): Boolean {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==LOCATION_REQUEST_CODE){

            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
            else{

            }
        }



    }

    private fun getCityWeather(city: String){
        APIObject.getAPIInterface()?.getCityWeatherData(city,apiKey)
            ?.enqueue(object :Callback<WeatherModel>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    Log.d("res",response.body().toString())
                    if(response.isSuccessful){
                        response.body()?.let {
                            setData(it)
                        }
                    }else{
                        Toast.makeText(activity, "No City Found",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Toast.makeText(activity, "Fail to connect",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(body:WeatherModel) {
        val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
        dateTime.text = currentDate.toString()
        maxTemp.text = "Max " + k2c(body?.main?.temp_max!!) + "°"
        minTemp.text = "Min " + k2c(body?.main?.temp_min!!) + "°"
        temp.text = "" + k2c(body?.main?.temp!!) + "°"
        weatherTitle.text = body.weather[0].main
        sunriseValue.text = ts2td(body.sys.sunrise.toLong())
        Log.d("Sys", "${body.sys.sunrise}")
        sunsetValue.text = ts2td(body.sys.sunset.toLong())
        pressureValue.text = body.main.pressure.toString()
        humidityValue.text = body.main.humidity.toString() + "%"
        tempFValue.text = "" + (k2c(body.main.temp).times(1.8)).plus(32)
            .roundToInt() + "°"
        edtSearch.setText(body.name)
        feelsLike.text = "" + k2c(body.main.feels_like) + "°"
        windValue.text = body.wind.speed.toString() + "m/s"
        groundValue.text = body.main.grnd_level.toString()
        seaValue.text = body.main.sea_level.toString()
        countryValue.text = body.sys.country
        Log.d("TIME ZONE", "${TimeZone.getAvailableIDs()} ")

        updateUI(body.weather[0].id)
    }

    private fun updateUI(id: Int) {
        when(id){
            //Thunderstorm
            in 200..232 -> {
                weatherImg.setImageResource(R.drawable.ic_storm_weather)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.thunderstrom_bg)
            }

            //Drizzle
            in 300..321 -> {
                weatherImg.setImageResource(R.drawable.ic_few_clouds)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.drizzle_bg)
            }
            //Rain
            in 500..531 -> {
                weatherImg.setImageResource(R.drawable.ic_rainy_weather)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.rain_bg)
            }

            //Snow
            in 600..631 -> {
                weatherImg.setImageResource(R.drawable.ic_snow_weather)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.snow_bg)
            }

            //Atmosphere
            in 700..731 -> {
                weatherImg.setImageResource(R.drawable.ic_broken_clouds)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.atmosphere_bg)
            }

            //Clear
            800 ->{
                weatherImg.setImageResource(R.drawable.ic_clear_day)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.clear_bg)
            }

            //Clouds
            in 801..804 ->{
                weatherImg.setImageResource(R.drawable.ic_cloudy_weather)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.clouds_bg)
            }

            //unknown
            else->{
                weatherImg.setImageResource(R.drawable.ic_unknown)
                mainLayout.background = ContextCompat.getDrawable(requireContext(),R.drawable.unknown_bg)
            }
        }
    }

    private fun k2c(t:Double):Double{
        var intTemp=t
        intTemp=intTemp.minus(273)
        return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun ts2td(ts:Long):String{
        val localTime=ts.let {
            Instant.ofEpochSecond(it)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
        }
        return localTime.toString()
    }
    private fun fetchCurrentLocationWeather(latitude: String, longitude: String) {
        APIObject.getAPIInterface()?.getCurrentWeatherData(latitude,longitude,apiKey)
            ?.enqueue(object :Callback<WeatherModel>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                    Log.d("resLocation",response.body().toString())
                    if (response.isSuccessful){
                        response.body()?.let {
                            setData(it)
                        }
                    }else{
                        Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Toast.makeText(context,"Fail to connect",Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        getLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
    private fun getLocationUpdates()
    {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
            1000)
            .setIntervalMillis(60000)
            .setMinUpdateIntervalMillis(5000)
            .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {

            }
        }
    }
}