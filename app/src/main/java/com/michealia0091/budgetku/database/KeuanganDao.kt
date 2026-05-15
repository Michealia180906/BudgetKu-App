package com.michealia0091.budgetku.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.michealia0091.budgetku.model.Keuangan
import kotlinx.coroutines.flow.Flow

@Dao
interface KeuanganDao {

    @Insert
    suspend fun insert(keuangan: Keuangan)

    @Update
    suspend fun update(keuangan: Keuangan)

    @Query("SELECT * FROM keuangan WHERE isDeleted = 0 ORDER BY id DESC")
    fun getAllKeuangan(): Flow<List<Keuangan>>

    @Query("SELECT * FROM keuangan WHERE isDeleted = 1 ORDER BY id DESC")
    fun getRecycleBin(): Flow<List<Keuangan>>

    @Query("SELECT * FROM keuangan WHERE id = :id")
    suspend fun getKeuanganById(id: Long): Keuangan?

    @Query("UPDATE keuangan SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteById(id: Long)

    @Query("UPDATE keuangan SET isDeleted = 0 WHERE id = :id")
    suspend fun restoreById(id: Long)

    @Query("DELETE FROM keuangan WHERE id = :id")
    suspend fun deletePermanentById(id: Long)
}