package com.petsearch.petsearchtask.moviedetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.petsearch.petsearchtask.BuildConfig
import com.petsearch.petsearchtask.R
import com.petsearch.petsearchtask.Util.Util
import com.petsearch.petsearchtask.models.MovieDetail
import com.petsearch.petsearchtask.mvvm.MovieViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieViewModel: MovieViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId:Int? = intent.getIntExtra("movieId",0)
        init()
        movieId?.let {
            getMovieDetails(it)
        }?:showError(getString(R.string.something_went_wrong))

    }

    private fun init(){
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("movieTitle")?:""
    }
    private fun getMovieDetails(movieId:Int){
        if (Util.isNetworkOnline(this)){
            setMovieDetailObserver()
            movieViewModel.getMovieDetail(Util.API_KEY,movieId)
        }else showError(getString(R.string.internet_is_not_connected_please_connect_to_internet_and_try_again))
    }

    private fun setMovieDetailObserver(){
        movieViewModel.movieDetailLiveData.observe(this, Observer {
            it?.let {
                when(it){
                    is MovieViewModel.MovieDetailResponse.Loading ->{
                        updateProgressBar(true)
                        constLt_detail.visibility = View.GONE
                        txt_error_text.visibility = View.GONE
                    }
                    is MovieViewModel.MovieDetailResponse.Success ->{
                        updateProgressBar(false)
                        initUI(it.movieDetail)
                        constLt_detail.visibility = View.VISIBLE
                        txt_error_text.visibility = View.GONE

                    }
                    is MovieViewModel.MovieDetailResponse.Error ->{
                        updateProgressBar(false)
                        showError(it.msg)
                        constLt_detail.visibility = View.GONE
                    }
                }
            }
        })

    }


    private fun initUI(movieDetail: MovieDetail){
        try {
            Picasso.get().load(BuildConfig.IMAGE_URL +"/w500" +
                    movieDetail.backdrop_path).into(img_backdrop_poster)
            txt_movie_duration.text = movieDetail.runtime?.toString() +" " +getString(R.string.minutes)
            txt_budget_val.text = getBudgetInMillions(movieDetail.budget)
            txt_revenue_val.text = getBudgetInMillions(movieDetail.revenue)
            txt_movie_lang.text = movieDetail.original_language
            txt_movie_release_date.text = Util.getDate(movieDetail.release_date)
            txt_movie_overview.text = movieDetail.overview
            txt_movie_rating.text = movieDetail.vote_average.toString()

            val genreList = ArrayList<String>()
            movieDetail.genres.forEach {
                genreList.add(it.name)
            }
            txt_movie_genres.text=android.text.TextUtils.join(", ",genreList)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

   private fun getBudgetInMillions(budget:Int):String{
       var budgetInDouble = 0.0
        if (budget>0){
            budgetInDouble = (budget/1000000).toDouble()
            return "$"+budgetInDouble.toString() + " "+getString(R.string.million)
        }else return  "$"+budget + " "+getString(R.string.million)
    }


    private fun updateProgressBar(shouldShow:Boolean){
        if (shouldShow)
            pb_movie_detail.visibility = View.VISIBLE
        else pb_movie_detail.visibility = View.GONE
    }

    private fun showError(errMsg:String){
        txt_error_text.text = errMsg
        txt_error_text.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId==android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


}
