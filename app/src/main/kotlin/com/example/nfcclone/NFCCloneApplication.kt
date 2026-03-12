package com.example.nfcclone

import android.app.Application
import timber.log.Timber

class NFCCloneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber logging
        Timber.plant(Timber.DebugTree())

        Timber.d("NFC Clone Application started")
    }
}
