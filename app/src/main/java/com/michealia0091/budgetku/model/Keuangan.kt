package com.michealia0091.budgetku.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keuangan")
data class Keuangan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val keterangan: String,
    val nominal: Int,
    val jenis: String,
    val saldoManual: Int = 0,
    val isDeleted: Boolean = false
)