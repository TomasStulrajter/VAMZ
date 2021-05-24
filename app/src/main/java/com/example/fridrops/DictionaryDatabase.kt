package com.example.fridrops

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * trieda predstavuuca databazu - pozostava z tabuliek a z objektu DAO
 */
@Database(entities = [Word::class], version = 3, exportSchema = false)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao() : DictionaryDao

    companion object {
        @Volatile
        private var instance : DictionaryDatabase? = null

        fun getDatabase(context : Context) : DictionaryDatabase {
            val tempInstance = instance
            if(instance != null) {
                return tempInstance!!
            } else {
                synchronized(this) {
                    val newInstance = Room.databaseBuilder(context.applicationContext, DictionaryDatabase::class.java, "dictionary").fallbackToDestructiveMigration().build()
                    instance = newInstance
                    return newInstance
                }
            }
        }

    }
}