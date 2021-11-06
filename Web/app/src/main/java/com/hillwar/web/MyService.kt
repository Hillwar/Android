package com.hillwar.web

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


private var map = mutableMapOf<String, Bitmap?>()

class MyService : Service() {

    private lateinit var url: String

    private lateinit var context: Context

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        url = intent?.getStringExtra("url").toString()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        return MyBinder()
    }

    inner class MyBinder : Binder() {
        fun getMyService() = this@MyService
    }

    suspend fun downloadImage(context: Context): Bitmap? {
        return if (map[url] == null) {
            var bitmapImage: Bitmap? = null
            try {
                val inputStream = URL(url).openStream()
                bitmapImage = BitmapFactory.decodeStream(inputStream)
                bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 720, 720, false)
            } catch (e: Exception) {
                Log.i("Test", "image")
                error()
            }
            map[url] = bitmapImage
            bitmapImage
        } else map[url]

    }

    suspend fun getListOfImages(): List<Image> {
        Log.i("Connection_test", "connection")
        val images = mutableListOf<Image>()
        var text: String? = null
        try {
            val httpUrlConnection = URL(url).openConnection() as HttpURLConnection
            httpUrlConnection.apply {
                connectTimeout = 10000
                requestMethod = "GET"
                doInput = true
            }
            val streamReader = InputStreamReader(httpUrlConnection.inputStream)
            text = streamReader.readText()
            httpUrlConnection.disconnect()
        } catch (e: Exception) {
            error()
        }
        if (text == null) return mutableListOf(Image("none", null, null))
        val json = JSONArray(text)
        for (i in 0 until json.length()) {
            val jsonImage = json.getJSONObject(i)
            val author = jsonImage.getString("author")
            val urlImage = jsonImage.getString("download_url")
            var myBitmap: Bitmap? = null
            try {
                val connection = URL(urlImage).openConnection() as HttpURLConnection
                connection.apply {
                    connectTimeout = 10000
                    requestMethod = "GET"
                    doInput = true
                }
                myBitmap = BitmapFactory.decodeStream(connection.inputStream)
                connection.disconnect()
            } catch (e: java.lang.Exception) {
                Log.i("Test", "item_of_list")
                error()
            }
            myBitmap = myBitmap?.let { Bitmap.createScaledBitmap(it, 200, 200, false) }
            images.add(Image(author, myBitmap, urlImage))
        }
        return images
    }

    private suspend fun error() {
        withContext(Dispatchers.Main) {
            val toast = Toast.makeText(
                applicationContext,
                "Нет подключения к Интернету. Повторите попытку позже", Toast.LENGTH_LONG
            )
            toast.show()
        }
    }
}