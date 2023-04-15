package com.example.activitylauncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.activitylauncher.MoviesDetails.MoviesDetails
import com.example.activitylauncher.databinding.ActivityMoviesDetailsBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesDetailsBinding

    private lateinit var title: TextView
    private lateinit var releaseDate : TextView
    private lateinit var score : TextView
    private lateinit var owerview : TextView
    private lateinit var banner : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = binding.movieDetailsTitle
        releaseDate = binding.movieDetailsDate
        score = binding.movieDetailsScore
        owerview = binding.movieDetailsBodyOverview
        banner = binding.movieDetailsImageBanner


        val id = intent.getIntExtra("id", 0)
        Log.d("MyLog", "id $id")


        val apiInterface = id.let { ApiInterface.create().getMovieDetails(it, "2d69bf37526472a27328b8717eb158ea") }

        apiInterface.enqueue(object : Callback<MoviesDetails> {
            override fun onResponse(call: Call<MoviesDetails>?, response: Response<MoviesDetails>?) {
                title.text = response?.body()?.title
                releaseDate.text = response?.body()?.release_date
                score.text = response?.body()?.vote_average.toString()
                owerview.text = response?.body()?.overview

                Picasso.get().load("https://image.tmdb.org/t/p/w500" + response?.body()?.backdrop_path).into(
                    this@MoviesDetailsActivity.banner
                )
            }

            override fun onFailure(call: Call<MoviesDetails>, t: Throwable) {
                Log.d("MyLog", "onFailure : ${t.message} ")
            }

        })

    }
}