package at.technikum_wien.polzert.news.view

import androidx.compose.foundation.background
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
import at.technikum_wien.polzert.news.data.NewsItem
import at.technikum_wien.polzert.news.ui.theme.SemiTransparentWhite
import at.technikum_wien.polzert.news.util.Util
import at.technikum_wien.polzert.news.viewmodels.NewsListViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun LargeNewsImage(viewModel : NewsListViewModel, newsItem : NewsItem) {
    val showImages by viewModel.showImages.observeAsState()

    Box(contentAlignment = Alignment.BottomStart) {
        if (newsItem.imageUrl != null && showImages != false) {
            GlideImage(
                imageModel = newsItem.imageUrl!!,
                contentScale = ContentScale.Fit,
                circularReveal = CircularReveal(duration = 250),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(modifier = Modifier.padding(start = 50.dp, bottom = 5.dp, end = 5.dp)) {
            Column(
                modifier = Modifier
                    .background(
                        color = SemiTransparentWhite
                    )
            ) {
                Text(
                    text = newsItem.title,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                )
                Text(
                    text = newsItem.author ?: "",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                )
                Text(
                    text = Util.instantToString(newsItem.publicationDate),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                )
            }
        }
    }
}
