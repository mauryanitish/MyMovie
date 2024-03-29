package com.nitish.mymovie.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nitish.mymovie.databinding.ActivityLoginBinding
import com.nitish.mymovie.utils.isInternetAvailable
import com.nitish.mymovie.utils.toastShow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class LoginActivity : AppCompatActivity() {
    private var changeScreenValue = 1
    private var login = true
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth:FirebaseAuth


    override fun onStart() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val verify = auth.currentUser?.isEmailVerified
            if (verify == true) {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
        }
        super.onStart()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            val checkPassword = binding.etPasswordRegister.text.toString()
if (isInternetAvailable(this)) {
    if (email.isEmpty()) {
        binding.etLoginEmail.error = "please enter the email"
    } else if (password.isEmpty()) {
        binding.etPasswordLogin.error = "please enter the password"
    }
    if(password.length<8) {
        binding.etPasswordLogin.error = "Password lessthan 8 letters"
    }else if (!login) {
        if (password != checkPassword) {
            binding.etPasswordRegister.error = "Password are not match !"
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                val verify = auth.currentUser?.isEmailVerified
                                // Sign in success, update UI with the signed-in user's information
                                if (verify == true) {
                                    Log.d("TAG", "createUserWithEmail:success")
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "SignIn Successfully.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    ).also {
                                        startActivity(it)
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Verify the email..",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@LoginActivity,
                            "Email is already exist...",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

    } else {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val verify = auth.currentUser?.isEmailVerified
                    // Sign in success, update UI with the signed-in user's information
                    if (verify == true) {
                        Log.d("TAG", "signInWithEmail:success")
                        Toast.makeText(
                            this@LoginActivity,
                            "SignIn Successfully.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Intent(this@LoginActivity, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Please verify the email..", Toast.LENGTH_SHORT).show()
                    }
                    auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@LoginActivity,
                        "Email or password are wrong..",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}else{
    toastShow("You are offline..")
}
        }


        binding.tvRegisterNow.setOnClickListener {
            if (changeScreenValue==1) {
                binding.tvLogin.text = "Register"
                binding.btnLogin.text = "Register"
                binding.tvForget.visibility = View.GONE
                binding.etPasswordRegister.visibility = View.VISIBLE
                login = false
                binding.tvRegisterNow.text = "Login Here"
                binding.tvDoNot.text = "Account have already"
                changeScreenValue = 0
            }else{
                binding.btnLogin.text = "Login"
                binding.tvLogin.text = "Login"
                binding.tvForget.visibility = View.VISIBLE
                binding.etPasswordRegister.visibility = View.GONE
                binding.tvRegisterNow.text = "Register Here"
                binding.tvDoNot.text = "Don't have an account?"
                changeScreenValue = 1
                login = true
            }
        }

        binding.tvForget.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordActivity::class.java))
        }

    }

}