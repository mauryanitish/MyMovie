package com.nitish.mymovie.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.nitish.mymovie.activity.DetailActivity
import com.nitish.mymovie.adapter.ClickInterface
import com.nitish.mymovie.adapter.FilmListAdapter
import com.nitish.mymovie.databinding.FragmentDiscoverBinding
import com.nitish.mymovie.domain.MoviesName
import com.nitish.mymovie.domain.MyApi
import com.nitish.mymovie.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class DiscoverFragment : Fragment(),ClickInterface {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = MainScope()
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var movieListArray: ArrayList<MoviesName>
    private lateinit var movieUpcomingListArray: ArrayList<MoviesName>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        if (isInternetAvailable(requireContext())) {
            coroutineScope.launch {
                Log.d("TAG", "IO run")
                val job1 = launch {
                    apiCall()
                }
                val job2 = launch {
                    apiCallAll()
                }
                joinAll(job1, job2)
            }
        }else{
            Toast.makeText(context,"You are offline..",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private suspend fun apiCall(){
        mainScope.launch{
            binding.progressBarNew.visibility = View.VISIBLE
        }
        try {
            val myApi = MyApi()
            val response = myApi.moviesDetail()
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("TAG", body.toString())
                if (body != null) {
                    movieListArray = body.data as ArrayList<MoviesName>
                    mainScope.launch {
                        setupRecyclerView()
                    }
                }
                Log.d("TAG", movieListArray.iterator().toString())
            } else {
                Log.d("TAG", response.errorBody().toString())
            }
        }catch (e:ApiException){
            mainScope.launch {
                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            mainScope.launch {
                Toast.makeText(context,"Check your internet connection..",Toast.LENGTH_SHORT).show()
            }
        }
        mainScope.launch {
        binding.progressBarNew.visibility = View.GONE
        }


    }

    private suspend fun apiCallAll(){
        mainScope.launch {
            binding.progressBarUpcoming.visibility = View.VISIBLE
        }
        try {
            val myApi = MyApi()
            val response = myApi.allMoviesDetail()
            if (response.isSuccessful) {
                val allBody = response.body()
                Log.d("T", allBody.toString())
                if (allBody != null) {
                    movieUpcomingListArray = allBody.data as ArrayList<MoviesName>
                    mainScope.launch {
                        setUpUpcomingRecyclerView()
                    }
                }
            } else {
                Log.d("T", response.errorBody().toString())
            }
        }catch (e:ApiException){
            mainScope.launch {
                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            mainScope.launch {
                Toast.makeText(context,"Check your internet connection..",Toast.LENGTH_SHORT).show()
            }
        }
        mainScope.launch {
            binding.progressBarUpcoming.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(){
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        Log.d("Tag","layout manager running")
        binding.recyclerViewNew.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        Log.d("Tag","return layout manager")
        val adapter = FilmListAdapter(this,movieListArray)
        Log.d("Tag","adapter is created")
        binding.recyclerViewNew.adapter = adapter
        Log.d("Tag","add adapter")
    }

    private fun setUpUpcomingRecyclerView(){
        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.HORIZONTAL,false)
        val adapter = FilmListAdapter(this,movieUpcomingListArray)
        binding.recyclerViewUpcoming.adapter = adapter
    }

    override fun clickItem(position:Int) {
        Intent(this.context, DetailActivity::class.java).also {
            Log.d("TAG",position.toString())
            it.putExtra("movieID",position.toString())
            startActivity(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
        mainScope.cancel()
    }
}