package com.example.activitylauncher
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activitylauncher.Movies.Movies
import com.example.activitylauncher.databinding.ActivityMoviesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = GridLayoutManager(this, 2)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..100) {
            data.add(ItemsViewModel(R.drawable.baseline_folder_open_24, "Item $i"))
        }

        val apiInterface = ApiInterface.create().getMovies("2d69bf37526472a27328b8717eb158ea")

        apiInterface.enqueue( /* callback = */ object : Callback<Movies>, CustomAdapter.ItemClickListener {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {

                // This will pass the ArrayList to our Adapter
                val adapter = CustomAdapter(response.body()?.results, this)

                // Setting the Adapter with the recyclerview
                recyclerview.adapter = adapter

                Log.d("MyLog", "On Response Success $call ${response.body()?.results}")
            /*    if(response?.body() != null)
                    recyclerAdapter.setMovieListItems(response.body()!!)*/
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d("MyLog", "Response fail : ${t.message}")
            }

            override fun onItemClick(position: Int) {
               val intent = Intent(this@MoviesActivity, MoviesDetailsActivity::class.java)
                intent.putExtra("id", position)
                startActivity(intent)
            }
        })

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

}