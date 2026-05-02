package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Query("SELECT * FROM election_table")
    fun getElections(): androidx.lifecycle.LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElectionById(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun clear()
}