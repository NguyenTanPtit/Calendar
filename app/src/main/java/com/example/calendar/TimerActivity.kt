package com.example.calendar

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.github.guilhe.views.CircularProgressView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TimerActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped, Paused, Running
    }
    private lateinit var start: FloatingActionButton
    private lateinit var pause: FloatingActionButton
    private lateinit var stop: FloatingActionButton
    private lateinit var time: TextView
    private lateinit var timerCountDown: CountDownTimer
    private lateinit var progressBar: CircularProgressView
    private lateinit var backBtn: ImageView
    private lateinit var setTime : AppCompatButton
    private lateinit var dialog : AlertDialog
    private var timerState = TimerState.Stopped

    private var timerLengthSecond = 600000L
    private var secondsRemaining = timerLengthSecond


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        initView()
    }

    private fun initView(){
        start = findViewById(R.id.fab_start)
        pause = findViewById(R.id.fab_pause)
        stop = findViewById(R.id.fab_stop)
        time = findViewById(R.id.time)
        backBtn = findViewById(R.id.btnBack)
        setTime = findViewById(R.id.btn_setTime)
        progressBar = findViewById(R.id.progress_circular)
        progressBar.setProgress(0F,true,1000)
        setOnclickBtn()
    }

    private fun setOnclickBtn(){
        start.setOnClickListener{
            startTimer()
        }

        pause.setOnClickListener{
            pauseTimer()
        }

        stop.setOnClickListener{
            secondsRemaining = timerLengthSecond
            updateCountDown()
            pause.isEnabled = false
            start.isEnabled = true
        }

        backBtn.setOnClickListener{
            dialog.dismiss()
            val i = Intent(this,HomeActivity::class.java)
            i.putExtra("fragment","lit")
            setResult(RESULT_OK,i)
            finish()
        }

        setTime.setOnClickListener{
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            val setTimeView : View = LayoutInflater.from(this)
                .inflate(R.layout.dialog_set_time_countdown,null)
            val setTimeBtn : Button = setTimeView.findViewById(R.id.dialog_setTime_btn)
            val textInputLayout : TextInputLayout = setTimeView.findViewById(R.id.edt_setTime)
            textInputLayout.helperText = "Default time will be 10 minutes"
            val edtTime : TextInputEditText = setTimeView.findViewById(R.id.textInputSetTime)
            edtTime.addTextChangedListener (object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //TODO("Not yet implemented")
                }


                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var time = edtTime.text.toString()
                    if(time.isNotBlank()){
                        if(time.length ==2||time.length == 5){
                            time += ":"
                            edtTime.text =  Editable.Factory.getInstance().newEditable(time)
                            edtTime.setSelection(time.length)
                        }
                        if(time.length == 8){
//                            edtTime.
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    //TODO("Not yet implemented")
                }


            })


            val timeInput : String = edtTime.text.toString()
            if(timeInput.isBlank()){
                timerLengthSecond = 600000L
            }else{
                setTimeBtn.setOnClickListener {

                }
            }
            builder.setView(setTimeView)
            dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_alert_dialog)
        }
    }

    private fun startTimer(){
        if(timerState == TimerState.Paused){
            timerCountDown = object : CountDownTimer(secondsRemaining,1000){
                override fun onTick(millisUntilFinished: Long){
                    secondsRemaining = millisUntilFinished
                    updateCountDown()
                }
                override fun onFinish() {

                }
            }
        }else {
            timerCountDown = object : CountDownTimer(timerLengthSecond, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = millisUntilFinished
                    updateCountDown()
                }

                override fun onFinish() {

                }
            }
        }
        timerState = TimerState.Running
        timerCountDown.start()
        start.isEnabled = false
        pause.isEnabled = true
    }

    private fun updateCountDown(){
        val min = (secondsRemaining / 1000) /60
        val seconds = (secondsRemaining/1000) %60
        val timeFormat = String.format("%02d:%02d",min,seconds)
        time.text = timeFormat
        val progress = (secondsRemaining.toDouble()/timerLengthSecond) * 100
        progressBar.setProgress((100-progress).toFloat(),true,1000 )
    }

    private fun pauseTimer(){
        timerCountDown.cancel()
        timerState = TimerState.Paused
        pause.isEnabled = false
        start.isEnabled = true
    }
}