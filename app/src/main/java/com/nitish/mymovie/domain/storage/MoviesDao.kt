package com.nitish.mymovie.domain.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nitish.mymovie.domain.MoviesName

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavouriteMovie(moviesName: MoviesName)

    @Delete
    fun deleteFavouriteMovie(moviesName: MoviesName)

    @Query("SELECT * FROM movies")
    fun getAllFavMovies():List<MoviesName>

    @Query("SELECT NOT EXISTS (SELECT 1 FROM movies WHERE id =:id)")
    fun getFavColorMovie(id:Int):Boolean
}