package com.example.mainactivity.movie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.mainactivity.R
import com.example.mainactivity.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

@SuppressLint("SetTextI18n")

class MainActivity : AppCompatActivity(){
    lateinit var mListener: OnDataToFragmentListener
    lateinit var mListenerTopRate: OnDataToFragmentListener

    var movies: ArrayList<Movie> = ArrayList()
    var moviesTopRate: ArrayList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""


        getDataTopRate()

        addFirstFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun addFirstFragment() {
        val nowPlayingFragment = TopRateFragment()
        openFragment(nowPlayingFragment)
        mListener = nowPlayingFragment
        mListener.sendDataToFragment(movies)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.man,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.logout->{
                FirebaseAuth.getInstance().signOut()
                val intentStart = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intentStart)
                return true

            }
        }
        return false
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.top_rate -> {
                val topRateFragment = TopRateFragment()
                mListenerTopRate = topRateFragment
                mListenerTopRate.sendDataToFragment(moviesTopRate)
                openFragment(topRateFragment)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun getDataTopRate() {
        pbLoading.visibility = View.VISIBLE
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://www.food2fork.com/api/search?key=98588d926fccf774934596f1fbef36b9&q=shredded%20chicken")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        pbLoading.visibility = View.GONE
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()!!.string()
                    val jsonObject = JSONObject(json)
                    val jsonDataMovie = jsonObject.get("recipes").toString()


                    val collectionType = object : TypeToken<Collection<Movie>>(){}.type

                    moviesTopRate = Gson().fromJson(jsonDataMovie, collectionType)

                    runOnUiThread {
                        pbLoading.visibility = View.GONE
                    }
                }
            })
    }
}

interface OnDataToFragmentListener {
    fun sendDataToFragment(movies: ArrayList<Movie>)
}