package com.example.calendar

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class HomeActivity : AppCompatActivity() {

    private lateinit var homeLayout : LinearLayout
    private lateinit var litLayout : LinearLayout
    private lateinit var weatherLayout : LinearLayout
    private lateinit var profileLayout : LinearLayout

    private lateinit var homeTv:TextView
    private lateinit var litTv:TextView
    private lateinit var weatherTv:TextView
    private lateinit var profileTv:TextView

    private lateinit var homeImg:ImageView
    private lateinit var litImg:ImageView
    private lateinit var weatherImg:ImageView
    private lateinit var profileImg:ImageView

    private lateinit var fragment: Fragment

    private var selectedTab :Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init(){
        homeLayout = findViewById(R.id.nav_homeLayout)
        litLayout=findViewById(R.id.nav_literatureLayout)
        weatherLayout=findViewById(R.id.nav_weatherLayout)
        profileLayout=findViewById(R.id.nav_profileLayout)

        homeTv=findViewById(R.id.nav_tv_home)
        litTv=findViewById(R.id.nav_tv_lit)
        weatherTv=findViewById(R.id.nav_tv_weather)
        profileTv=findViewById(R.id.nav_tv_profile)

        homeImg=findViewById(R.id.nav_img_home)
        litImg=findViewById(R.id.nav_img_lit)
        weatherImg=findViewById(R.id.nav_img_weather)
        profileImg=findViewById(R.id.nav_img_profile)

        //default fragment 1
        fragment  = CalendarFragment()
        loadFragment(fragment)

        setOnclick()
    }

    private fun setOnclick(){
        homeLayout.setOnClickListener((View.OnClickListener {
            if(selectedTab!=1) {
                //select Home
                homeTv.visibility = View.VISIBLE
                homeImg.layoutParams?.width = 65
                homeImg.setImageResource(R.drawable.calendar_selected_icon)
                homeLayout.setBackgroundResource(R.drawable.bot_nav_back_item)
                fragment = CalendarFragment()
                loadFragment(fragment)

                //unselect 3 tab
                litTv.visibility = View.GONE
                litImg.layoutParams?.width = 80
                litImg.setImageResource(R.drawable.features_icon)
                litLayout.setBackgroundResource(android.R.color.transparent)
                weatherTv.visibility = View.GONE
                weatherImg.layoutParams?.width = 80
                weatherImg.setImageResource(R.drawable.weather)
                weatherLayout.setBackgroundResource(android.R.color.transparent)
                profileTv.visibility = View.GONE
                profileImg.layoutParams?.width = 80
                profileImg.setImageResource(R.drawable.user)
                profileLayout.setBackgroundResource(android.R.color.transparent)

                val anim = ScaleAnimation(0.8f,1.0f,1f,1f,
                    Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,0.0f)
                anim.duration = 350
                anim.fillAfter = true
                homeLayout.startAnimation(anim)
                selectedTab = 1
            }
        }))

        litLayout.setOnClickListener{
            if(selectedTab!=2) {
                //select Home
                litTv.visibility = View.VISIBLE
                litImg.layoutParams?.width = 65
                litImg.setImageResource(R.drawable.features_selected_icon)
                litLayout.setBackgroundResource(R.drawable.bot_nav_back_item)
                fragment = LitFragment()
                loadFragment(fragment)

                //unselect 3 tab
                homeTv.visibility = View.GONE
                homeImg.layoutParams?.width = 80
                homeImg.setImageResource(R.drawable.calendar_icon)
                homeLayout.setBackgroundResource(android.R.color.transparent)
                weatherTv.visibility = View.GONE
                weatherImg.layoutParams?.width = 80
                weatherImg.setImageResource(R.drawable.weather)
                weatherLayout.setBackgroundResource(android.R.color.transparent)
                profileTv.visibility = View.GONE
                profileImg.layoutParams?.width = 80
                profileImg.setImageResource(R.drawable.user)
                profileLayout.setBackgroundResource(android.R.color.transparent)

                val anim = ScaleAnimation(0.8f,1.0f,1f,1f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.0f)
                anim.duration = 350
                anim.fillAfter = true
                litLayout.startAnimation(anim)

                selectedTab = 2
            }
        }
        weatherLayout.setOnClickListener{
            if(selectedTab!=3) {
                //select Home
                weatherTv.visibility = View.VISIBLE
                weatherImg.layoutParams?.width = 65
                weatherImg.setImageResource(R.drawable.weather_selected_icon)
                weatherLayout.setBackgroundResource(R.drawable.bot_nav_back_item)
                fragment = WeatherFragment()
                loadFragment(fragment)

                //unselect 3 tab
                litTv.visibility = View.GONE
                litImg.layoutParams?.width = 80
                litImg.setImageResource(R.drawable.features_icon)
                litLayout.setBackgroundResource(android.R.color.transparent)
                homeTv.visibility = View.GONE
                homeImg.layoutParams?.width = 80
                homeImg.setImageResource(R.drawable.calendar_icon)
                homeLayout.setBackgroundResource(android.R.color.transparent)
                profileTv.visibility = View.GONE
                profileImg.layoutParams?.width = 80
                profileImg.setImageResource(R.drawable.user)
                profileLayout.setBackgroundResource(android.R.color.transparent)

                val anim = ScaleAnimation(0.8f,1.0f,1f,1f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.0f)
                anim.duration = 350
                anim.fillAfter = true
                weatherLayout.startAnimation(anim)

                selectedTab = 3
            }
        }
        profileLayout.setOnClickListener{
            if(selectedTab!=4) {
                //select Home
                profileTv.visibility = View.VISIBLE
                profileImg.layoutParams?.width = 65
                profileImg.setImageResource(R.drawable.user_selected_icon)
                profileLayout.setBackgroundResource(R.drawable.bot_nav_back_item)
                fragment = ProfileFragment()
                loadFragment(fragment)

                //unselect 3 tab
                litTv.visibility = View.GONE
                litImg.layoutParams?.width = 80
                litImg.setImageResource(R.drawable.features_icon)
                litLayout.setBackgroundResource(android.R.color.transparent)
                weatherTv.visibility = View.GONE
                weatherImg.layoutParams?.width = 80
                weatherImg.setImageResource(R.drawable.weather)
                weatherLayout.setBackgroundResource(android.R.color.transparent)
                homeTv.visibility = View.GONE
                homeImg.layoutParams?.width = 80
                homeImg.setImageResource(R.drawable.calendar_icon)
                homeLayout.setBackgroundResource(android.R.color.transparent)

                val anim = ScaleAnimation(0.8f,1.0f,1f,1f,
                    Animation.RELATIVE_TO_SELF,1.0f,
                    Animation.RELATIVE_TO_SELF,0.0f)
                anim.duration = 350
                anim.fillAfter = true
                profileLayout.startAnimation(anim)

                selectedTab = 4
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_home,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}


