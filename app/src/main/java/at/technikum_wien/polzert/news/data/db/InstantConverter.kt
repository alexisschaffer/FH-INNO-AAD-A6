package at.technikum_wien.polzert.news.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun toInstant(sinceEpoch: Long): Instant {
        return Instant.fromEpochSeconds(sinceEpoch)
    }

    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.epochSeconds
    }
}
