package com.itblaze.mentor.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itblaze.mentor.data.models.db.RequestItem

@Dao
interface RequestsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRequestsItem(requestsItem: RequestItem)

    @Delete
    suspend fun deleteRequestsItem(requestsItem: RequestItem)

    @Query("SELECT * FROM requestItems")
    fun getAllRequestsItems() : LiveData<List<RequestItem>>

    @Query("SELECT * FROM requestItems WHERE id = :id")
    fun getRequestsByIdItems(id: Int) : LiveData<RequestItem?>
}