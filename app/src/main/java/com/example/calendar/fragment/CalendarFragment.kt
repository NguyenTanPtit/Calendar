package com.example.calendar.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar.CustomCalendarView
import com.example.calendar.DB.DBOpenHelper
import com.example.calendar.DB.DBStructure
import com.example.calendar.adapter.OnClickItemListener
import com.example.calendar.R
import com.example.calendar.adapter.EventRecyclerAdapter
import com.example.calendar.adapter.GridAdapter
import com.example.calendar.model.AlarmReceiver
import com.example.calendar.model.Events
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerAdapter: EventRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cal : CustomCalendarView
    private lateinit var view : View
    private lateinit var gridView : GridView
    private lateinit var alertDialog : AlertDialog
    private lateinit var gridAdapter: GridAdapter
    private lateinit var nextBtn: ImageView
    private lateinit var preBtn: ImageView
    private lateinit var floatBtnAddEvent: FloatingActionButton
    private lateinit var emptyState :LinearLayout
    private lateinit var upComingTxt: TextView

    private val MAX_DAY = 42
    private var currentPosition = -1
    private var currentDatePos = -1

    private var dateFormat = SimpleDateFormat("MMMM yyyy")
    private val dateFormatSave = SimpleDateFormat("dd/MM/yyyy")
    var monthFormat = SimpleDateFormat("MM")
    var yearFormat = SimpleDateFormat("yyyy")
    private var dates : MutableList<Date> = mutableListOf()
    private var eventMonthList : MutableList<Events> = mutableListOf()
    private var eventDayList: MutableList<Events> = mutableListOf()
    private val cal2: Calendar = Calendar.getInstance()

    private var alarmYear = -1
    private var alarmMonth = -1
    private var alarmDay = -1
    private var alarmHour = -1
    private var alarmMinute = -1
    private var isPushNotificationGranted = false
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
        view  = inflater.inflate(R.layout.fragment_calendar, container, false)
        initView()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycle_cal_event)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initView(){
        cal = view.findViewById(R.id.custom_cal)
        recyclerView = view.findViewById(R.id.recycle_cal_event)
        gridView = cal.getGridView()
        nextBtn = cal.getNextBtn()
        preBtn = cal.getPreBtn()
        floatBtnAddEvent = view.findViewById(R.id.float_btn_add_event_cal)
        emptyState = view.findViewById(R.id.empty_state)
        upComingTxt = view.findViewById(R.id.textView3)
        setupCal()
        nextBtn.setOnClickListener{
            cal2.add(Calendar.MONTH,+1)
            setupCal()
        }

        preBtn.setOnClickListener{
            cal2.add(Calendar.MONTH,-1)
            setupCal()
        }
        currentDatePos = getCurrentDatePosition()
        currentPosition = currentDatePos
        Log.d("currentDatePos", getCurrentDatePosition().toString())
        initRecView(currentDatePos)
        setOnClickItemCal()
        addEventOnClick()
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    private fun setOnClickItemCal(){
        gridView.setOnItemClickListener { parent, view, position, id ->
            currentDatePos = getCurrentDatePosition()
            currentPosition = position
            if(position!= currentDatePos) {
                view.background = parent.context.resources
                    .getDrawable(R.drawable.bg_cell_gridview_select,null)
            }
            for(i in 0 until dates.size){
                val c :View = parent.getChildAt(i)
                if(i!=position && i!=currentDatePos){
                    c.background = parent.context.resources
                        .getDrawable(R.drawable.bg_normal_day,null)
                }
            }
            initRecView(position)
        }

    }
    private fun saveEvent(name:String, time:String, date:String, month:String, year: String){
        val dbOpen = DBOpenHelper(cal.getContexts())
        val db = dbOpen.writableDatabase
        dbOpen.saveEvent(name,time,date,month,year,db)
        db.close()
        Toast.makeText(context,"Saved!",Toast.LENGTH_SHORT).show()
    }
    private fun setupCal(){
        val currentDate = cal.getCurrentDate()

        val currentDates = dateFormat.format(cal2.time)
        currentDate.text = currentDates
        dates.clear()

        val month = cal2.clone() as Calendar
        month.set(Calendar.DAY_OF_MONTH,1)

        val firstDayOfMonth = month.get(Calendar.DAY_OF_WEEK) - 2
        Log.d("firstDayOfMonth", "${month.get(Calendar.DAY_OF_WEEK)}")
        month.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)
        getEventPerMonth(monthFormat.format(cal2.time), yearFormat.format(cal2.time))
        while (dates.size < MAX_DAY){
            dates.add(month.time)
            month.add(Calendar.DAY_OF_MONTH,1)
        }
        gridAdapter = GridAdapter(cal.getContexts(),dates,cal2,eventMonthList)
        gridView.adapter = gridAdapter
    }
    @SuppressLint("Range")
    private fun getEventPerMonth(month: String, year:String){
        eventMonthList.clear()
        val dbOpen = DBOpenHelper(cal.getContexts())
        val db: SQLiteDatabase = dbOpen.readableDatabase
        val cursor : Cursor = dbOpen.readEventMonth(month,year, db)
        while (cursor.moveToNext()){
            val event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT))
            val time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME))
            val date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE))
            val months = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH))
            val years = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR))
            val e = Events(event, time, date, months, years)
            eventMonthList.add(e)
        }
        cursor.close()
        dbOpen.close()
    }

    @SuppressLint("Range")
    private fun getEventPerDay(day: String){
        eventDayList.clear()
        val dbOpen = DBOpenHelper(cal.getContexts())
        val db: SQLiteDatabase = dbOpen.readableDatabase
        val cursor : Cursor = dbOpen.readEventDate(day,db)
        while (cursor.moveToNext()){
            val event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT))
            val time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME))
            val date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE))
            val months = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH))
            val years = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR))
            val e = Events(event, time, date, months, years)
            eventDayList.add(e)
        }
        cursor.close()
        dbOpen.close()
    }

    private fun initRecView(position:Int){
        recyclerView.layoutManager = LinearLayoutManager(context)
        val date = dateFormatSave.format(dates[position])
        getEventPerDay(date)
        if(eventDayList.size > 0) {
            emptyState.visibility = View.GONE
            upComingTxt.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            Log.d("listEventDate", eventDayList.size.toString())
            recyclerAdapter = EventRecyclerAdapter(requireContext(), eventDayList,
                object : OnClickItemListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onClick(position: Int) {
                        deleteEvent(
                            eventDayList[position].Event,
                            eventDayList[position].Date,
                            eventDayList[position].Time
                        )
                        eventDayList.removeAt(position)
                        recyclerAdapter.notifyDataSetChanged()
                        setupCal()
                    }
                })
            recyclerView.adapter = recyclerAdapter
            hideFabWhenScroll()
        }else{
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            upComingTxt.visibility = View.GONE
        }
    }

    private fun addEventOnClick(){
        val cal3 = Calendar.getInstance()
        floatBtnAddEvent.setOnClickListener{
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val addEventView :View = LayoutInflater.from(context)
                .inflate(R.layout.add_event_layout,null)
            val eventName: EditText = addEventView.findViewById(R.id.event_title)
            val eventTime: LinearLayout = addEventView.findViewById(R.id.event_time)
            val addEvent: Button = addEventView.findViewById(R.id.event_add_btn)
            val timeSet: TextView = addEventView.findViewById(R.id.time_set)
            val currentTime = simpleDateFormat.format(cal3.time)
            timeSet.text = currentTime.toString()

            cal3.time = dates[currentPosition]
            alarmYear= cal3.get(Calendar.YEAR)
            alarmMonth = cal3.get(Calendar.MONTH)
            alarmDay = cal3.get(Calendar.DAY_OF_MONTH)

            eventTime.setOnClickListener{
                val cal : Calendar = Calendar.getInstance()
                val hours = cal.get(Calendar.HOUR_OF_DAY)
                val min = cal.get(Calendar.MINUTE)

                val timePicker = TimePickerDialog(addEventView.context,
                    R.style.MyTimePickerDialogTheme,
                    { view, hourOfDay, minute ->
                        val c = Calendar.getInstance()
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                        c.set(Calendar.MINUTE,minute)
                        c.timeZone = TimeZone.getDefault()
                        val time = simpleDateFormat.format(c.time)
                        timeSet.text = time

                        alarmHour = c.get(Calendar.HOUR_OF_DAY)
                        alarmMinute = c.get(Calendar.MINUTE)
                    },hours,min,false)
                timePicker.show()
            }

            val date = dateFormatSave.format(dates[currentPosition])
            Log.d("Date Saved", date.toString())
            val month = monthFormat.format(dates[currentPosition])
            val year = yearFormat.format(dates[currentPosition])

            addEvent.setOnClickListener {
                val name = eventName.text.toString()
                if(name.isBlank()){
                    Toast.makeText(context,"Title is not blank!", Toast.LENGTH_SHORT).show()
                }
                else {
                    saveEvent(name, timeSet.text.toString(), date, month, year)
                    setupCal()
                    val calendar = Calendar.getInstance()
                    val now = calendar.timeInMillis
                    calendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute)
                    Log.d("alarm", "$alarmMinute $alarmHour ")
                    val alarmTime = calendar.timeInMillis
                    if(now <= alarmTime) {
                        setAlarm(
                            calendar,
                            name,
                            timeSet.text.toString(),
                            getCode(date, name, timeSet.text.toString())
                        )
                    }
                    alertDialog.dismiss()
                    initRecView(currentPosition)
                }
            }
            builder.setView(addEventView)
            alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_alert_dialog)
        }
    }

    private fun getCurrentDatePosition(): Int {
        val cal3 = cal2.clone() as Calendar
//        Log.d("time", (dates[17]== cal3.time).toString())
//        Log.d("time2", cal3.time.toString())
        return dates.indexOf(cal3.time)
    }

    private fun setAlarm (cal:Calendar, event:String, time:String, code:Int){
        isPushNotificationGranted = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                android.Manifest.permission.POST_NOTIFICATIONS)
        } == PackageManager.PERMISSION_GRANTED
        Log.d("per", isPushNotificationGranted.toString())
            if(!isPushNotificationGranted){
                val builder : AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setCancelable(true)
                val viewDialog = LayoutInflater.from(context).
                                    inflate(R.layout.alert_dialog_request_permission,null)
                builder.setView(viewDialog)
                val alertDialog : AlertDialog = builder.create()
                alertDialog.show()
                alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_alert_dialog)
                val okButton = viewDialog.findViewById<Button>(R.id.alertDialogOK)

                okButton.setOnClickListener {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", "com.example.calendar", null)
                    })
                    alertDialog.dismiss()
                }

                val cancelButton = viewDialog.findViewById<Button>(R.id.alertDialogCancel)
                cancelButton.setOnClickListener {
                    alertDialog.cancel()
                }
            }
        val i = Intent(context, AlarmReceiver::class.java)
        i.putExtra("event",event)
        i.putExtra("time",time)
        i.putExtra("id",code)
        val pendingIntent:PendingIntent = PendingIntent
                                        .getBroadcast(context,code,i,PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis,pendingIntent)

    }

    @SuppressLint("Range")
    private fun getCode(date: String, event:String, time : String):Int{
        var code = 0
        val dbOpen = DBOpenHelper(cal.getContexts())
        val db: SQLiteDatabase = dbOpen.readableDatabase
        val cursor : Cursor = dbOpen.readIDEvents(date,event,time,db)
        while (cursor.moveToNext()) {
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.ID))
            Log.d("request code", "$code")
        }
        cursor.close()
        dbOpen.close()
        return code
    }

    private fun deleteEvent(event: String , date: String, time : String){
        val dbOpen = DBOpenHelper(cal.getContexts())
        val db  = dbOpen.writableDatabase
        dbOpen.deleteEvent(event,date,time,db)
        dbOpen.close()
    }

    private fun hideFabWhenScroll(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy<0&&!floatBtnAddEvent.isShown){
                    floatBtnAddEvent.show()
                }
                else if(dy>0 && floatBtnAddEvent.isShown){
                    floatBtnAddEvent.hide()
                }
            }
        })
    }
}