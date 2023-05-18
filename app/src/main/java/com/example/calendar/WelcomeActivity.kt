package com.example.calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class WelcomeActivity : AppCompatActivity() {
    private lateinit var loginBtn: AppCompatButton
    private lateinit var loginGG: LinearLayout
    private lateinit var skip: LinearLayout
    private lateinit var register: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var ggSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView(){
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        ggSignInClient = GoogleSignIn.getClient(this,gso)
        loginBtn = findViewById(R.id.btnLogin)
        loginGG = findViewById(R.id.btnGoogle)
        skip = findViewById(R.id.layout_skip)
        register = findViewById(R.id.txtRegister)

        loginGG.setOnClickListener{
            signInWithGG()
        }
        loginBtn.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }

        skip.setOnClickListener {
            goHomeActivity("Skip")
            finish()
        }

        register.setOnClickListener{
            val i = Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }
    }

    private fun signInWithGG() {
        val signInIntent = ggSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        res ->
        if(res.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                goHomeActivity("GG")
                finish()
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goHomeActivity(type:String){
        val intent = Intent(this,HomeActivity::class.java)
        intent.putExtra("type",type)
        startActivity(intent)
    }

}