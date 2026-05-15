package com.michealia0091.budgetku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michealia0091.budgetku.model.Keuangan

@Database(
    entities = [Keuangan::class],
    version = 1,
    exportSchema = false
)
abstract class KeuanganDb : RoomDatabase() {

    abstract val dao: KeuanganDao

    companion object {

        @Volatile
        private var INSTANCE: KeuanganDb? = null

        fun getInstance(context: Context): KeuanganDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        KeuanganDb::class.java,
                        "budgetku.db"
                    ).build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}