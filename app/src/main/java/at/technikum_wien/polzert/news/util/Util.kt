package at.technikum_wien.polzert.news.util

import kotlinx.datetime.Instant
import java.text.DateFormat
import java.util.*

object Util {
    private val df: DateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)

    fun instantToString(instant: Instant): String {
        return df.format(Date(1000 * instant.epochSeconds))
    }
}
