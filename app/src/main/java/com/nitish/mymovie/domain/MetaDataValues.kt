package com.nitish.mymovie.domain

import java.io.Serializable

data class MetaDataValues(
val current_page: String,
val per_page: Int,
val page_count: Int,
val total_count: Int

):Serializable
