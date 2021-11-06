package com.hillwar.web

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.hillwar.web.databinding.HightActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HightMainActivity : AppCompatActivity() {

    private lateinit var binding: HightActivityMainBinding

    private var myService: MyService? = null

    private var isBound = false

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            GlobalScope.launch(Dispatchers.Main) {
                val binderBridge: MyService.MyBinder = service as MyService.MyBinder
                myService = binderBridge.getMyService()
                isBound = true
                val bitmap: Bitmap? = withContext(Dispatchers.IO){ myService!!.downloadImage(binding.root.context) }
                if (bitmap != null) binding.photo.setImageBitmap(bitmap)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            myService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HightActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyService::class.java)
        val url = getIntent().getStringExtra("image")
        intent.putExtra("url", url)
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(boundServiceConnection)
        }
    }
}