package com.hillwar.retrofit

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun error(context: Context) {
    withContext(Dispatchers.Main) {
        val toast = Toast.makeText(
            context,
            "Нет подключения к Интернету. Повторите попытку позже", Toast.LENGTH_LONG
        )
        toast.show()
    }
}