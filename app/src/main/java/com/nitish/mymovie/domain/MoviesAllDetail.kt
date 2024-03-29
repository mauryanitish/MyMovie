package com.nitish.mymovie.domain

import com.google.gson.annotations.SerializedName

//"id": 11,
//    "title": "The Lord of the Rings: The Fellowship of the Ring",
//    "poster": "https://moviesapi.ir/images/tt0120737_poster.jpg",
//    "year": "2001",
//    "rated": "PG-13",
//    "released": "19 Dec 2001",
//    "runtime": "178 min",
//    "director": "Peter Jackson",
//    "writer": "J.R.R. Tolkien (novel), Fran Walsh (screenplay), Philippa Boyens (screenplay), Peter Jackson (screenplay)",
//    "actors": "Alan Howard, Noel Appleby, Sean Astin, Sala Baker",
//    "plot": "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle Earth from the Dark Lord Sauron.",
//    "country": "New Zealand, USA",
//    "awards": "Won 4 Oscars. Another 108 wins & 122 nominations.",
//    "metascore": "92",
//    "imdb_rating": "8.8",
//    "imdb_votes": "1,269,564",
//    "imdb_id": "tt0120737",
//    "type": "movie",
//    "genres": [
//        "Adventure",
//        "Drama",
//        "Fantasy"
//    ],
//    "images": [
//        "https://moviesapi.ir/images/tt0120737_screenshot1.jpg",
//        "https://moviesapi.ir/images/tt0120737_screenshot2.jpg",
//        "https://moviesapi.ir/images/tt0120737_screenshot3.jpg"
//    ]
//}
data class MoviesAllDetail(
    @SerializedName("id"          ) var id         : Int ?= null,
    @SerializedName("title"       ) var title      : String ?= null,
    @SerializedName("poster"      ) var poster     : String ?= null,
    @SerializedName("year"        ) var year       : String ?= null,
    @SerializedName("rated"       ) var rated      : String ?= null,
    @SerializedName("released"    ) var released   : String ?= null,
    @SerializedName("runtime"     ) var runtime    : String ?= null,
    @SerializedName("director"    ) var director   : String ?= null,
    @SerializedName("writer"      ) var writer     : String ?= null,
    @SerializedName("actors"      ) var actors     : String ?= null,
    @SerializedName("plot"        ) var plot       : String ?= null,
    @SerializedName("country"     ) var country    : String ?= null,
    @SerializedName("awards"      ) var awards     : String ?= null,
    @SerializedName("metascore"   ) var metascore  : String ?= null,
    @SerializedName("imdb_rating" ) var imdbRating : String ?= null,
    @SerializedName("imdb_votes"  ) var imdbVotes  : String ?= null,
    @SerializedName("imdb_id"     ) var imdbId     : String ?= null,
    @SerializedName("type"        ) var type       : String ?= null,
    @SerializedName("genres"      ) var genres     : ArrayList<String> ?= arrayListOf(),
    @SerializedName("images"      ) var images     : ArrayList<String> ?= arrayListOf()
)
