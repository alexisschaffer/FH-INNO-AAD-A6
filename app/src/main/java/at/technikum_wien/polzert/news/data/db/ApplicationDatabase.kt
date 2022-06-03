package at.technikum_wien.polzert.news.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import at.technikum_wien.polzert.news.data.NewsItem

@Database(entities = [NewsItem::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun newsItemDao(): NewsItemDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val tempInstance2 = INSTANCE
                if (tempInstance2 != null) {
                    return tempInstance2
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "news_item"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
