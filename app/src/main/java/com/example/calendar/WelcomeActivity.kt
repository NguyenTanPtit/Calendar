package com.example.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.calendar.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.reflect.InvocationTargetException

class WelcomeActivity : AppCompatActivity() {
    private lateinit var loginBtn: AppCompatButton
    private lateinit var loginGG: LinearLayout
    private lateinit var skip: LinearLayout
    private lateinit var register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
//        testCreateUser()
    }

    private fun initView(){
        loginBtn = findViewById(R.id.btnLogin)
        loginGG = findViewById(R.id.btnGoogle)
        skip = findViewById(R.id.layout_skip)
        register = findViewById(R.id.txtRegister)

        loginBtn.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }

        skip.setOnClickListener {
            val i = Intent(this,HomeActivity::class.java)
            startActivity(i)
        }

        register.setOnClickListener{
            val i = Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }
    }
    private fun testCreateUser(){
        try {
            val database = Firebase.database.reference
            val user = User("NguyenTan","tan@gmail.com","123")
            database.child("Test").child("dhakjkda").setValue(user).addOnSuccessListener {
                println("pass")
            }
        }catch (e: InvocationTargetException){
            Log.d("exxxxxxx", "${e.targetException}")
        }

    }
}