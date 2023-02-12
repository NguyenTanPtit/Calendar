package com.example.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CustomCalendarView : LinearLayout {
    private lateinit var nextBtn : ImageView
    private lateinit var preBtn : ImageView
    private lateinit var currentDate: TextView
    private lateinit var gridView :GridView
    private var MAX_DAY = 42
    var cal = Calendar.getInstance()
    @get:JvmName("getAdapterContext")
    private lateinit var context : Context

    lateinit var dates : List<Date>
    lateinit var eventList : List<Events>

    var dateFormat = SimpleDateFormat("dd/MM/yyyy")
    var  monthFormat = SimpleDateFormat("MM")
    var yearFormat = SimpleDateFormat("yyyy")

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs : AttributeSet) : super(context, attrs){
        this.context = context
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private fun initLayout(){
        val inflater :LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflater.inflate(R.layout.calendar_layout,this)
        nextBtn = view.findViewById(R.id.nextBtn)
        preBtn = view.findViewById(R.id.backBtn)
        currentDate = view.findViewById(R.id.currentTime)
        gridView =view.findViewById(R.id.grid_cal)

        nextBtn.setOnClickListener{
            cal.add(Calendar.MONTH,+1)
            setupCal()
        }

        preBtn.setOnClickListener{
            cal.add(Calendar.MONTH,-1)
            setupCal()
        }

    }

    private fun setupCal(){
        val currentDates = dateFormat.format(cal.time)
        currentDate.text = currentDates
    }

}