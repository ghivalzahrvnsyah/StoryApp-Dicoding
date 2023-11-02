package com.ghivalhrvnsyah.storyappdicoding.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghivalhrvnsyah.storyappdicoding.LoadingStateAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        storyAdapter = StoryAdapter()

        val switchTheme = findViewById<SwitchMaterial>(R.id.switchTheme)

        // Mengamati pengaturan tema gelap/cerah
        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val themeMode = if (isDarkModeActive) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(themeMode)
            switchTheme.isChecked = isDarkModeActive
        }

        // Mengatur aksi ketika tombol switch tema digunakan
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        with(binding) {
            rvListStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvListStory.adapter = storyAdapter
        }

        // Mengatur tindakan ketika tombol Logout diklik
        binding.ivLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Mengatur tindakan ketika tombol Maps diklik
        binding.ivMaps.setOnClickListener {
            navigateToMapsActivity()
        }

        // Mengatur tindakan ketika tombol Tambah Cerita (Floating Action Button) diklik
        binding.fabAddStory.setOnClickListener {
            navigateToStoryUpload()
        }

        // Mengatur adapter dan mengamati data cerita
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                navigateToStoryDetail(data.name, data.photoUrl, data.description)
            }
        })

        // Mengamati data cerita dengan menggunakan Paging
        binding.rvListStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        viewModel.storyPage.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }

        storyAdapter.addLoadStateListener {
            binding.tvData.visibility = if (it.refresh !is LoadState.Loading && storyAdapter.itemCount <= 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        binding.swipeToRefresh.setOnRefreshListener {
            storyAdapter.refresh()
            binding.swipeToRefresh.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
        storyAdapter.refresh()
    }

    // Fungsi untuk menavigasi ke halaman unggah cerita
    private fun navigateToStoryUpload() {
        val intent = Intent(this@MainActivity, StoryUploadActivity::class.java)
        startActivity(intent)
    }

    // Fungsi untuk menavigasi ke halaman detail cerita dengan membawa data
    private fun navigateToStoryDetail(name: String?, url: String?, desc: String?) {
        val intent = Intent(this@MainActivity, StoryDetailActivity::class.java).apply {
            putExtra(StoryDetailActivity.EXTRA_TITLE, name)
            putExtra(StoryDetailActivity.EXTRA_URL, url)
            putExtra(StoryDetailActivity.EXTRA_DESCRIPTION, desc)
        }
        startActivity(intent)
    }

    // Fungsi untuk menampilkan dialog konfirmasi saat logout
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.alertLogoutMsg))
            .setMessage(getString(R.string.alertConfirmLogout))
            .setPositiveButton("Yes") { _, _ ->
                viewModel.logout()
                ViewModelFactory.resetInstance()
                navigateToWelcomeActivity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    // Fungsi untuk menavigasi ke halaman peta
    private fun navigateToMapsActivity() {
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    // Fungsi untuk menavigasi ke halaman sambutan (Welcome) setelah logout
    private fun navigateToWelcomeActivity() {
        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
