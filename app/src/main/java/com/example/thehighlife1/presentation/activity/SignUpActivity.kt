package com.example.thehighlife1.presentation.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.thehighlife1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.text.Editable
import android.widget.EditText
import com.example.thehighlife1.data.model.User
import com.example.thehighlife1.utils.Extensions.toast
import com.example.thehighlife1.presentation.activity.LoginActivity



class SignUpActivity : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var emailEt: EditText
    private lateinit var passEt: EditText
    private lateinit var CpassEt: EditText


    private lateinit var progressDialog: ProgressDialog
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDatabaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signUpBtn = findViewById<Button>(R.id.signupBtn)
        fullName = findViewById(R.id.etSignupFullName)
        emailEt = findViewById(R.id.etSignupEmail)
        passEt = findViewById(R.id.etSignupPassword)
        CpassEt = findViewById(R.id.etSignupConfirmPassword)
        val signInTv = findViewById<TextView>(R.id.tvSignuptoSignin)

        progressDialog = ProgressDialog(this)
        firebaseAuth = FirebaseAuth.getInstance()
        userDatabaseRef = FirebaseDatabase.getInstance().reference.child("Users")

        textAutoCheck()

        signInTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        signUpBtn.setOnClickListener {
            checkInput()
        }
    }



    private fun textAutoCheck() {
        fullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                fullName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count >= 4){
                    fullName.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }

            }

            override fun afterTextChanged(s: Editable) {
                if (fullName.text.isEmpty()){
                    fullName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (fullName.text.length >= 4){
                    fullName.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }

        })


            emailEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (emailEt.text.matches(emailPattern.toRegex())) {
                        emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                            R.drawable.ic_check
                        ), null)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    if (emailEt.text.isEmpty()){
                        emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                    }
                    else if (emailEt.text.matches(emailPattern.toRegex())) {
                        emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                            R.drawable.ic_check
                        ), null)
                    }
                }

            })


            passEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count > 5){
                        passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                            R.drawable.ic_check
                        ), null)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (passEt.text.isEmpty()){
                        passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                    }
                    else if (passEt.text.length > 5){
                        passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                            R.drawable.ic_check
                        ), null)
                    }
                }

            })

        CpassEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                CpassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (CpassEt.text.toString() == passEt.text.toString()){
                    CpassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (CpassEt.text.isEmpty()){
                    CpassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (CpassEt.text.toString() == passEt.text.toString()){
                    CpassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }

        })

    }

    private fun checkInput() {
        if (fullName.text.isEmpty()) {
            toast("Name can't be empty!")
            return
        }
        if (emailEt.text.isEmpty()) {
            toast("Email can't be empty!")
            return
        }
        if (!emailEt.text.matches(emailPattern.toRegex())) {
            toast("Enter a valid email")
            return
        }
        if (passEt.text.isEmpty()) {
            toast("Password can't be empty!")
            return
        }
        if (passEt.text.toString() != CpassEt.text.toString()) {
            toast("Passwords do not match")
            return
        }
        signIn()
    }

    private fun signIn() {
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account")
        progressDialog.show()

        val emailV: String = emailEt.text.toString()
        val passV: String = passEt.text.toString()
        val fullname: String = fullName.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(emailV, passV)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.setMessage("Save User Data")
                    val user = User(fullname, "", firebaseAuth.uid.toString(), emailV, "", "")
                    storeUserData(user)
                    // Navigate to LoginActivity after successful sign-up
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    progressDialog.dismiss()
                    toast("Failed to Authenticate: ${task.exception?.message}")
                }
            }
    }


    private fun storeUserData(user: User) {
        userDatabaseRef.child(firebaseAuth.uid.toString()).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Data Saved")
                    progressDialog.dismiss()
                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    progressDialog.dismiss()
                    toast("Failed to save data: ${task.exception?.message}")
                }
            }
    }



}