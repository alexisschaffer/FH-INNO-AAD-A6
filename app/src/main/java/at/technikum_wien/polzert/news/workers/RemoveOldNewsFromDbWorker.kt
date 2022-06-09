package at.technikum_wien.polzert.news.workers

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import at.technikum_wien.polzert.news.data.NewsRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*


class RemoveOldNewsFromDbWorker(applicationContext: Context, workerParams : WorkerParameters) : CoroutineWorker(applicationContext, workerParams),
    LifecycleOwner {

    companion object {
        val LOG_TAG: String = RemoveOldNewsFromDbWorker::class.java.simpleName

    }

    override suspend fun doWork(): Result = coroutineScope {
        Log.d(LOG_TAG, "Work started")

        //sleep(100000);
        val newsRepository : NewsRepository = NewsRepository(applicationContext)

        val keepDuration : Long = inputData.getLong("keep_duration", 432000)
        val dateLimit : Long = Clock.System.now().epochSeconds - keepDuration

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val netDate = Date(dateLimit * 1000)
        Log.i(GetDataInsertInDbWorker.LOG_TAG, "Keeping duration is: " + keepDuration.toString())
        Log.i(GetDataInsertInDbWorker.LOG_TAG, "Date limit: " + sdf.format(netDate))

        newsRepository.deleteOlder(dateLimit)

        Result.success()

    }

    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }
}

