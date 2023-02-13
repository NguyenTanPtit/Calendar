package com.example.calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*

class GridAdapter : ArrayAdapter<Any> {
    private var dates: MutableList<Date>
    private var currentDate: Calendar
    private var events: MutableList<Events>
    private var layoutInflater: LayoutInflater

    constructor(
        context: Context, dates: MutableList<Date>, currentDate: Calendar,
        events: MutableList<Events>
    ) : super(context, R.layout.single_cell_layout) {
        this.dates = dates
        this.currentDate = currentDate
        this.events = events
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val monthDate = dates[position]
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = monthDate
        val dayNum = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = dateCalendar.get(Calendar.MONTH)+1
        Log.d("displayMonth", displayMonth.toString())
        val currentMonth = currentDate.get(Calendar.MONTH)+1
        Log.d("currentMonth", currentMonth.toString())
        val displayYear = dateCalendar.get(Calendar.YEAR)
        val currentYear = currentDate.get(Calendar.YEAR)

        val view:View = convertView ?: layoutInflater.inflate(R.layout.single_cell_layout,parent,false)
        val dayNumber:TextView = view.findViewById(R.id.day_cal)
        if(displayMonth == currentMonth && currentYear == displayYear){
            dayNumber.setTextColor(ContextCompat.getColor(context,R.color.black))
        }
        else{
            dayNumber.setTextColor(ContextCompat.getColor(context,R.color.gray))
        }

        dayNumber.text = dayNum.toString()
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
}