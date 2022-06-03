package at.technikum_wien.polzert.news.data.db

import at.technikum_wien.polzert.news.data.NewsItem

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class NewsItemDao {
    @get:Query("SELECT * FROM news_item ORDER BY publication_date DESC")
    abstract val newsItems : LiveData<List<NewsItem>>
    @Query("SELECT _id FROM news_item WHERE identifier = :identifier")
    abstract suspend fun getIdForIdentifier(identifier : String) : Long
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(newsItem: NewsItem): Long
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(newsItems: List<NewsItem>): List<Long>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun tryInsert(newsItem: NewsItem): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun tryInsert(newsItems: List<NewsItem>): List<Long>
    @Update
    abstract suspend fun update(newsItem: NewsItem)
    @Update
    abstract suspend fun update(newsItems: List<NewsItem>)
    @Delete
    abstract suspend fun delete(newsItem: NewsItem)
    @Query("DELETE FROM news_item")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun updateOrInsertAll(newsItems: List<NewsItem>) {
        val insertResult: List<Long> = tryInsert(newsItems)
        val updateList: MutableList<NewsItem> = ArrayList()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                newsItems[i].id = getIdForIdentifier(newsItems[i].identifier)
                updateList.add(newsItems[i])
            }
        }
        if (updateList.isNotEmpty()) {
            update(updateList)
        }
    }

    @Transaction
    open suspend fun replaceAll(newsItems: List<NewsItem>) {
        deleteAll()
        insert(newsItems)
    }
}
