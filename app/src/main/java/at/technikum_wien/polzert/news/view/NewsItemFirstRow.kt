package at.technikum_wien.polzert.news.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import at.technikum_wien.polzert.news.data.NewsItem
import at.technikum_wien.polzert.news.viewmodels.NewsListViewModel

@Composable
fun NewsItemFirstRow(navController : NavController, index : Int, newsItem : NewsItem, viewModel : NewsListViewModel) {
    Column(
        modifier = Modifier
            .clickable {
                navController.navigate(Screen.DetailScreen.route + "/${index}")
            }
    ) {
        LargeNewsImage(newsItem = newsItem, viewModel = viewModel)
    }
}
