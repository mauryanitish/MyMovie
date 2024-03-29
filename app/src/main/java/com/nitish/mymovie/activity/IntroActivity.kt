package com.nitish.mymovie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.nitish.mymovie.R
import com.nitish.mymovie.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        },3000)

//        binding.getIn.setOnClickListener {
//            Intent(this,LoginActivity::class.java).also {
//                startActivity(it)
//            }
//        }
    }
}