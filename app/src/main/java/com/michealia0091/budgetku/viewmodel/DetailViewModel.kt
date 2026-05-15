package com.michealia0091.budgetku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michealia0091.budgetku.database.KeuanganDao
import com.michealia0091.budgetku.model.Keuangan
import kotlinx.coroutines.launch

class DetailViewModel(
    private val dao: KeuanganDao
) : ViewModel() {

    suspend fun getKeuangan(id: Long): Keuangan? {
        return dao.getKeuanganById(id)
    }

    fun update(
        id: Long,
        keterangan: String,
        nominal: Int,
        jenis: String,
        saldoManual: Int
    ) {
        val data = Keuangan(
            id = id,
            keterangan = keterangan,
            nominal = nominal,
            jenis = jenis,
            saldoManual = saldoManual
        )

        viewModelScope.launch {
            dao.update(data)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            dao.softDeleteById(id)
        }
    }
}