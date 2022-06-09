package at.technikum_wien.polzert.news.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import at.technikum_wien.polzert.news.data.NewsItem
import kotlinx.datetime.Instant

@Dao
abstract class NewsItemDao {
    @get:Query("SELECT * FROM news_item ORDER BY publication_date DESC")
    abstract val newsItems : LiveData<List<NewsItem>>
    @Query("SELECT _id FROM news_item WHERE identifier = :identifier")
    abstract suspend fun getIdForIdentifier(identifier : String) : Long
    @Query("SELECT * FROM news_item LIMIT 1")
    abstract suspend fun getFirstNews(): NewsItem?
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
    @Query("DELETE  FROM news_item WHERE publication_date  < :publicationDate")
    abstract suspend fun deleteOlder(publicationDate : Long)

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
