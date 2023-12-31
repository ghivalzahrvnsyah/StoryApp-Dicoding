package com.ghivalhrvnsyah.storyappdicoding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.di.Injection
import com.ghivalhrvnsyah.storyappdicoding.view.login.LoginViewModel
import com.ghivalhrvnsyah.storyappdicoding.view.main.MainViewModel
import com.ghivalhrvnsyah.storyappdicoding.view.maps.MapsViewModel
import com.ghivalhrvnsyah.storyappdicoding.view.signup.SignupViewModel
import com.ghivalhrvnsyah.storyappdicoding.view.welcome.WelcomeViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context)
                    )

                }
            }
            return INSTANCE as ViewModelFactory
        }

        fun resetInstance() {
            INSTANCE = null
            Injection.resetInstance()
        }
    }
}