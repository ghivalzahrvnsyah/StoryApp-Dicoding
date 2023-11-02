package com.ghivalhrvnsyah.storyappdicoding.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghivalhrvnsyah.storyappdicoding.R
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityMainBinding
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import com.ghivalhrvnsyah.storyappdicoding.view.maps.MapsActivity
import com.ghivalhrvnsyah.storyappdicoding.view.story.StoryAdapter
import com.ghivalhrvnsyah.storyappdicoding.view.story.StoryDetailActivity
import com.ghivalhrvnsyah.storyappdicoding.view.story.StoryUploadActivity
import com.ghivalhrvnsyah.storyappdicoding.view.welcome.WelcomeActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        storyAdapter = StoryAdapter()

        val switchTheme = findViewById<SwitchMaterial>(R.id.switchTheme)

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false

            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }



        with(binding) {
            rvListStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvListStory.adapter = storyAdapter

        }
        binding.ivLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.alertLogoutMsg))
                .setMessage(getString(R.string.alertConfirmLogout))
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
        binding.ivMaps.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }
        binding.fabAddStory.setOnClickListener {
            navigateToStoryUpload()
        }
        viewModel.stories.observe(this) { stories ->
            if (stories != null) {
                storyAdapter.setStories(stories as ArrayList<ListStoryItem>)
            }
        }


        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                navigateToStoryDetail(data.name, data.photoUrl, data.description)
            }
        })
    }


    private fun navigateToStoryUpload() {
        val intent = Intent(this@MainActivity, StoryUploadActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToStoryDetail(name: String?, url: String?, desc: String?) {
        val intent = Intent(this@MainActivity, StoryDetailActivity::class.java).apply {
            putExtra(StoryDetailActivity.EXTRA_TITLE, name)
            putExtra(StoryDetailActivity.EXTRA_URL, url)
            putExtra(StoryDetailActivity.EXTRA_DESCRIPTION, desc)
        }
        startActivity(intent)
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.getStories()
//    }


}