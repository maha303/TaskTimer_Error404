package com.example.tasktimer_error404.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Goal::class, Task::class], version = 2, exportSchema = false)
abstract class TimerDatabase: RoomDatabase() {

    abstract fun timerDao(): TimerDao

    companion object {
        @Volatile  // writes to this field are immediately visible to other threads
        private var INSTANCE: TimerDatabase? = null

        fun getDatabase(context: Context): TimerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {  // protection from concurrent execution on multiple threads
                val instance = Room.databaseBuilder(
                    context.applicationContext, //application context stays longer than activity context
                    TimerDatabase::class.java,
                    "timer_database" //db name
                ).fallbackToDestructiveMigration()  // Destroys old database on version change
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}