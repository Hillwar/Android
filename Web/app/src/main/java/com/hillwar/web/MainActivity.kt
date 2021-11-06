package com.hillwar.web

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.hillwar.web.databinding.ActivityMainBinding
import kotlinx.coroutines.*


private const val ENDPOINT = "https://picsum.photos/v2/list?page=5&limit=10"

private const val LIST_KEY = "list"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var myService: MyService? = null

    private var isBound = false

    private var listOfImages: List<Image> = mutableListOf()

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            GlobalScope.launch(Dispatchers.Main) {
                val binderBridge: MyService.MyBinder = service as MyService.MyBinder
                myService = binderBridge.getMyService()
                isBound = true
                if (listOfImages.isEmpty()) withContext(Dispatchers.IO) {
                    listOfImages = myService!!.getListOfImages()
                }
                binding.list.adapter = ImageAdapter(listOfImages) {
                    val intent = Intent(binding.root.context, HightMainActivity::class.java)
                    intent.putExtra("image", it.url)
                    ContextCompat.startActivity(binding.root.context, intent, null)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            myService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("url", ENDPOINT)
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(boundServiceConnection)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(LIST_KEY, listOfImages as ArrayList<Image>)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        listOfImages = savedInstanceState.getParcelableArrayList<Image>(LIST_KEY) as List<Image>
    }
}
