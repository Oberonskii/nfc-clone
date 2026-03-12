package com.example.nfcclone.nfc

import android.app.Activity
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import timber.log.Timber

object NfcManager {
    fun getNfcTagFromIntent(intent: Intent): NfcTagData? {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return null
        
        return try {
            val id = tag.id.toHexString()
            val technologies = tag.techList.map { it.substringAfterLast('.') }
            val type = tag.type ?: "Unknown"

            val ndefMessage = extractNdefMessage(tag)

            NfcTagData(
                id = id,
                type = type,
                technologies = technologies,
                ndefMessage = ndefMessage,
                rawData = tag.id
            )
        } catch (e: Exception) {
            Timber.e(e, "Error reading NFC tag")
            null
        }
    }

    private fun extractNdefMessage(tag: Tag): String? {
        return try {
            val ndef = Ndef.get(tag) ?: return null
            val ndefMessage = ndef.cachedNdefMessage ?: return null

            val messages = mutableListOf<String>()
            for (record in ndefMessage.records) {
                val payload = record.payload
                if (record.tnf == NdefRecord.TNF_WELL_KNOWN &&
                    record.type.contentEquals(NdefRecord.RTD_TEXT)
                ) {
                    try {
                        val text = String(payload.copyOfRange(1, payload.size))
                        messages.add(text)
                    } catch (e: Exception) {
                        Timber.e(e, "Error decoding NDEF text record")
                    }
                } else if (record.tnf == NdefRecord.TNF_WELL_KNOWN &&
                    record.type.contentEquals(NdefRecord.RTD_URI)
                ) {
                    try {
                        val uri = String(payload)
                        messages.add(uri)
                    } catch (e: Exception) {
                        Timber.e(e, "Error decoding NDEF URI record")
                    }
                }
            }

            if (messages.isNotEmpty()) {
                messages.joinToString(" | ")
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error extracting NDEF message")
            null
        }
    }

    fun readNfcTag(tag: Tag): NfcTagData? {
        return try {
            val nfcA = NfcA.get(tag)
            nfcA?.connect()

            // Sende eine einfache SELECT-Kommando für Master File (MF)
            val selectMf = byteArrayOf(0x00, 0xA4, 0x00, 0x00, 0x00)
            val response = nfcA?.transceive(selectMf)

            Timber.d("Tag response: ${response?.toHexString()}")

            nfcA?.close()

            getNfcTagFromIntent(Intent().apply {
                putExtra(NfcAdapter.EXTRA_TAG, tag)
            })
        } catch (e: Exception) {
            Timber.e(e, "Error communicating with NFC tag")
            null
        }
    }

    fun createNdefMessage(text: String): NdefMessage {
        val record = NdefRecord.createTextRecord("en", text)
        return NdefMessage(arrayOf(record))
    }

    fun createNdefUriMessage(uri: String): NdefMessage {
        val record = NdefRecord.createUri(uri)
        return NdefMessage(arrayOf(record))
    }

    fun writeNdefToTag(tag: Tag, message: NdefMessage): Boolean {
        return try {
            val ndef = Ndef.get(tag)
            ndef?.connect()
            ndef?.writeNdefMessage(message)
            ndef?.close()
            true
        } catch (e: Exception) {
            Timber.e(e, "Error writing NDEF message to tag")
            false
        }
    }
}

private fun ByteArray.toHexString(): String {
    return joinToString("") { "%02X".format(it) }
}
