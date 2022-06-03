package at.technikum_wien.polzert.news.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.technikum_wien.polzert.news.R
import at.technikum_wien.polzert.news.data.NewsItem
import at.technikum_wien.polzert.news.viewmodels.NewsListViewModel

@Composable
fun DetailScreen(navController: NavController, newsItem : NewsItem?, viewModel : NewsListViewModel) {
    val scrollState = rememberScrollState()
    val keywordScrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                if (newsItem != null)
                    LargeNewsImage(newsItem = newsItem, viewModel = viewModel)
                WebPageScreen(data = newsItem?.description ?: "", viewModel = viewModel)
                Text(
                    stringResource(R.string.keywords),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                )
                Row(
                    modifier = Modifier
                        .horizontalScroll(keywordScrollState)
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = newsItem?.keywords?.joinToString(", ") ?: "",
                        style = MaterialTheme.typography.body2
                    )
                }
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(newsItem?.link)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        stringResource(R.string.full_story),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    )
}
