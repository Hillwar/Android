package com.hillwar.retrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import com.google.android.material.snackbar.Snackbar
import com.hillwar.retrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var instance: MainActivity
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.coroutineScope.launch {
            binding.progressBar.isVisible = true
            binding.add.isVisible = false
            val data = async {
                try {
                    MyApp.instance.service.getListOfPosts()
                } catch (e: Exception) {
                    if (MyApp.instance.list == null) error(applicationContext)
                    arrayListOf(Post("body", 0, "title", 1))
                }
            }
            if (MyApp.instance.list == null) MyApp.instance.list = data.await()
            adapter = CustomAdapter(MyApp.instance.list as ArrayList<Post>)
            binding.recycler.adapter = adapter
            binding.progressBar.isVisible = false
            binding.add.isVisible = true
        }

        binding.add.setOnClickListener {
            lifecycle.coroutineScope.launch {
                val response =
                    withContext(Dispatchers.Default) {
                        try {
                            MyApp.instance.service.createPost(Post("body", 0, "title", 1))
                        } catch (e: Exception) {
                            error(applicationContext)
                            null
                        }
                    }
                if (response != null) Snackbar.make(
                    binding.root, (if (response.isSuccessful) "Post is successful: "
                    else "Post failed: ") + response.code(), Snackbar.LENGTH_LONG
                ).show()
                if (response != null && response.isSuccessful) {
                    adapter.add(Post("body", 0, "title", 1))
                }
            }
        }
    }
}