package com.itblaze.mentor.data.view.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itblaze.mentor.data.repository.MentorRepository

class MentorViewModelProviderFactory(
    private val application: Application,
    private val repository: MentorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MentorsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MentorsViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}