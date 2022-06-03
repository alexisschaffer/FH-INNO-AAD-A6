package at.technikum_wien.polzert.news.viewmodels

import androidx.lifecycle.*
import at.technikum_wien.polzert.news.data.download.NewsDownloader
import at.technikum_wien.polzert.news.data.NewsItem
import at.technikum_wien.polzert.news.data.NewsRepository
import at.technikum_wien.polzert.news.settings.UserPreferencesRepository
import kotlinx.coroutines.launch

class NewsListViewModel(private val newsRepository : NewsRepository, private val userPreferencesRepository : UserPreferencesRepository) : ViewModel() {
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
                    downloadNewsItems(it.feedUrl, delete = true)
                }
                lastFeedUrl = it.feedUrl
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

    fun reload() {
        lastFeedUrl?.let { downloadNewsItems(it, delete = false) }
    }

    fun updatePreferences(feedUrl : String, showImages : Boolean, downloadImages : Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateFeedUrl(feedUrl = feedUrl)
            userPreferencesRepository.updateShowImages(showImages = showImages)
            userPreferencesRepository.updateDownloadImages(downloadImages = downloadImages)
        }
    }
}
