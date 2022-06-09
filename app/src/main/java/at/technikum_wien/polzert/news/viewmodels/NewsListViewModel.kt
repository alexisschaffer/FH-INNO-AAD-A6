package at.technikum_wien.polzert.news.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import androidx.work.*
import at.technikum_wien.polzert.news.MyApp
import at.technikum_wien.polzert.news.data.download.NewsDownloader
import at.technikum_wien.polzert.news.data.NewsItem
import at.technikum_wien.polzert.news.data.NewsRepository
import at.technikum_wien.polzert.news.data.db.ApplicationDatabase
import at.technikum_wien.polzert.news.settings.UserPreferencesRepository
import at.technikum_wien.polzert.news.workers.GetDataInsertInDbWorker
import at.technikum_wien.polzert.news.workers.RemoveOldNewsFromDbWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewsListViewModel(application: Application, private val newsRepository : NewsRepository, private val userPreferencesRepository : UserPreferencesRepository) : AndroidViewModel(application) {
    val LOG_TAG = NewsListViewModel::class.java.simpleName
    private val _error = MutableLiveData(false)
    private val _busy = MutableLiveData(false)
    private var lastFeedUrl : String? = null

    init {

        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect {
                feedUrl.value = it.feedUrl
                showImages.value = it.showImages
                downloadImages.value = it.downloadImages
                if (lastFeedUrl != null && lastFeedUrl != it.feedUrl) {
                    Log.i(LOG_TAG, "Fetch data because of url change from: " + it.feedUrl)
                    reload(feedUrl= it.feedUrl, delete = true)
                    reloadPeriodic(feedUrl = it.feedUrl)
                } else {
                    reloadPeriodic(feedUrl = it.feedUrl)
                }
                lastFeedUrl = it.feedUrl

                // Worker of initial fetch if DB is empty
                if(ApplicationDatabase.getDatabase(application.applicationContext).newsItemDao().getFirstNews() == null){
                    Log.i(LOG_TAG, "DB is empty fetch data from: " + lastFeedUrl)
                    reload()
                }
                // Delete entries older than 5 Days every Day
                deleteOldPeriodic(432000)

            }
        }
    }

    val newsItems by lazy { newsRepository.newsItems }
    val error : LiveData<Boolean>
        get() = _error
    val busy : LiveData<Boolean>
        get() = _busy
    val feedUrl = MutableLiveData("")
    val showImages = MutableLiveData(false)
    val downloadImages = MutableLiveData(false)

    private fun downloadNewsItems(newsFeedUrl: String, delete : Boolean) {
        _error.value = false
        _busy.value = true
        viewModelScope.launch {
            if (delete)
                newsRepository.deleteAll()
            val newsItems = NewsDownloader().load(newsFeedUrl)
            when (newsItems) {
                null -> _error.value = true
                else -> newsRepository.updateOrInsertAll(newsItems)
            }
            _busy.value = false
        }
    }

    fun reload(delete: Boolean = false, feedUrl: String = "") {
        val data = Data.Builder()
        //Add parameter in Data class. just like bundle. You can also add Boolean and Number in parameter.
        data.putString("feed_url", if(feedUrl != "") feedUrl else lastFeedUrl)
        data.putBoolean("delete", delete)

        val workRequest = OneTimeWorkRequest.Builder(GetDataInsertInDbWorker::class.java)
            .setInputData(data.build())
            .build()

        WorkManager
            .getInstance(MyApp.applicationContext())
            .enqueue(workRequest)

    }

    private fun reloadPeriodic(delete: Boolean = false, feedUrl: String = "") {
        val data = Data.Builder()
        val url : String? = if(feedUrl != "") feedUrl else lastFeedUrl
        //Add parameter in Data class. just like bundle. You can also add Boolean and Number in parameter.
        data.putString("feed_url", url)
        data.putBoolean("delete", delete)

        Log.i(LOG_TAG, "Start periodic worker (30, Minutes) with url: " + url)
        val workRequest = PeriodicWorkRequest.Builder(
            GetDataInsertInDbWorker::class.java,
            30,
            TimeUnit.MINUTES
        )
            .setInputData(data.build())
            .build()

        WorkManager
            .getInstance(MyApp.applicationContext())
            .enqueueUniquePeriodicWork(
                GetDataInsertInDbWorker::class.java.canonicalName ?: "-periodic",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )

    }

    private fun deleteOldPeriodic(keepDuration : Long){
        val data = Data.Builder()
        data.putLong("keep_duration", keepDuration)

        Log.i(LOG_TAG, "Start periodic worker (1, Day) with keep duration: " + keepDuration.toString())
        val workRequest = PeriodicWorkRequest.Builder(
            RemoveOldNewsFromDbWorker::class.java,
            1,
            TimeUnit.DAYS
        )
            .setInputData(data.build())
            .build()

        WorkManager
            .getInstance(MyApp.applicationContext())
            .enqueueUniquePeriodicWork(
                RemoveOldNewsFromDbWorker::class.java.canonicalName ?: "-periodic",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun updatePreferences(feedUrl : String, showImages : Boolean, downloadImages : Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateFeedUrl(feedUrl = feedUrl)
            userPreferencesRepository.updateShowImages(showImages = showImages)
            userPreferencesRepository.updateDownloadImages(downloadImages = downloadImages)
        }
    }
}
