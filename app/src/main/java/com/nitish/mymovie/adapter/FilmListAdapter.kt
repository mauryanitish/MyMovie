package com.nitish.mymovie.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nitish.mymovie.R
import com.nitish.mymovie.domain.MoviesName
import com.nitish.mymovie.domain.storage.MoviesTable
import com.nitish.mymovie.fragment.FavouriteFragment
import com.squareup.picasso.Picasso
import java.net.URI
import java.net.URL

class FilmListAdapter(private val clickInterface:ClickInterface, movieArray: List<MoviesName>) : RecyclerView.Adapter<FilmListAdapter.ViewHolder>() {

    private var movieLisArray = movieArray

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_film,parent,false)

        return ViewHolder(view)
   }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=movieLisArray[position].title
        holder.scoreText.text = movieLisArray[position].imdb_rating

        Picasso.get()
            .load(Uri.parse(movieLisArray[position].poster))
            .into(holder.pic)

        holder.itemView.setOnClickListener {
            clickInterface.clickItem(movieLisArray[position].id)
        }
    }

    override fun getItemCount(): Int {
        return movieLisArray.size
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val pic = itemView.findViewById<ImageView>(R.id.pic)
        val title = itemView.findViewById<TextView>(R.id.titleTxt)
        val scoreText = itemView.findViewById<TextView>(R.id.scoreTxt)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredArray(filteredArray:ArrayList<MoviesName>){
        this.movieLisArray = filteredArray
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFavArray(favArray:ArrayList<MoviesName>){
        this.movieLisArray = favArray
        notifyDataSetChanged()
    }
}