package com.nitish.mymovie.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.nitish.mymovie.R
import com.nitish.mymovie.domain.MoviesAllDetail
import com.squareup.picasso.Picasso

class FilmDetailImg(private val imgList:List<String>):RecyclerView.Adapter<FilmDetailImg.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_detail_image,parent,false))
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(Uri.parse(imgList[position]))
            .into(holder.img)
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ShapeableImageView>(R.id.itemImg)
    }
}