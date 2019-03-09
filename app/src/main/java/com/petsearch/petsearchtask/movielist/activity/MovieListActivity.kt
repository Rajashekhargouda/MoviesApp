package com.petsearch.petsearchtask.movielist.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.petsearch.petsearchtask.R
import com.petsearch.petsearchtask.Util.Util
import com.petsearch.petsearchtask.models.Movie
import com.petsearch.petsearchtask.moviedetail.MovieDetailActivity
import com.petsearch.petsearchtask.mvvm.MovieViewModel
import com.petsearch.petsearchtask.movielist.adapter.MovieListAdapter
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieListActivity : AppCompatActivity() {

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var movieListViewModel: MovieViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        init()
        getMovies()
    }

    private fun getMovies(){
        if (Util.isNetworkOnline(this)){
            observeMovieList()
            movieListViewModel.getMovies(Util.API_KEY, 1, "popularity.desc")
        }else showError(getString(R.string.internet_is_not_connected_please_connect_to_internet_and_try_again))


    }


    private fun init(){
        movieListViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.movie_list_title)
    }

    private fun observeMovieList(){
        movieListViewModel.movieListLiveData.observe(this, Observer {
            it?.let {
                when(it){
                    is MovieViewModel.MovieListResponse.Loading -> {
                        updateProgressBar(true)
                        txt_error_text.visibility = View.GONE
                    }
                    is MovieViewModel.MovieListResponse.Success ->{
                        txt_error_text.visibility = View.GONE
                        updateProgressBar(false)
                        updateUI(it.movieList)


                    }
                    is MovieViewModel.MovieListResponse.Error ->{
                        updateProgressBar(false)
                        showError(it.msg)
                    }
                }
            }
        })
    }

    private fun updateUI(movieList:List<Movie>){
        movieListAdapter = MovieListAdapter(movieList,{movieId:Int, movieTitle:String ->  onDetailImageClicked(movieId,movieTitle) })
        recycler_movie_list.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recycler_movie_list.adapter = movieListAdapter
    }


    private fun onDetailImageClicked(movieId:Int,movieTitle:String){
        val intent = Intent(this,MovieDetailActivity::class.java).apply {
            putExtra("movieId",movieId)
            putExtra("movieTitle",movieTitle)
        }
        startActivity(intent)
    }

   private fun showError(errMsg:String){
       txt_error_text.text = errMsg
       txt_error_text.visibility = View.VISIBLE
    }

    private fun updateProgressBar(shouldShow:Boolean){
        if (shouldShow)
            pb_movie_list.visibility = View.VISIBLE
        else pb_movie_list.visibility = View.GONE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_movie_list,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId==android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }
}
