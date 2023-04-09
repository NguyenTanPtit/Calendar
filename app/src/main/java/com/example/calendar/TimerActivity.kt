package com.example.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.github.guilhe.views.CircularProgressView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
    }

    private fun startTimer(){
        timerCountDown = object : CountDownTimer(timerLengthSecond,1000){
            override fun onTick(millisUntilFinished: Long){
                secondsRemaining = millisUntilFinished
                updateCountDown()
            }
            override fun onFinish() {

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