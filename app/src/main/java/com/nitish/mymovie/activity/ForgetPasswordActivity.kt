package com.nitish.mymovie.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nitish.mymovie.databinding.ActivityForgetPasswordBinding
import com.nitish.mymovie.utils.toastShow

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityForgetPasswordBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnReset.setOnClickListener {
            val email = binding.etForgegtEmail.text.toString().trim()
            if (email.isNotEmpty()){
                forgetPassword(email)
            }else{
                binding.etForgegtEmail.error = "Required email"
            }
        }

        binding.tvLoginNow.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

    private fun forgetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    toastShow("Check your email.")
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }else{
                    toastShow("error : "+it.exception?.message)
                }
            }
    }
}