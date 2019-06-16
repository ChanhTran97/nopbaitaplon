package com.example.mainactivity.movie

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.bumptech.glide.Glide
import com.example.mainactivity.R
import kotlinx.android.synthetic.main.activity_details.*



import android.content.res.Resources
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast


class DetailsActivity : AppCompatActivity() {
    //var link = ""// global variable
    var res: Resources? = null// global variable

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_details)
        //toolbar
//        setSupportActionBar(findViewById(R.id.toolbar))
//        supportActionBar!!.title = "Details"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        var webview: WebView? = null
//        //show it through setcontentview()method
//        setContentView(webview)

        val data = intent.extras
        if (data != null) {
            val movie = data.getParcelable(MOVIE_MODEL_KEY) as MovieModel
//            Glide.with(this)
//                .load(movie.image_url)
//                .centerCrop()
//                .into(img_movie)
//            tv_title.text = movie.title
//            tv_overview.text = movie.ingredients

            help_webview.loadUrl(movie.source_url)
        }

    }


//    private fun getData() {
//        val data = intent.extras
//        val movie = data.getParcelable<MovieModel>(MOVIE_MODEL_KEY)
//
//        if (data != null) {
//            //tv_title.text = movie.title
//            //tv_overview.text = movie.overview
//            //tv_release_date.text = "Release date: " + movie.release_date
//
//            Glide.with(this)
//            .load("https://www.food2fork.com/api/search?key=98588d926fccf774934596f1fbef36b9&q=shredded%20chicken" + movie.image_url)
//            .centerCrop()
//            .override(800,1000)
//            .into(img_movie)
//
////            rating_bar.rating = movie.vote_average/2
////            if (movie.video)
////                img_play.visibility = View.VISIBLE
////            else
////                img_play.visibility = View.INVISIBLE
//        }
//    }
}
