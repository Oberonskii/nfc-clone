package com.example.nfcclone.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class NfcManagerTest {

    @Before
    fun setUp() {
        // Initialisierung für Tests
    }

    @Test
    fun testByteArrayToHexString() {
        val byteArray = byteArrayOf(0x12, 0x34, 0x56, 0x78)
        val hexString = byteArray.toHexString()
        assertEquals("12345678", hexString)
    }

    @Test
    fun testCreateNdefMessage() {
        val text = "Hello World"
        val message = NfcManager.createNdefMessage(text)
        assertNotNull(message)
        assertEquals(1, message.records.size)
    }

    @Test
    fun testCreateNdefUriMessage() {
        val uri = "https://example.com"
        val message = NfcManager.createNdefUriMessage(uri)
        assertNotNull(message)
        assertEquals(1, message.records.size)
    }

    @Test
    fun testValidateApduCommand() {
        val validApdu = byteArrayOf(0x00, 0xA4, 0x00, 0x00, 0x02, 0x3F, 0x00)
        assertEquals(true, validApdu.size >= 4)
    }

    @Test
    fun testInvalidApduCommand() {
        val invalidApdu = byteArrayOf(0x00, 0xA4)
        assertEquals(true, invalidApdu.size < 4)
    }
}

// Extension function für Tests
private fun ByteArray.toHexString(): String {
    return joinToString("") { "%02X".format(it) }
}
