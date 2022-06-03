package at.technikum_wien.polzert.news.data

import android.content.Context
import at.technikum_wien.polzert.news.data.db.ApplicationDatabase

class NewsRepository(context: Context) {
    private val newsItemDao by lazy { ApplicationDatabase.getDatabase(context).newsItemDao() }
    val newsItems by lazy { newsItemDao.newsItems }

    suspend fun updateOrInsertAll(newsItems : List<NewsItem>) {
        newsItemDao.updateOrInsertAll(newsItems = newsItems)
    }

    suspend fun replace(newsItems : List<NewsItem>) {
        newsItemDao.replaceAll(newsItems = newsItems)
    }

    suspend fun deleteAll() {
        newsItemDao.deleteAll()
    }
}
