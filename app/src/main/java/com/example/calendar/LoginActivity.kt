package com.example.calendar


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException


class LoginActivity : AppCompatActivity() {

    private lateinit var imgBack: ImageView
    private lateinit var img4: ImageView
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var loginBtn: AppCompatButton
    private lateinit var emailError: TextView
    private lateinit var loginError: TextView

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        imgBack = findViewById(R.id.img_back)
        img4= findViewById(R.id.imageView4)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        loginBtn = findViewById(R.id.btnLogin)
        emailError = findViewById(R.id.error_email)
        loginError = findViewById(R.id.error_login)
        auth = FirebaseAuth.getInstance()


        imgBack.setOnClickListener {
            finish()
        }

        loginBtn.setOnClickListener {
            val email = edtEmail.text.toString()
            val pass = edtPassword.text.toString()
            if(email.isBlank()){
                emailError.visibility = View.VISIBLE
                edtEmail.clearFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailError.visibility = View.VISIBLE
                edtEmail.clearFocus()
                return@setOnClickListener
            }
            if(pass.isBlank()){
                loginError.visibility = View.VISIBLE
                edtPassword.clearFocus()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                if(it.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }else if(it.exception!=null){
                    val exception: FirebaseAuthException = it.exception as FirebaseAuthException
                    when(exception.errorCode){
                        "ERROR_WRONG_PASSWORD" ->{
                            loginError.text = "Password is incorrect!"
                            loginError.visibility = View.VISIBLE
                            edtPassword.clearFocus()
                            edtEmail.clearFocus()
                        }
                        "ERROR_USER_NOT_FOUND"->{
                            loginError.text = "There is no user exist with this credential!"
                            loginError.visibility = View.VISIBLE
                            edtPassword.clearFocus()
                            edtEmail.clearFocus()
                        }
                    }
                }
            }

        }

        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                emailError.visibility = View.GONE
                loginError.visibility = View.GONE
            }
        }

        edtPassword.setOnFocusChangeListener{_, hasFocus ->
            if(hasFocus){
                loginError.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                emailError.visibility = View.GONE
                loginError.visibility = View.GONE
            }
        }
        edtPassword.setOnFocusChangeListener{_, hasFocus ->
            if(hasFocus){
                loginError.visibility = View.GONE
            }
        }
    }

}