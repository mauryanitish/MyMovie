package com.nitish.mymovie.domain

import com.google.gson.annotations.SerializedName

data class MoviesStore(
  @SerializedName("data") val data:List<MoviesName>?,
  @SerializedName("metadata") val metadata: MetaDataValues?
)
