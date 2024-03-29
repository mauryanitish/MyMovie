package com.nitish.mymovie.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.nitish.mymovie.activity.DetailActivity
import com.nitish.mymovie.adapter.ClickInterface
import com.nitish.mymovie.adapter.FilmListAdapter
import com.nitish.mymovie.databinding.FragmentFavouriteBinding
import com.nitish.mymovie.domain.MoviesName
import com.nitish.mymovie.domain.storage.MoviesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment(),ClickInterface {
    private lateinit var binding:FragmentFavouriteBinding
    lateinit var arrayFavMovie:ArrayList<MoviesName>
    lateinit var moviesDatabase: MoviesDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moviesDatabase = MoviesDatabase.getDatabase(requireActivity())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            arrayFavMovie = moviesDatabase.moviesDao().getAllFavMovies() as ArrayList<MoviesName>
            MainScope().launch {
                setFavRecycler()
            }
        }
        val detailActivity = DetailActivity()



        return binding.root
    }

//    suspend fun apiCall(){
//        val myApi=MyApi()
//        val arrayList:ArrayList<MoviesName>
//        val response = myApi.moviesDetail()
//        if (response.isSuccessful){
//            val body = response.body()
//            Log.d("Favo",response.body().toString())
//            if (body!=null) {
//                arrayList = body.data as ArrayList<MoviesName>
//                MainScope().launch {
//                    setFavRecycler()
//                }
//            }
//            else{
//                Log.d("Favo",response.errorBody().toString())
//            }
//        }
//
//    }

    private fun setFavRecycler(){
        binding.recyclerFavView.layoutManager = GridLayoutManager(this.context,3,GridLayoutManager.VERTICAL,false)
        val adapter = FilmListAdapter(this,arrayFavMovie)
        binding.recyclerFavView.adapter = adapter
    }

    override fun clickItem(position: Int) {
        Intent(this.context, DetailActivity::class.java).also {
            Log.d("TAG", position.toString())
            it.putExtra("movieID", position.toString())
            startActivity(it)
        }
    }

}