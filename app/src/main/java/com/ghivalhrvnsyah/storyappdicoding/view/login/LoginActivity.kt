package com.ghivalhrvnsyah.storyappdicoding.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.ghivalhrvnsyah.storyappdicoding.R
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.data.model.UserModel
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityLoginBinding
import com.ghivalhrvnsyah.storyappdicoding.response.ErrorResponse
import com.ghivalhrvnsyah.storyappdicoding.view.customView.MyButtonLogin
import com.ghivalhrvnsyah.storyappdicoding.view.customView.MyEditText
import com.ghivalhrvnsyah.storyappdicoding.view.customView.MyEditTextEmail
import com.ghivalhrvnsyah.storyappdicoding.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myButtonLogin: MyButtonLogin
    private lateinit var myEditText: MyEditText
    private lateinit var myEditTextEmail: MyEditTextEmail
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButtonLogin = findViewById(R.id.loginButton)
        myEditTextEmail = findViewById(R.id.emailEditText)
        myEditText = findViewById(R.id.passwordEditText)

        setButtonEnable()
        myEditTextEmail.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()

            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        myEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()

            }
            override fun afterTextChanged(s: Editable) {
            }
        })



        setupView()
        setupAction()
        playAnimation()
    }

    private fun setButtonEnable() {
        val result = myEditText.text
        myButtonLogin.isEnabled = result != null && result.toString().isNotEmpty() && myEditTextEmail.text.toString().isNotEmpty()
    }

    private suspend fun login(email: String, password: String) {
        try {
            //get success message
            val message  = viewModel.login(email, password)
            viewModel.saveUser(
               UserModel(email, message.loginResult?.token!!)
            )
            showLoading(false)
            ViewModelFactory.resetInstance()
            showSuccesMessage()
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            showLoading(false)
        }
    }

    private fun showSuccesMessage() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(getString(R.string.alertMsgLogin))
            setPositiveButton(getString(R.string.continueMsg)) { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            showLoading(true)
            lifecycleScope.launch {
                login(email, password)
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
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
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
        }
    }
}