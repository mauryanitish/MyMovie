package com.nitish.mymovie.activity

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.nitish.mymovie.adapter.FilmDetailImg
import com.nitish.mymovie.databinding.ActivityDetailBinding
import com.nitish.mymovie.domain.MoviesAllDetail
import com.nitish.mymovie.domain.MoviesName
import com.nitish.mymovie.domain.MyApi
import com.nitish.mymovie.domain.storage.MoviesDatabase
import com.nitish.mymovie.utils.isInternetAvailable
import com.nitish.mymovie.utils.toastShow
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(){
    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = MainScope()
    private var FAVOURITE_ID = true
    private lateinit var binding: ActivityDetailBinding
    private lateinit var movieDetail:MoviesAllDetail
    private lateinit var moviesDatabase: MoviesDatabase
    private lateinit var idMovie:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMovie= intent.getStringExtra("movieID").toString()

        coroutineScope.launch{
            moviesDatabase = MoviesDatabase.getDatabase(this@DetailActivity)
                FAVOURITE_ID = moviesDatabase.moviesDao().getFavColorMovie(idMovie.toInt())
            Log.d("DEL",idMovie.toString()+FAVOURITE_ID.toString())
        }

        Log.d("DEL",idMovie.toString())
        if (isInternetAvailable(applicationContext)) {
            coroutineScope.launch {
                callDetailMovie(idMovie)
            }
        }else{
            toastShow("You are offline..")
        }

        binding.imgBack.setOnClickListener { onBackPressed() }
        binding.imgFavourite.setOnClickListener {
            if (isInternetAvailable(this)) {
                if (FAVOURITE_ID) {
                    setFavColor(Color.RED)
                    coroutineScope.launch {
                        moviesDatabase.moviesDao().addFavouriteMovie(
                            MoviesName(
                                id = idMovie.toInt(),
                                title = movieDetail.title,
                                poster = movieDetail.poster,
                                imdb_rating = movieDetail.imdbRating
                            )
                        )
                    }
                    FAVOURITE_ID = false
                } else {
                    setFavColor(Color.TRANSPARENT)
                    coroutineScope.launch {
                        moviesDatabase.moviesDao().deleteFavouriteMovie(
                            MoviesName(
                                id = idMovie.toInt(),
                                title = movieDetail.title,
                                poster = movieDetail.poster,
                                imdb_rating = movieDetail.imdbRating
                            )
                        )
                    }
                    FAVOURITE_ID = true
                }
            }
            else{
                toastShow("You are offline..")
            }
        }
    }

    private fun setFavColor(color: Int) {
        binding.imgFavourite.setColorFilter(color)
    }


    private suspend fun callDetailMovie(idMovie:String){
        mainScope.launch{
            binding.detailProgress.visibility = View.VISIBLE
        }

        try {
            val myApi = MyApi()
            Log.d("DET", "start running")
            val response = myApi.doGetUserList(id = idMovie)
            Log.d("DET", "return result")
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("DET", body.toString())
                if (body != null) {
                    movieDetail = body
                }
            }
        }catch (e: ApiException){
            mainScope.launch {
toastShow(e.toString())            }
        }catch (e:Exception){
            mainScope.launch {
toastShow("Check your internet connection..")            }
        }finally {
            mainScope.launch {
                setAttributes()
                binding.detailProgress.visibility = View.GONE
            }
        }
        }

    private fun setAttributes(){
        if (FAVOURITE_ID){
            setFavColor(Color.TRANSPARENT)
        }else{
            setFavColor(Color.RED)
        }
        movieDetail.poster?.let { posterUrl ->
            loadImg(posterUrl, binding.posterBigImg)
            loadImg(posterUrl, binding.posterNormalImg)
        }
        binding.tvMovieName.text= movieDetail.title
        binding.tvMoviesSummaryInfo.text= movieDetail.plot
        binding.tvMovieRating.text= movieDetail.imdbRating
        binding.tvMovieDate.text= movieDetail.released
        binding.tvMovieActionInfo.text=movieDetail.actors
        binding.tvMovieTime.text = movieDetail.runtime
        imageRecycler()

    }

    private fun imageRecycler() {
        binding.imageRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val adapter = movieDetail.images?.let {
            FilmDetailImg(it)
        }
        binding.imageRecyclerView.adapter = adapter
    }

    private fun loadImg(poster:String, id:ImageView){
        Picasso.get()
            .load(Uri.parse(poster))
            .into(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        mainScope.cancel()
    }
}