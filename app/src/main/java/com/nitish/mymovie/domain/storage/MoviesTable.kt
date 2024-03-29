package com.nitish.mymovie.domain.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MoviesTable")
data class MoviesTable(
    @PrimaryKey(autoGenerate = false)var id:Int?=null,

    var movieId:String?=null,
    var title:String?=null,
    var poster:String?=null,
    var rating:String?=null
)
