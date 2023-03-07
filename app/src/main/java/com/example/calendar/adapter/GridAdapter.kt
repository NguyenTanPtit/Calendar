package com.example.calendar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.calendar.Events
import com.example.calendar.R
import java.text.SimpleDateFormat
import java.util.*

class GridAdapter : ArrayAdapter<Any> {
    private var dates: MutableList<Date>
    private var currentDate: Calendar
    private var events: MutableList<Events>
    private var layoutInflater: LayoutInflater
    var currentDatePos = -1
    constructor(
        context: Context, dates: MutableList<Date>, currentDate: Calendar,
        events: MutableList<Events>
    ) : super(context, R.layout.single_cell_layout) {
        this.dates = dates
        this.currentDate = currentDate
        this.events = events
        this.layoutInflater = LayoutInflater.from(context)

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val monthDate = dates[position]
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = monthDate
        val dayNum = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = dateCalendar.get(Calendar.MONTH)+1
        val currentMonth = currentDate.get(Calendar.MONTH)+1
        val displayYear = dateCalendar.get(Calendar.YEAR)
        val currentYear = currentDate.get(Calendar.YEAR)

        val view:View = convertView ?: layoutInflater.inflate(R.layout.single_cell_layout,parent,false)
            view.background = context.resources
                .getDrawable(R.drawable.bg_normal_day,null)

        val dayNumber:TextView = view.findViewById(R.id.day_cal)
        val evenNum :TextView= view.findViewById(R.id.event)

        if(displayMonth == currentMonth && currentYear == displayYear){
            dayNumber.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        else{
            dayNumber.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }

        val cal2 = Calendar.getInstance()
        val currentDay = cal2.get(Calendar.DAY_OF_MONTH)
        val monthOfCurrentDay = cal2.get(Calendar.MONTH) +1
        val yearOfCurrentDay = cal2.get(Calendar.YEAR)

        if(currentDay == dayNum && monthOfCurrentDay == displayMonth
            && yearOfCurrentDay == displayYear ){
            currentDatePos = position
            Log.d("currentDatePos", currentDatePos.toString())
            view.background = context.resources
                .getDrawable(R.drawable.bg_cell_gridview_currentday,null)
            dayNumber.setTextColor(ContextCompat.getColor(context, R.color.white))
            evenNum.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        dayNumber.text = dayNum.toString()
        val eventCal = Calendar.getInstance()
        val arr = mutableListOf<String>()
        for(i in 0 until events.size){
            val dayTime = convertStringToDate(events[i].Date)
            if(dayTime == null){
                Log.d("convert string to date", "null")
            }else {
                eventCal.time = dayTime
                if(eventCal.get(Calendar.DAY_OF_MONTH) == dayNum && displayMonth== eventCal.get(Calendar.MONTH)+1
                    && displayYear == eventCal.get(Calendar.YEAR)){
                    arr.add(events[i].Event)
                    Log.d("eventListSig", arr.size.toString())
                    evenNum.text = "${arr.size}"
                }
            }
        }

        return view
    }

    override fun getCount(): Int {
        return dates.size
    }

    override fun getItem(position: Int): Any {
        return dates[position]
    }

    override fun getPosition(item: Any?): Int {
        return dates.indexOf(item)
    }

    private fun convertStringToDate(eventDate:String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy")
        var date :Date? = null
        try {
            date = format.parse(eventDate) as Date
        }catch (e:java.lang.Exception){
            Log.d("convertStringToDate", e.stackTraceToString())
        }
        return date
    }
}