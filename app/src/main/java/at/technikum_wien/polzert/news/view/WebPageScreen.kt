package at.technikum_wien.polzert.news.view

import android.util.Base64
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import at.technikum_wien.polzert.news.viewmodels.NewsListViewModel


@Composable
fun WebPageScreen(viewModel : NewsListViewModel, data : String) {
    val showImages by viewModel.showImages.observeAsState()

    var html =
        "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "img {\n" +
                "  width: 100% !important;\n" +
                "  height: auto !important;\n"
    if (showImages == false)
        html += "  display: none !important;\n"
    html +=             "},\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            data + "\n" +
            "</body>\n" +
            "</html>"
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context -> WebView(context) }
    ) { webView ->
        with(webView){
            val encodedHtml = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
            loadData(encodedHtml, "text/html", "base64")
        }
    }
}
