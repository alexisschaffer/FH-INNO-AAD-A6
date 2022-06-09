package at.technikum_wien.polzert.news.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.technikum_wien.polzert.news.data.NewsRepository
import at.technikum_wien.polzert.news.settings.UserPreferencesRepository

class NewsListViewModelFactory(private val application : Application ,private val newsRepository : NewsRepository, private val userPreferencesRepository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsListViewModel(application = application, newsRepository = newsRepository, userPreferencesRepository = userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
