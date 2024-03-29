package com.nitish.mymovie.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nitish.mymovie.domain.storage.Converter
import java.io.Serializable

//{
//            "id": 1,
//            "title": "The Shawshank Redemption",
//            "poster": "https://moviesapi.ir/images/tt0111161_poster.jpg",
//            "year": "1994",
//            "country": "USA",
//            "imdb_rating": "9.3",
//            "genres": [
//                "Crime",
//                "Drama"
//            ],
//            "images": [
//                "https://moviesapi.ir/images/tt0111161_screenshot1.jpg",
//                "https://moviesapi.ir/images/tt0111161_screenshot2.jpg",
//                "https://moviesapi.ir/images/tt0111161_screenshot3.jpg"
//            ]
//        },
@Entity(tableName = "movies")
data class MoviesName(
  @PrimaryKey(autoGenerate = false)
  val id:Int=0,
  val title:String?=null,
  val poster:String?=null,
  val year: String?=null,
  val country:String?=null,
  val imdb_rating:String?=null,
  @TypeConverters(Converter::class)
  val genres:List<String>?= arrayListOf(),
  @TypeConverters(Converter::class)
  val images:List<String>?= arrayListOf(),
)
