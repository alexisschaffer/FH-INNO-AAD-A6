package at.technikum_wien.polzert.news.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.technikum_wien.polzert.news.data.NewsItem
import at.technikum_wien.polzert.news.util.Util
import at.technikum_wien.polzert.news.viewmodels.NewsListViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NewsItemRow(navController : NavController, index : Int, newsItem : NewsItem, viewModel : NewsListViewModel) {
    val showImages by viewModel.showImages.observeAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                navController.navigate(Screen.DetailScreen.route + "/${index}")
            }
            .padding(horizontal = 4.dp)
    ) {
        if (newsItem.imageUrl != null && showImages != false)
            GlideImage(
                imageModel = newsItem.imageUrl!!,
                contentScale = ContentScale.Fit,
                circularReveal = CircularReveal(duration = 250),
                modifier = Modifier
                    .size(80.dp)
                    .padding(2.dp)
            )
        Column(modifier = Modifier.padding(2.dp)) {
            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = newsItem.author ?: "",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = Util.instantToString(newsItem.publicationDate),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
