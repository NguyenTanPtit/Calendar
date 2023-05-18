package com.example.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.calendar.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText

    private lateinit var fullNameError: TextView
    private lateinit var emailError: TextView
    private lateinit var passwordError: TextView
    private lateinit var confirmPasswordError: TextView
    private lateinit var registerError: TextView

    private lateinit var signUpBtn: AppCompatButton

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        edtFullName = findViewById(R.id.edt_full_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPassword = findViewById(R.id.edt_confirm_password)
        fullNameError = findViewById(R.id.full_name_error)
        emailError = findViewById(R.id.email_error)
        passwordError = findViewById(R.id.password_error)
        confirmPasswordError = findViewById(R.id.confirm_password_error)
        signUpBtn = findViewById(R.id.btnRegister)
        registerError = findViewById(R.id.register_error)
        auth = FirebaseAuth.getInstance()


        signUpBtn.setOnClickListener {
            val fullName = edtFullName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPass = edtConfirmPassword.text.toString().trim()

            if(fullName.isBlank()){
                fullNameError.visibility = View.VISIBLE
                edtFullName.clearFocus()
                return@setOnClickListener
            }
            if(email.isBlank()){
                emailError.visibility = View.VISIBLE
                edtEmail.clearFocus()
                return@setOnClickListener
            }
            if(password.isBlank()){
                passwordError.visibility = View.VISIBLE
                edtPassword.clearFocus()
                return@setOnClickListener
            }
            if(password.length<6||password.length>18){
                passwordError.text = "Password length must be between 6-18 characters"
                passwordError.visibility = View.VISIBLE
                edtPassword.clearFocus()
                return@setOnClickListener
            }
            if(confirmPass.isBlank()){
                confirmPasswordError.text = "Confirm password is required!"
                confirmPasswordError.visibility = View.VISIBLE
                edtConfirmPassword.clearFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailError.visibility = View.VISIBLE
                edtEmail.clearFocus()
                return@setOnClickListener
            }
            if(confirmPass != password){
                confirmPasswordError.text = "Confirm password don't match!"
                confirmPasswordError.visibility = View.VISIBLE
                edtConfirmPassword.clearFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{it1 ->
                if(it1.isSuccessful){
                    val user = User(null,fullName,email,password,null,null)
                    if(auth.currentUser!=null){
                        val id :String = auth.currentUser!!.uid
                        user.id = id
                        addUser(user)
                    }
                    else{
                        Toast.makeText(this,"NULL",Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    val exception: FirebaseAuthException = it1.exception as FirebaseAuthException
                    when(exception.errorCode){
                        "ERROR_EMAIL_ALREADY_IN_USE" ->{
                            emailError.text = "The email address is already in use by another account."
                            emailError.visibility = View.VISIBLE
                            edtEmail.clearFocus()
                        }
                        else -> {
                            registerError.text = "Fail to register!"
                            registerError.visibility = View.VISIBLE
                            edtConfirmPassword.clearFocus()
                        }
                    }
                }
            }

        }

        edtFullName.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                fullNameError.visibility = View.GONE
            }
        }
        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                emailError.visibility = View.GONE
            }
        }
        edtPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                passwordError.visibility = View.GONE
            }
        }
        edtConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                confirmPasswordError.visibility = View.GONE
            }
        }
    }

    private fun addUser(user: User){
        database = Firebase.database
        auth.currentUser?.let {
            database.getReference("Users").child(it.uid).setValue(user).addOnSuccessListener {
                Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Fail to register", Toast.LENGTH_SHORT).show()
            }
        }
    }

}