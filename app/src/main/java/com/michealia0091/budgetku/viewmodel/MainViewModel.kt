package com.michealia0091.budgetku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michealia0091.budgetku.database.KeuanganDao
import com.michealia0091.budgetku.model.Keuangan
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dao: KeuanganDao
) : ViewModel() {

    val dataKeuangan = dao.getAllKeuangan()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun insert(
        keterangan: String,
        nominal: Int,
        jenis: String,
        saldoManual: Int
    ) {

        val data = Keuangan(
            keterangan = keterangan,
            nominal = nominal,
            jenis = jenis,
            saldoManual = saldoManual
        )

        viewModelScope.launch {
            dao.insert(data)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            dao.softDeleteById(id)
        }
    }

    fun restore(id: Long) {
        viewModelScope.launch {
            dao.restoreById(id)
        }
    }
}