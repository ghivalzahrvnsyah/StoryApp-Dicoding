package com.ghivalhrvnsyah.storyappdicoding.view.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ghivalhrvnsyah.storyappdicoding.R
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        getSupportActionBar()?.hide();
    }

private fun setupUI() {
    val title = intent.getStringExtra(EXTRA_TITLE)
    val id = intent.getIntExtra(EXTRA_ID, 0)
    val url = intent.getStringExtra(EXTRA_URL)
    val description = intent.getStringExtra(EXTRA_DESCRIPTION)
    val bundle = Bundle().apply {
        putString(EXTRA_TITLE, title)
    }

        with(binding) {
           Glide.with(this@StoryDetailActivity)
                .load(url)
                .into(ivStoryDetail)
            tvNameDetail.text = title
            tvDescriptionDetail.text = description
        }
    binding.ivBackBtn.setOnClickListener {
        onBackPressed()
    }
}
    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_DESCRIPTION = "extra_description"

    }
}