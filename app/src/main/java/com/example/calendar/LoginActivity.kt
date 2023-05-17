package com.example.calendar


import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    private lateinit var imgBack: ImageView
    private lateinit var img4: ImageView
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var loginBtn: AppCompatButton
    private lateinit var emailError: TextView

    private lateinit var auth : FirebaseAuth
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        initView()
    }

    private fun initView(){
        imgBack = findViewById(R.id.img_back)
        img4= findViewById(R.id.imageView4)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        loginBtn = findViewById(R.id.btnLogin)
        emailError = findViewById(R.id.error_email)
        auth = FirebaseAuth.getInstance()


        imgBack.setOnClickListener {
            finish()
        }

        loginBtn.setOnClickListener {
            val email = edtEmail.text
            if(email.isEmpty()){
                emailError.visibility = View.VISIBLE
                edtEmail.clearFocus()
                return@setOnClickListener
            }
        }
        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                emailError.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                emailError.visibility = View.GONE
            }
        }
    }

}