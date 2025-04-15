package com.itblaze.mentor.data.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.requests.data.view.model.DBRequestsViewModel

class DBRequestsViewModelProviderFactory(private val database: MainDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DBRequestsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DBRequestsViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}