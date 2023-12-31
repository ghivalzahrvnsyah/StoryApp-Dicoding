package com.ghivalhrvnsyah.storyappdicoding.view.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        supportActionBar?.hide()
    }

    private fun setupUI() {
        // Mengambil data tambahan dari intent
        val title = intent.getStringExtra(EXTRA_TITLE)
        val url = intent.getStringExtra(EXTRA_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        // Menampilkan data pada tampilan
        with(binding) {
            Glide.with(this@StoryDetailActivity)
                .load(url)
                .into(ivStoryDetail)
            tvNameDetail.text = title
            tvDescriptionDetail.text = description
        }

        // Menambahkan fungsi untuk tombol kembali
        binding.ivBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}
