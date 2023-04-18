package com.example.calendar

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.calendar.DB.DBOpenHelper
import com.example.calendar.DB.DBStructure
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class ConvertDateActivity : AppCompatActivity() {

    private lateinit var  solarDayLayout :TextInputLayout
    private lateinit var  textInputSolarDay :TextInputEditText
    private lateinit var pickSolarDay : Button
    private lateinit var result : Button
    private lateinit var backBtn :FloatingActionButton
    private lateinit var resultText: TextView
    private lateinit var fromDateType: TextView
    private lateinit var convertTo: TextView
    private lateinit var swap: ImageView
    private lateinit var addEvent: Button
    private val dateFormatSave = SimpleDateFormat("dd/MM/yyyy")
    var monthFormat = SimpleDateFormat("MM")
    var yearFormat = SimpleDateFormat("yyyy")
    private var alarmYear = -1
    private var alarmMonth = -1
    private var alarmDay = -1
    private var alarmHour = -1
    private var alarmMinute = -1
    private lateinit var alertDialog : AlertDialog

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
        addEvent = findViewById(R.id.convertDate_addEvent)
        backBtn = findViewById(R.id.btnBack)
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
        textInputSolarDay.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                convertDate()
                return@setOnEditorActionListener true
            }
            false
        }
        textInputSolarDay.setOnClickListener {
            solarDayLayout.error = null
        }

        addEventOnClick()

        backBtn.setOnClickListener {
            val i = Intent(this,HomeActivity::class.java)
            startActivity(i)
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
                resultText.text = "${lunaDateArray[0]}/${lunaDateArray[1]}/${lunaDateArray[2]}"
            }else{
                val solarDateArray  =  convertDate.convertLunar2Solar(dateNum,monthNum,yearNum,0,7.0)
                resultText.text = "${solarDateArray[0]}/${solarDateArray[1]}/${solarDateArray[2]}"
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

    private fun addEventOnClick(){
        val cal3 = Calendar.getInstance()
        addEvent.setOnClickListener{
            val date:String = textInputSolarDay.text.toString()
            if(date.isBlank()){
                solarDayLayout.error = "The date is not empty!"
//                return@setOnClickListener
            }
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val addEventView : View = LayoutInflater.from(this)
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


            addEvent.setOnClickListener {
                val name = eventName.text.toString()
                if(name.isBlank()){
                    Toast.makeText(this,"Title is not blank!", Toast.LENGTH_SHORT).show()
                }
                else {

                    Log.d("Date Saved2", dateFormatSave.parse(date).toString())
                    if(fromDateType.text.equals("Solar")) {
                        val dateEvent = dateFormatSave.parse(date)
                        cal3.time = dateEvent
                        val dateSave = dateFormatSave.format(dateEvent)
                        val monthSave = monthFormat.format(dateEvent)
                        val yearSave = yearFormat.format(dateEvent)

                        saveEvent(name, timeSet.text.toString(), dateSave, monthSave,yearSave)
                    }else{
                        val dateEvent = dateFormatSave.parse(resultText.text.toString())
                        cal3.time = dateEvent
                        val dateSave = dateFormatSave.format(dateEvent)
                        val monthSave = monthFormat.format(dateEvent)
                        val yearSave = yearFormat.format(dateEvent)
                        saveEvent(name, timeSet.text.toString(), dateSave, monthSave,yearSave)
                    }
                    alarmYear= cal3.get(Calendar.YEAR)
                    alarmMonth = cal3.get(Calendar.MONTH)
                    alarmDay = cal3.get(Calendar.DAY_OF_MONTH)
                    Log.d("Date alarm ", cal3.time.toString())
                    val calendar = Calendar.getInstance()
                    val now = calendar.timeInMillis
                    calendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute)
                    Log.d("alarm2", "$alarmMinute $alarmHour ")
                    val alarmTime = calendar.timeInMillis
                    Log.d("alarmTime", calendar.time.toString())
                    if(now <= alarmTime) {
                        setAlarm(
                            calendar,
                            name,
                            timeSet.text.toString(),
                            getCode(date, name, timeSet.text.toString())
                        )
                    }
                    alertDialog.dismiss()
                }
            }
            builder.setView(addEventView)
            alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_alert_dialog)
        }
    }
    @SuppressLint("Range")
    private fun getCode(date: String, event:String, time : String):Int{
        var code = 0
        val dbOpen = DBOpenHelper(this)
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

    private fun setAlarm (cal:Calendar, event:String, time:String, code:Int){
        val i = Intent(this,AlarmReceiver::class.java)
        i.putExtra("event",event)
        i.putExtra("time",time)
        i.putExtra("id",code)
        val pendingIntent: PendingIntent = PendingIntent
            .getBroadcast(this,code,i, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis,pendingIntent)

    }
    private fun saveEvent(name:String, time:String, date:String, month:String, year: String){
        val dbOpen = DBOpenHelper(this)
        val db = dbOpen.writableDatabase
        dbOpen.saveEvent(name,time,date,month,year,db)
        db.close()
        Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show()
    }
}