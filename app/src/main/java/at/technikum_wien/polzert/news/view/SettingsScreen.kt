package at.technikum_wien.polzert.news.view

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.technikum_wien.polzert.news.R
import at.technikum_wien.polzert.news.viewmodels.NewsListViewModel

@Composable
fun SettingsScreen(navController : NavController, viewModel : NewsListViewModel) {
    val feedUrl by viewModel.feedUrl.observeAsState()
    val showImages by viewModel.showImages.observeAsState()
    val downloadImages by viewModel.downloadImages.observeAsState()

    var newFeedUrl by remember { mutableStateOf(feedUrl) }
    var newShowImages by remember { mutableStateOf(showImages) }
    var newDownloadImages by remember { mutableStateOf(downloadImages) }

    BackHandler {
        viewModel.updatePreferences(
            feedUrl = newFeedUrl ?: "https://www.engadget.com/rss.xml",
            showImages = newShowImages ?: true,
            downloadImages = newDownloadImages ?: true
        )
        navController.navigateUp()
    }

    Column {
        TopAppBar(title = { Text(stringResource(R.string.settings)) })
        Text(
            text = stringResource(R.string.feed_url),
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                .fillMaxWidth()
        )
        TextField(
            value = newFeedUrl ?: "https://www.engadget.com/rss.xml",
            onValueChange = {
                newFeedUrl = it
            },
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth()
        )
        Divider(color = Color.LightGray, thickness = 1.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = newShowImages ?: true,
                onCheckedChange = {
                    newShowImages = it
                },
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            Column(modifier = Modifier.padding(8.dp)){
                Text(
                    stringResource(R.string.show_images),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (newShowImages != false)
                    Text(stringResource(R.string.image_display_yes),
                        style = MaterialTheme.typography.body1,)
                else
                    Text(stringResource(R.string.image_display_no),
                        style = MaterialTheme.typography.body1,)
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = newDownloadImages ?: true,
                onCheckedChange = {
                    newDownloadImages = it
                },
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            Column(modifier = Modifier.padding(8.dp)){
                Text(
                    stringResource(R.string.download_images),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (newDownloadImages != false)
                    Text(stringResource(R.string.image_download_yes),
                        style = MaterialTheme.typography.body1,)
                else
                    Text(stringResource(R.string.image_download_no),
                        style = MaterialTheme.typography.body1,)
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    //https://dev.to/pawegio/handling-back-presses-in-jetpack-compose-50d5
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
