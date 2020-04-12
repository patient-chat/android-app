package com.patientchat.androidapp.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

// TODO: set directory for Room ot use to export schema
@Database(entities = [Patient::class], version = 1, exportSchema = false)
public abstract class PatientChatDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDoa

    companion object {
        @Volatile
        private var INSTANCE: PatientChatDatabase? = null

        fun getDatabase(context: Context): PatientChatDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatientChatDatabase::class.java,
                    "patient_chat_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}