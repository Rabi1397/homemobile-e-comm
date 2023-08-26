package com.example.thehighlife1.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.thehighlife1.R
import com.example.thehighlife1.presentation.LoadingDialog
import com.example.thehighlife1.utils.Extensions.toast
import com.example.thehighlife1.utils.FirebaseUtils.firebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInBtn: Button
    lateinit var emailEt: EditText
    lateinit var passEt: EditText
    lateinit var loadingDialog: LoadingDialog

    lateinit var emailError: TextView
    lateinit var passwordError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signupTv = findViewById<TextView>(R.id.tvLoginSignup)
            signInBtn = findViewById(R.id.loginBtn)
            emailEt = findViewById(R.id.etEmail)
            passEt = findViewById(R.id.etPassword)
            emailError = findViewById(R.id.emailError)
            passwordError = findViewById(R.id.passwordError)


        textAutoCheck()

        loadingDialog = LoadingDialog(this)

        signupTv.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener {
            checkInput()
        }
    }



    private fun textAutoCheck() {
    emailEt.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (Patterns.EMAIL_ADDRESS.matcher(emailEt.text).matches()) {
                emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_check
                ), null)
                emailError.visibility = View.GONE
            }
        }

        override fun afterTextChanged(s: Editable) {
            if (emailEt.text.isEmpty()){
                emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            }
            else if (Patterns.EMAIL_ADDRESS.matcher(emailEt.text).matches()) {
                emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_check
                ), null)
                emailError.visibility = View.GONE
            }
        }
    })

        passEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                passwordError.visibility = View.GONE
                if (count > 4){
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)

                }
            }

            override fun afterTextChanged(s: Editable) {
                if (passEt.text.isEmpty()){
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (passEt.text.length > 4){
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)

                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkInput() {
        if (emailEt.text.isEmpty()){
            emailError.visibility = View.VISIBLE
            emailError.text = "Email Can't be Empty"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.text).matches()) {
            emailError.visibility = View.VISIBLE
            emailError.text = "Enter Valid Email"
            return
        }
        if(passEt.text.isEmpty()){
            passwordError.visibility = View.VISIBLE
            passwordError.text = "Password Can't be Empty"
            return
        }

        if ( passEt.text.isNotEmpty() && emailEt.text.isNotEmpty()){
            emailError.visibility = View.GONE
            passwordError.visibility = View.GONE
            signInUser()
        }
    }

    private fun signInUser() {
        loadingDialog.startLoadingDialog()
        signInEmail = emailEt.text.toString().trim()
        signInPassword = passEt.text.toString()

        firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
            .addOnCompleteListener { signIn ->
                if (signIn.isSuccessful) {
                    loadingDialog.dismissDialog()
                    // Navigate to HomeActivity after successful login
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    toast("Signed in successfully")
                    finish()
                } else {
                    toast("Sign in failed")
                    loadingDialog.dismissDialog()
                }
            }
    }

}