package com.ghivalhrvnsyah.storyappdicoding.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityMainBinding
import com.ghivalhrvnsyah.storyappdicoding.response.ErrorResponse
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import com.ghivalhrvnsyah.storyappdicoding.view.story.StoryAdapter
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter()

        with(binding) {
            rvListStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvListStory.adapter = storyAdapter

        }
        viewModel.stories.observe(this) { stories ->
            if (stories != null) {
                storyAdapter.setStories(stories as ArrayList<ListStoryItem>)
            }
        }
    }
}