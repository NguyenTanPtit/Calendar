package com.example.calendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.Calendar

class ConvertDateActivity : AppCompatActivity() {

    private lateinit var  solarDay :EditText
    private lateinit var pickSolarDay : Button
    private lateinit var lunaDay: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_date)
        initView()
    }

    private fun initView(){
        solarDay = findViewById(R.id.edt_solarDay)
        pickSolarDay = findViewById(R.id.pickSolarDay)
        lunaDay = findViewById(R.id.lunaDay)
        setOnclick()
    }

    @SuppressLint("SetTextI18n")
    private fun setOnclick(){
        val c = Calendar.getInstance()
        pickSolarDay.setOnClickListener {
            val datePicker = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                val date = "$dayOfMonth / ${(monthOfYear+1)} / $year"
                Log.d("date :", date)
                solarDay.text = Editable.Factory.getInstance().newEditable(date)
                val convertDate = ConvertDate()

                val lunaDateArray  =  convertDate.convertSolar2Lunar(dayOfMonth,monthOfYear+1,year,
                     7.0)
                lunaDay.text = "${lunaDateArray[0]} / ${lunaDateArray[1]} / ${lunaDateArray[2]}"
            },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
    }
}