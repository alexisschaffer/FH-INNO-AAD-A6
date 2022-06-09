package at.technikum_wien.polzert.news.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Error
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
import java.text.SimpleDateFormat

@Composable
fun NewsItemRow(navController : NavController, index : Int, newsItem : NewsItem, viewModel : NewsListViewModel) {
    val showImages by viewModel.showImages.observeAsState()

    Card(elevation = 1.dp,
        border = BorderStroke(0.dp, MaterialTheme.colors.surface),
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 30.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.DetailScreen.route + "/${index}")
            }
    )
    {
        Column() {
            if(newsItem.imageUrl != null && showImages != false){
                GlideImage(
                    imageModel = newsItem.imageUrl!!,
                    contentScale = ContentScale.Fit,
                    circularReveal = CircularReveal(duration = 250),
                    placeHolder = Icons.Filled.Downloading,
                    error = Icons.Filled.Error,
                )
            }
            Row(modifier = Modifier.padding(all = 10.dp)){
                Text(text = newsItem.title, style = MaterialTheme.typography.h6)
            }
            Row(modifier = Modifier.padding(all = 10.dp)){
                Text(text = newsItem.author ?: "", style = MaterialTheme.typography.subtitle1)
            }
            Row(modifier = Modifier.padding(all = 10.dp)){
                val format  = SimpleDateFormat("MMM dd, yyyy HH:mm:ss")
                Text(text = Util.instantToString(newsItem.publicationDate), style = MaterialTheme.typography.caption)
            }
        }
    }
}
