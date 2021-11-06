package com.hillwar.contacts

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hillwar.contacts.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val myRequestId = 0
    private var permission by Delegates.notNull<Boolean>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_CONTACTS,
                    android.Manifest.permission.SEND_SMS
                ), myRequestId
            )
            permission = false
            binding.errorText.visibility = View.VISIBLE
        } else {
            permission = true
            binding.errorText.visibility = View.GONE
            setContentView(binding.root)
            val list = fetchAllContacts()
            val toast = Toast.makeText(
                this,
                resources.getQuantityString(
                    R.plurals.numberOfContactsAvailable,
                    list.size,
                    list.size
                ),
                Toast.LENGTH_SHORT
            )
            toast.show()
            binding.list.adapter = ContactAdapter(this, list)
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
            && !permission) {
            recreate()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            myRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate()
                }
            }
        }
    }

    fun settings(view: android.view.View) {
        this.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")
            )
        )
    }

    fun restart(view: android.view.View) {
        recreate()
    }

}