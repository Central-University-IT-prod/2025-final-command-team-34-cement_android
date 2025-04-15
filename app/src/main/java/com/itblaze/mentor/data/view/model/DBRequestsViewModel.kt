package com.itblaze.requests.data.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.mentor.data.models.db.RequestItem
import kotlinx.coroutines.launch

class DBRequestsViewModel(private val database: MainDB) : ViewModel() {

    val allRequestsProfile: LiveData<List<RequestItem>> =
        database.requestsDAO().getAllRequestsItems()

    fun upsertRequestsProfile(requestItem: RequestItem) {
        viewModelScope.launch {
            database.requestsDAO().upsertRequestsItem(requestItem)
        }
    }

    fun deleteRequestsProfile(requestItem: RequestItem) {
        viewModelScope.launch {
            database.requestsDAO().deleteRequestsItem(requestItem)
        }
    }

    fun getRequestsProfileById(id: Int?) =
        if (id != null) database.requestsDAO().getRequestsByIdItems(id) else null
}