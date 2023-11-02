package com.ghivalhrvnsyah.storyappdicoding.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityWelcomeBinding
import com.ghivalhrvnsyah.storyappdicoding.view.login.LoginActivity
import com.ghivalhrvnsyah.storyappdicoding.view.main.MainActivity
import com.ghivalhrvnsyah.storyappdicoding.view.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        // Cek apakah pengguna sudah login sebelumnya
        if (viewModel.getSession().isLogin) {
            navigateToMainActivity()
        }
    }

    private fun playAnimation() {
        // Animasi pergeseran gambar
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Animasi tampilan elemen-elemen
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(100)

        // Animasi bersamaan
        val together = AnimatorSet().apply {
            playTogether(login, signup)
            start()
        }

        // Animasi secara berurutan
        AnimatorSet().apply {
            playSequentially(together, title, desc)
            start()
        }
    }

    private fun setupView() {
        // Sembunyikan StatusBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        // Tindakan saat tombol login diklik
        binding.loginButton.setOnClickListener {
            navigateToLoginActivity()
        }

        // Tindakan saat tombol signup diklik
        binding.signupButton.setOnClickListener {
            navigateToSignupActivity()
        }
    }

    // Navigasi ke halaman LoginActivity
    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // Navigasi ke halaman SignupActivity
    private fun navigateToSignupActivity() {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    // Navigasi ke halaman MainActivity
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Menutup Activity saat kembali ke MainActivity
    }
}
