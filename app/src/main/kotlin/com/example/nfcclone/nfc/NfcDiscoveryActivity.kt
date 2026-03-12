package com.example.nfcclone.nfc

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import timber.log.Timber

class NfcDiscoveryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleNfcIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        when (intent.action) {
            NfcAdapter.ACTION_TECH_DISCOVERED,
            NfcAdapter.ACTION_TAG_DISCOVERED -> {
                Timber.d("NFC Tag discovered")
                val tagData = NfcManager.getNfcTagFromIntent(intent)
                if (tagData != null) {
                    Timber.d("Tag ID: ${tagData.id}")
                    Timber.d("Tag Type: ${tagData.type}")
                    Timber.d("Tag Technologies: ${tagData.technologies}")
                    // Hier können Sie die Tag-Daten verarbeiten oder an die MainActivity weitergeben
                }
            }
        }
        finish()
    }
}
