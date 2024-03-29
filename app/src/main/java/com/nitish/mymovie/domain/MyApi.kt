package com.nitish.mymovie.domain

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {
    var ID :String
//    https://moviesapi.ir/api/v1/movies?page=1
    @GET("movies?page=1")
    suspend fun moviesDetail():Response<MoviesStore>

    @GET("movies/{id}")
    suspend fun doGetUserList(@Path("id") id: String?): Response<MoviesAllDetail>

    //    suspend fun doGetUserList():Response<MoviesAllDetail>

@GET("movies?page=11")
    suspend fun allMoviesDetail():Response<MoviesStore>

    //    suspend fun movieId(id:Int){
//        ID="movies/$ID"
//    }

    companion object{
        operator fun invoke(): MyApi{
            val okkHttpclient = OkHttpClient.Builder()
                .build()
            Log.d("TAG","build run")
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://moviesapi.ir/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(MyApi::class.java)
        }

    }

}