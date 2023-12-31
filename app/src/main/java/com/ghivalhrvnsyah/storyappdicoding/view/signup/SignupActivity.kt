package com.ghivalhrvnsyah.storyappdicoding.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ghivalhrvnsyah.storyappdicoding.R
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivitySignupBinding
import com.ghivalhrvnsyah.storyappdicoding.response.ErrorResponse
import com.ghivalhrvnsyah.storyappdicoding.view.customView.MyButtonSignup
import com.ghivalhrvnsyah.storyappdicoding.view.customView.MyEditText
import com.ghivalhrvnsyah.storyappdicoding.view.customView.MyEditTextEmail
import com.ghivalhrvnsyah.storyappdicoding.view.login.LoginActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var myButtonSignUp: MyButtonSignup
    private lateinit var myEditText: MyEditText
    private lateinit var myEditTextEmail: MyEditTextEmail
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi komponen tampilan
        myButtonSignUp = findViewById(R.id.signupButton)
        myEditTextEmail = findViewById(R.id.ed_register_email)
        myEditText = findViewById(R.id.ed_register_password)

        // Mengatur status awal tombol signup
        setButtonEnable()

        // Menambahkan TextWatcher untuk memantau perubahan pada EditText
        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        // Mengatur tampilan dan menjalankan animasi
        setupView()
        setupAction()
        playAnimation()
    }

    // Mengatur status enable/disable tombol signup
    private fun setButtonEnable() {
        val result = myEditText.text
        myButtonSignUp.isEnabled = result != null && result.toString().isNotEmpty()
    }

    // Mengatur tampilan, termasuk menyembunyikan status bar
    private fun setupView() {
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

    // Melakukan proses registrasi
    private suspend fun register(name: String, email: String, password: String) {
        try {
            // Memanggil ViewModel untuk melakukan registrasi
            val message = viewModel.register(name, email, password)
            showLoading(false)
            showSuccessMessage()
        } catch (e: HttpException) {
            // Menangani kesalahan jika registrasi gagal
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            showLoading(false)
        }
    }

    // Menampilkan atau menyembunyikan indikator loading
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.signupButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.signupButton.visibility = View.VISIBLE
        }
    }

    // Mengatur tindakan saat tombol signup ditekan
    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            // Menampilkan indikator loading dan melakukan registrasi
            showLoading(true)
            lifecycleScope.launch {
                register(name, email, password)
            }
        }
    }

    // Menampilkan pesan sukses setelah registrasi berhasil
    private fun showSuccessMessage() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(getString(R.string.alertMessage))
            setPositiveButton(getString(R.string.continueMsg)) { _, _ ->
                // Navigasi ke halaman login setelah registrasi berhasil
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    // Menjalankan animasi
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}
