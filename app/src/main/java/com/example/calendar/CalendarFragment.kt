package com.example.calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var cal :CustomCalendarView
    private lateinit var view : View
    private lateinit var gridView : GridView
    private lateinit var alertDialog : AlertDialog
    private lateinit var gridAdapter: GridAdapter
    private lateinit var nextBtn: ImageView
    private lateinit var preBtn: ImageView

    private val MAX_DAY = 42

    private var dateFormat = SimpleDateFormat("MMM yyyy")
    private val dateFormatSave = SimpleDateFormat("dd/MM/yyyy")
    var monthFormat = SimpleDateFormat("MM")
    var yearFormat = SimpleDateFormat("yyyy")
    private var dates : MutableList<Date> = mutableListOf()
    private var eventList : MutableList<Events> = mutableListOf()
    private val cal2: Calendar = Calendar.getInstance()

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
        gridView = cal.getGridView()
        nextBtn = cal.getNextBtn()
        preBtn = cal.getPreBtn()
        setupCal()
        nextBtn.setOnClickListener{
            cal2.add(Calendar.MONTH,+1)
            setupCal()
        }

        preBtn.setOnClickListener{
            cal2.add(Calendar.MONTH,-1)
            setupCal()
        }
        setOnClickItemCal()
    }

    private fun setOnClickItemCal(){
        val cal3 = Calendar.getInstance()
        gridView.setOnItemClickListener{ adapterView, view, position, id ->
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val simpleDateFormat = SimpleDateFormat("HH:mm a")
            val addEventView :View = LayoutInflater.from(adapterView.context)
                .inflate(R.layout.add_event_layout,null)
            val eventName: EditText = addEventView.findViewById(R.id.event_title)
            val eventTime: LinearLayout = addEventView.findViewById(R.id.event_time)
            val addEvent: Button = addEventView.findViewById(R.id.event_add_btn)
            val timeSet: TextView = addEventView.findViewById(R.id.time_set)
            val currentTime = simpleDateFormat.format(cal3.time)
            timeSet.text = currentTime.toString()

            eventTime.setOnClickListener{
                val cal : Calendar = Calendar.getInstance()
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
            Log.d("Date Saved", date.toString())
            val month = monthFormat.format(dates[position])
            val year = yearFormat.format(dates[position])

            addEvent.setOnClickListener {
                val name = eventName.text.toString()
                if(name.isBlank()){
                    Toast.makeText(context,"Title is not blank!", Toast.LENGTH_SHORT).show()
                }
                else {
                    saveEvent(name, timeSet.text.toString(), date, month, year)
                    setupCal()
                    alertDialog.dismiss()
                }
            }

            builder.setView(addEventView)
            alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_alert_dialog)
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

        val firstDayOfMonth = month.get(Calendar.DAY_OF_WEEK) -2
        month.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)
        getEventPerMonth(monthFormat.format(cal2.time), yearFormat.format(cal2.time))
        while (dates.size < MAX_DAY){
            dates.add(month.time)
            month.add(Calendar.DAY_OF_MONTH,1)
        }
        gridAdapter = GridAdapter(cal.getContexts(),dates,cal2,eventList)
        gridView.adapter = gridAdapter
    }
    @SuppressLint("Range")
    private fun getEventPerMonth(month: String, year:String){
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
            eventList.add(e)
        }
        cursor.close()
        dbOpen.close()
    }
}