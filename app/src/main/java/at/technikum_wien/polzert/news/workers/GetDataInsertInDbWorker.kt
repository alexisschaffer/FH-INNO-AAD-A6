package at.technikum_wien.polzert.news.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import at.technikum_wien.polzert.news.data.download.NewsDownloader
import androidx.lifecycle.*
import at.technikum_wien.polzert.news.data.NewsRepository
import kotlinx.coroutines.coroutineScope

class GetDataInsertInDbWorker(applicationContext: Context, workerParams : WorkerParameters) : CoroutineWorker(applicationContext, workerParams), LifecycleOwner {

    companion object {
        val LOG_TAG: String = GetDataInsertInDbWorker::class.java.simpleName
        
    }

    override suspend fun doWork(): Result = coroutineScope {
        Log.d(LOG_TAG, "Work started")
        val newsRepository : NewsRepository = NewsRepository(applicationContext)
        if(inputData.getString("feed_url") == null){
            Log.d(LOG_TAG, "Passed parameter feed_url = null")
            Result.failure()
        }
        val feedUrl : String = inputData.getString("feed_url").toString();

        if (inputData.getBoolean("delete", false)){
            Log.d(LOG_TAG, "Delete = true")
            newsRepository.deleteAll()
        }

        Log.i(LOG_TAG, "Worker initiate news fetch for: " + feedUrl)
        when (val newsItems = NewsDownloader().load(feedUrl)) {
            null -> {
                Log.d(LOG_TAG, "Work failed.")
                Result.failure()
            }
            else -> {
                newsRepository.updateOrInsertAll(newsItems)
            }
        }

        Result.success()

    }

    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }
}

