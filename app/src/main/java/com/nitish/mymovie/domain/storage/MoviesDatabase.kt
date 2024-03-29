package com.nitish.mymovie.domain.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nitish.mymovie.domain.MoviesName

@Database(entities = [MoviesName::class], exportSchema = false, version = 1)
@TypeConverters(Converter::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        var moviesDatabase: MoviesDatabase? = null

        fun getDatabase(context: Context):MoviesDatabase{

            if (moviesDatabase == null){
                moviesDatabase = Room.databaseBuilder(context,
                    MoviesDatabase::class.java,
                    "MoviesDatabase").build()
            }

            return moviesDatabase!!
        }
    }
}