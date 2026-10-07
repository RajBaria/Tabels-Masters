package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.QuizDao
import com.example.data.local.entity.MistakeRecord
import com.example.data.local.entity.QuizResult
import com.example.data.local.entity.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        QuizResult::class,
        MistakeRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gadiya_master_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Seed default user profile
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.quizDao()?.insertProfile(
                                    UserProfile(
                                        name = "Primary Learner",
                                        gradeOrLevel = "Standard 5",
                                        avatarIndex = 0,
                                        pinHash = null,
                                        isCurrent = true
                                    )
                                )
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
