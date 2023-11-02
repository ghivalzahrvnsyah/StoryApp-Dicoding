package com.ghivalhrvnsyah.storyappdicoding.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import java.io.IOException

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

        supportActionBar?.hide()

        initializeViews()
        setupTextWatchers()
        setupAction()
        playEntranceAnimation()
    }

    private fun initializeViews() {
        myButtonLogin = findViewById(R.id.loginButton)
        myEditTextEmail = findViewById(R.id.emailEditText)
        myEditText = findViewById(R.id.passwordEditText)

        setButtonEnable()
    }

    private fun setupTextWatchers() {
        myEditTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setButtonEnable() {
        val result = myEditText.text
        myButtonLogin.isEnabled =
            result != null && result.toString().isNotEmpty() && myEditTextEmail.text.toString()
                .isNotEmpty()
    }

    private suspend fun login(email: String, password: String) {
        try {
            val message = viewModel.login(email, password)
            if (message.loginResult != null) {
                message.loginResult.token?.let { UserModel(email, it) }
                    ?.let { viewModel.saveUser(it) }
                showLoading(false)
                ViewModelFactory.resetInstance()
                showSuccessMessage()
            } else {
                showErrorAlert(getString(R.string.loginErrorAllert))
                showLoading(false)
            }
        } catch (e: IOException) {
            showErrorAlert(getString(R.string.loginInternetError))
            showLoading(false)
        } catch (e: HttpException) {
            handleApiError(e)
        }
    }

    private fun showSuccessMessage() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(getString(R.string.alertMsgLogin))
            setPositiveButton(getString(R.string.continueMsg)) { _, _ ->
                navigateToMainActivity()
            }
            create()
            show()
        }
    }

    private fun handleApiError(e: HttpException) {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        val errorMessage = errorBody.message
        showLoading(false)
    }

    private fun showErrorAlert(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK") { _, _ -> }
            create()
            show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun playEntranceAnimation() {
        val imageViewAnimator =
            ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f)
                .apply {
                    duration = 6000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }

        val titleAnimator =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val messageAnimator =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextViewAnimator =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayoutAnimator =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextViewAnimator =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayoutAnimator =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val loginButtonAnimator =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                messageAnimator,
                emailTextViewAnimator,
                emailEditTextLayoutAnimator,
                passwordTextViewAnimator,
                passwordEditTextLayoutAnimator,
                loginButtonAnimator
            )
            startDelay = 100
        }.start()

        imageViewAnimator.start()
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