package com.ghivalhrvnsyah.storyappdicoding.data

import androidx.lifecycle.liveData
import com.ghivalhrvnsyah.storyappdicoding.api.ApiConfig
import com.ghivalhrvnsyah.storyappdicoding.api.ApiService
import com.ghivalhrvnsyah.storyappdicoding.data.model.UserModel
import com.ghivalhrvnsyah.storyappdicoding.data.pref.SettingPreferences
import com.ghivalhrvnsyah.storyappdicoding.data.pref.UserPreferences
import com.ghivalhrvnsyah.storyappdicoding.di.ResultState
import com.ghivalhrvnsyah.storyappdicoding.response.FileUploadResponse
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import com.ghivalhrvnsyah.storyappdicoding.response.LoginResponse
import com.ghivalhrvnsyah.storyappdicoding.response.RegisterResponse
import com.ghivalhrvnsyah.storyappdicoding.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPref: UserPreferences,
    private val settingPref: SettingPreferences
) {


    suspend fun saveSession(user: UserModel) {
        userPref.saveSession(user)

    }

    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }

    suspend fun logout() {
        userPref.logout()
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getStories(): List<ListStoryItem?>? {
        return apiService.getStories().listStory
    }

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }

    fun getThemeSettings() = settingPref.getThemeSetting()
    suspend fun saveThemeSetting(checked: Boolean) {
        settingPref.saveThemeSetting(checked)
    }




    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPref: UserPreferences,
            settingPref: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPref, settingPref)

            }.also { instance = it }

        fun refreshRepository() {
            instance = null
        }
    }
}