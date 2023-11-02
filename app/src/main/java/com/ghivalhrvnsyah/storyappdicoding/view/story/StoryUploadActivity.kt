package com.ghivalhrvnsyah.storyappdicoding.view.story

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ghivalhrvnsyah.storyappdicoding.R
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityStoryUploadBinding
import com.ghivalhrvnsyah.storyappdicoding.di.ResultState
import com.ghivalhrvnsyah.storyappdicoding.utils.getImageUri
import com.ghivalhrvnsyah.storyappdicoding.utils.reduceFileImage
import com.ghivalhrvnsyah.storyappdicoding.utils.uriToFile
import com.ghivalhrvnsyah.storyappdicoding.view.main.MainActivity
import com.ghivalhrvnsyah.storyappdicoding.view.main.MainViewModel

class StoryUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryUploadBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sembunyikan ActionBar
        supportActionBar?.hide()

        // Tambahkan OnClickListener untuk tombol galeri, kamera, dan unggah
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        binding.ivBackBtn.setOnClickListener { navigateToMainActivity() }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    // Fungsi untuk memulai pemilihan gambar dari galeri
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // Fungsi untuk memulai pengambilan gambar dari kamera
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    // Menampilkan gambar yang dipilih atau diambil di tampilan
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    // Fungsi untuk mengunggah gambar ke server
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.inputEditText.text.toString()

            viewModel.uploadImage(imageFile, description).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showToast(result.data.message)
                        showLoading(false)
                        navigateToStoryList()
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    // Navigasi ke halaman daftar cerita setelah mengunggah gambar
    private fun navigateToStoryList() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    // Menampilkan atau menyembunyikan indikator loading
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Menampilkan pesan toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
