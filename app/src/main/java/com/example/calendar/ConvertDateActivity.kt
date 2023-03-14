package com.example.calendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class ConvertDateActivity : AppCompatActivity() {

    private lateinit var  solarDayLayout :TextInputLayout
    private lateinit var  textInputSolarDay :TextInputEditText
    private lateinit var pickSolarDay : Button
    private lateinit var result : Button
    private lateinit var resultText: TextView
    private lateinit var fromDateType: TextView
    private lateinit var convertTo: TextView
    private lateinit var swap: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_date)
        initView()
    }

    private fun initView(){
        solarDayLayout = findViewById(R.id.edt_solarDay)
        textInputSolarDay = findViewById(R.id.textInputSolarDay)
        pickSolarDay = findViewById(R.id.pickSolarDay)
        result = findViewById(R.id.result)
        resultText = findViewById(R.id.lunaDay)
        fromDateType = findViewById(R.id.fromDateType)
        convertTo = findViewById(R.id.convertTo)
        swap = findViewById(R.id.swap)
        setOnclick()
    }

    @SuppressLint("SetTextI18n")
    private fun setOnclick(){
        val c = Calendar.getInstance()
        pickSolarDay.setOnClickListener {
            val datePicker = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                val date = "$dayOfMonth/${(monthOfYear+1)}/$year"
                Log.d("date :", date)
                textInputSolarDay.text = Editable.Factory.getInstance().newEditable(date)
            },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
        result.setOnClickListener {
            convertDate()
        }

        swap.setOnClickListener{
            swap()
        }
        textInputSolarDay.setOnClickListener {
            solarDayLayout.error = null
        }

    }

    @SuppressLint("SetTextI18n")
    private fun convertDate(){
        resultText.text = null
        val date:String = textInputSolarDay.text.toString()
        if(date.isBlank()){
            solarDayLayout.error = "The date is not empty!"
            return
        }
        val reg = "/".toRegex()
        val dateArray: MutableList<String>
        val convertDate = ConvertDate()
        val dateNum: Int
        val monthNum: Int
        val yearNum: Int
        try {
            dateArray = reg.split(date).filter { it.isNotBlank() } as MutableList<String>
            dateNum = dateArray[0].trim().toInt()
            monthNum = dateArray[1].trim().toInt()
            yearNum = dateArray[2].trim().toInt()
            if(dateNum<1||dateNum>31||monthNum<1||monthNum>12||yearNum<1){
                solarDayLayout.error = "The date is not correct!"
                return
            }
            solarDayLayout.error = null
            if(fromDateType.text.equals("Solar")){
                val lunaDateArray  =  convertDate.convertSolar2Lunar(dateNum,monthNum,yearNum,7.0)
                resultText.text = "${lunaDateArray[0]} / ${lunaDateArray[1]} / ${lunaDateArray[2]}"
            }else{
                val solarDateArray  =  convertDate.convertLunar2Solar(dateNum,monthNum,yearNum,0,7.0)
                resultText.text = "${solarDateArray[0]} / ${solarDateArray[1]} / ${solarDateArray[2]}"
            }

        }catch (e:java.lang.Exception){
            solarDayLayout.error = "The date is not correct!"
            return
        }
    }

    @SuppressLint("SetTextI18n")
    private fun swap(){
        val animLeftToRight = TranslateAnimation(
            Animation.ABSOLUTE.toFloat(), 150F,
            Animation.ABSOLUTE.toFloat(), Animation.ABSOLUTE.toFloat()
        )
        val animRightToLeft = TranslateAnimation(
            Animation.ABSOLUTE.toFloat(), -150F,
            Animation.ABSOLUTE.toFloat(), Animation.ABSOLUTE.toFloat()
        )
        animRightToLeft.duration = 100
        animLeftToRight.duration = 100
        val fromDate = fromDateType.text
        if(fromDate.equals("Solar")){
            fromDateType.startAnimation(animLeftToRight)
            fromDateType.text = "Lunar"
            convertTo.startAnimation(animRightToLeft)
            convertTo.text = "Solar"
        }else{
            fromDateType.startAnimation(animLeftToRight)
            fromDateType.text = "Solar"
            convertTo.startAnimation(animRightToLeft)
            convertTo.text = "Lunar"
        }
        Log.d("positionType", fromDateType.text as String)
    }
}