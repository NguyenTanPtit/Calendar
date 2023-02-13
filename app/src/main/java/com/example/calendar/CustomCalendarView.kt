package com.example.calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class CustomCalendarView : LinearLayout {
    private lateinit var nextBtn : ImageView
    private lateinit var preBtn : ImageView
    private lateinit var currentDate: TextView
    private lateinit var gridView :GridView
    private lateinit var gridAdapter: GridAdapter

    private val MAX_DAY = 42
    var cal: Calendar = Calendar.getInstance()

//    @get:JvmName("getAdapterContext")
    private lateinit var context : Context

    private var dates : MutableList<Date> = mutableListOf()
    private var eventList : MutableList<Events> = mutableListOf()

    private var dateFormat = SimpleDateFormat("MMM yyyy")
    private val dateFormatSave = SimpleDateFormat("dd")
    var monthFormat = SimpleDateFormat("MM")
    var yearFormat = SimpleDateFormat("yyyy")

    private lateinit var alertDialog : AlertDialog
    constructor(context: Context) : super(context)



    constructor(context: Context, attrs : AttributeSet) : super(context, attrs){
        this.context = context
        initLayout()
        setupCal()
        setOnClickItemCal()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private fun initLayout(){
        val inflater :LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
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
        dates.clear()
        val month = cal.clone() as Calendar
        month.set(Calendar.DAY_OF_MONTH,1)
        val firstDayOfMonth = month.get(Calendar.DAY_OF_WEEK) -2
        month.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)
        while (dates.size< MAX_DAY){
            dates.add(month.time)
            month.add(Calendar.DAY_OF_MONTH,1)
        }
        gridAdapter = GridAdapter(context,dates,cal,eventList)
        gridView.adapter = gridAdapter

    }

    @SuppressLint("InflateParams")
    private fun setOnClickItemCal(){
        gridView.setOnItemClickListener{ adapterView, view, position, id ->
            val builder :AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val simpleDateFormat = SimpleDateFormat("HH:mm a")
            val addEventView :View = LayoutInflater.from(adapterView.context)
                .inflate(R.layout.add_event_layout,null)
            val eventName:EditText = addEventView.findViewById(R.id.event_title)
            val eventTime:LinearLayout = addEventView.findViewById(R.id.event_time)
            val addEvent:Button = addEventView.findViewById(R.id.event_add_btn)
            val timeSet: TextView = addEventView.findViewById(R.id.time_set)
            val currentTime = simpleDateFormat.format(cal.time)
            timeSet.text = currentTime.toString()

            eventTime.setOnClickListener{
                val cal :Calendar = Calendar.getInstance()
                val hours = cal.get(Calendar.HOUR_OF_DAY)
                val min = cal.get(Calendar.MINUTE)

                val timePicker = TimePickerDialog(addEventView.context,
                    androidx.appcompat.R.style.Theme_AppCompat_Dialog,
                    { view, hourOfDay, minute ->
                        val c = Calendar.getInstance()
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                        c.set(Calendar.MINUTE,minute)
                        c.timeZone = TimeZone.getDefault()
                        val time = simpleDateFormat.format(c.time)
                        timeSet.text = time
                    },hours,min,false)
                timePicker.show()
            }

            val date = dateFormatSave.format(dates[position])
            val month = monthFormat.format(dates[position])
            val year = yearFormat.format(dates[position])

            addEvent.setOnClickListener {
                saveEvent(eventName.text.toString(),timeSet.text.toString(),date,month,year)
                setupCal()
                alertDialog.dismiss()
            }

            builder.setView(addEventView)
            alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_alert_dialog)
        }
    }

    private fun saveEvent(name:String, time:String, date:String, month:String, year: String){
        val dbOpen = DBOpenHelper(context)
        val db = dbOpen.writableDatabase
        dbOpen.saveEvent(name,time,date,month,year,db)
        db.close()
        Toast.makeText(context,"Saved!",Toast.LENGTH_SHORT).show()
    }

}