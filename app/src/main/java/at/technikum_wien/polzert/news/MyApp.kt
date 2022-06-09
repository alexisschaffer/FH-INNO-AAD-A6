package at.technikum_wien.polzert.news

import android.app.Application
import android.content.Context


class MyApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MyApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MyApp.applicationContext()
    }
}