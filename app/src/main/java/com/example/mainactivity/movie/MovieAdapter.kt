package com.example.mainactivity.movie

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mainactivity.R
import kotlinx.android.synthetic.main.row_now_playing.view.*
import java.util.*

class MovieAdapter (var items: ArrayList<Movie>, val context: Context): RecyclerView.Adapter<MovieViewHolder>() {
    lateinit var mListener: MovieItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_now_playing,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(movieViewHolder: MovieViewHolder, position: Int) {
        movieViewHolder.tvTitle.text = items[position].title
        Glide.with(context)
            .load(items[position].image_url)
            .centerCrop()
            .override(600,600)
            .into(movieViewHolder.imgView)


        movieViewHolder.itemView.setOnClickListener{
            mListener.onItemCLicked(position)
        }

        movieViewHolder.itemView.setOnLongClickListener {
            mListener.onItemLongCLicked(position)
            true
        }
    }

    fun setListener(listener: MovieItemClickListener) {
        this.mListener = listener
    }

    fun setData(items: ArrayList<Movie>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class MovieViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var tvTitle = view.tv_title_film
    var imgView = view.img_film
    var tvDetail = view.tv_detail_film
}