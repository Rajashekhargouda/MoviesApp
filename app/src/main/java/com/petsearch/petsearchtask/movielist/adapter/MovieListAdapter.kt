package com.petsearch.petsearchtask.movielist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.petsearch.petsearchtask.BuildConfig
import com.petsearch.petsearchtask.R
import com.petsearch.petsearchtask.Util.Util
import com.petsearch.petsearchtask.models.Movie
import com.squareup.picasso.Picasso
import java.lang.Exception

class MovieListAdapter(var list: List<Movie>,
                       private var onDetailImgClicked:(movieId:Int,movieTitle:String)->Unit):
        RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_movie_list,p0,false)
        return MovieViewHolder((view))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, pos: Int) {
        try {
            val item = list[pos]
            with(holder){
                txtMovieContent.text = item.overview
                txtMovieLang.text = item.original_language
                txtMovieRating.text = item.vote_average.toString()
                txtReleaseDate.text = Util.getDate(item.release_date)
                txtMovieTitle.text = item.title
                item.poster_path?.let {
                    Picasso.get().load(BuildConfig.IMAGE_URL + "w300" + it).into(imgPoster)
                }
                itemView.setOnClickListener {
                    onDetailImgClicked.invoke(item.id,item.title)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtMovieTitle = itemView.findViewById<TextView>(R.id.txt_movie_title)
        val txtMovieRating = itemView.findViewById<TextView>(R.id.txt_movie_rating)
        val txtMovieContent = itemView.findViewById<TextView>(R.id.txt_movie_overview)
        val txtReleaseDate = itemView.findViewById<TextView>(R.id.txt_release_date)
        val txtMovieLang = itemView.findViewById<TextView>(R.id.txt_language)
        val imgPoster = itemView.findViewById<ImageView>(R.id.img_movie_poster)
        val imgDetailBtn = itemView.findViewById<ImageView>(R.id.img_detail_button)

    }
}