package com.example.unitybridge

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.existing.sdk.ExistingSdk

class UnitySdkBridge(private val context: Context) {
    fun initializeSync(callback: InitializationCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            val existingSdk = ExistingSdk(context)
            val result = existingSdk.initialize()
            callback.onInitializationComplete(result)
        }
    }

    fun createFileSync(size: Int, callback: FileCreationCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            val existingSdk = ExistingSdk(context)
            val result = existingSdk.createFile(size)
            callback.onFileCreationComplete(result)
        }
    }
}