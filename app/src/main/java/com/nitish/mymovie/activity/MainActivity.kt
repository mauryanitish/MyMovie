package com.nitish.mymovie.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.nitish.mymovie.R
import com.nitish.mymovie.databinding.ActivityMainBinding
import com.nitish.mymovie.fragment.DiscoverFragment
import com.nitish.mymovie.fragment.FavouriteFragment
import com.nitish.mymovie.fragment.ProfileFragment
import com.nitish.mymovie.fragment.Search

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomMenu.background = ColorDrawable(resources.getColor(android.R.color.transparent)) // Replace with your desired color


        binding.bottomMenu.setOnItemSelectedListener {
            val navController = Navigation.findNavController(this@MainActivity,R.id.fragmentViewContainer)
            when(it.itemId){
                R.id.optDiscover -> {
                    navController.popBackStack(navController.graph.startDestinationId, true)
                    navController.navigate(R.id.discoverFragment)
                }
                    R.id.optSearch ->{
                        navController.navigate(R.id.search)
                }
                R.id.optFavourite ->{
                    navController.navigate(R.id.favouriteFragment)
                }
                R.id.optProfile ->{
                    navController.navigate(R.id.profileFragment)
                }
                else ->Toast.makeText(this,"Invalid option",Toast.LENGTH_SHORT).show()
            }
            true
        }

    }
}