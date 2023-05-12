package com.example.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class StopWatchActivity : AppCompatActivity() {

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var time :TextView
    private lateinit var back : ImageView
    private lateinit var saveBtn: AppCompatButton
    private lateinit var resetBtn: AppCompatButton
    private lateinit var playBtn: FloatingActionButton
    private lateinit var listHistory: ListView
    private var timeState = TimerState.Stopped
    private var second : Int = 0
    private lateinit var listTime : MutableList<String>
    private var stt = 1
    private lateinit var adapter : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_watch)
        initView()
    }

    private fun initView(){
        time = findViewById(R.id.time)
        back = findViewById(R.id.back_icon)
        saveBtn = findViewById(R.id.saveBtn)
        resetBtn = findViewById(R.id.resetBtn)
        playBtn = findViewById(R.id.floatingActionButtonPlay)
        listHistory = findViewById(R.id.historyTime)
        if(timeState == TimerState.Stopped || timeState == TimerState.Paused) {
            playBtn.setImageResource(R.drawable.icon_play)
        }else{
            playBtn.setImageResource(R.drawable.pause_icon)
        }
        listTime = mutableListOf()
        adapter  = ArrayAdapter(this,R.layout.listview_item_stopwatch,
        listTime)
        listHistory.adapter = adapter

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE

        val handler = Handler()
        handler.post(object : Runnable{
            override fun run() {
                val hour = second / 3600
                val min = (second%3600)/60
                val sec = second % 60

                val mTime = String.format(Locale.getDefault(),"%02d:%02d:%02d",
                    hour,min,sec)
                time.text = mTime
                if(timeState == TimerState.Running){
                    time.clearAnimation()
                    second++
                }
                if(timeState == TimerState.Paused){
                    time.startAnimation(anim)
                }
                handler.postDelayed(this,1000)
            }
        })
        setOnClick()
    }

    private fun setOnClick(){
        back.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            i.putExtra("fragment", "lit")
            setResult(RESULT_OK, i)
            finish()
        }

        playBtn.setOnClickListener{
            onRunning()
        }

        resetBtn.setOnClickListener {
            onReset()
        }
        saveBtn.setOnClickListener {
            onSave()
        }
    }

    private fun onSave() {
        val timeSave : String = time.text.toString()
        listTime.add("#$stt  $timeSave")
        stt++
        adapter.notifyDataSetChanged()
    }

    private fun onRunning(){
        if (timeState==TimerState.Stopped || timeState == TimerState.Paused){
            timeState = TimerState.Running
            playBtn.setImageResource(R.drawable.pause_icon)
        }else {
            timeState = TimerState.Paused
            playBtn.setImageResource(R.drawable.icon_play)
        }
    }

    private fun onReset(){
        second = 0
        timeState = TimerState.Stopped
        time.clearAnimation()
        playBtn.setImageResource(R.drawable.icon_play)
        listTime.clear()
        adapter.notifyDataSetChanged()
    }

}