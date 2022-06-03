package at.technikum_wien.polzert.news.data.db

import androidx.room.TypeConverter

class SetConverter {
    @TypeConverter
    fun toSet(str: String): Set<String> {
        return str.split("\n").toSet()
    }

    @TypeConverter
    fun fromSet(set: Set<String>): String {
        return set.joinToString("\n")
    }
}
