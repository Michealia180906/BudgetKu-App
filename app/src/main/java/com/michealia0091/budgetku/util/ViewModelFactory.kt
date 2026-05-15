package com.michealia0091.budgetku.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michealia0091.budgetku.database.KeuanganDao
import com.michealia0091.budgetku.viewmodel.DetailViewModel
import com.michealia0091.budgetku.viewmodel.MainViewModel

class ViewModelFactory(
    private val dao: KeuanganDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        }

        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}