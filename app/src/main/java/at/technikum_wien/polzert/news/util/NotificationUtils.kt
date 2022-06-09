package at.technikum_wien.polzert.news.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import com.bumptech.glide.request.transition.Transition;
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import at.technikum_wien.polzert.news.MyApp
import at.technikum_wien.polzert.news.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

object NotificationUtils {
    private val LOG_TAG = NotificationUtils::class.java.simpleName
    const val CHANNEL_ID = "news_channel_01"

    fun registerNotificationChannel(context : Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name : CharSequence = "News Channel 01"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(context : Context, notificationId : Int , title : String, imagePath : String) {
        Glide.with(MyApp.applicationContext())
            .asBitmap()
            .load(imagePath)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setLargeIcon(resource)
                        .setStyle(NotificationCompat.BigPictureStyle()
                            .bigPicture(resource)
                            .bigLargeIcon(null))


                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
                            NotificationManager

                    val intent = Intent(Intent.ACTION_VIEW, "mynews://show/$notificationId".toUri())
                    val taskStackBuilder = TaskStackBuilder.create(context)
                    taskStackBuilder.addNextIntentWithParentStack(intent)
                    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        taskStackBuilder.getPendingIntent(notificationId,
                            PendingIntent.FLAG_IMMUTABLE.or(PendingIntent.FLAG_UPDATE_CURRENT))
                    } else {
                        taskStackBuilder.getPendingIntent(notificationId,
                            PendingIntent.FLAG_UPDATE_CURRENT)
                    }
                    notificationBuilder.setContentIntent(pendingIntent)

                    notificationManager.notify(notificationId, notificationBuilder.build())
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })


    }
}