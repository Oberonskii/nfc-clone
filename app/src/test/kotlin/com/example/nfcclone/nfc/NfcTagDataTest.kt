package com.example.nfcclone.nfc

import org.junit.Test
import kotlin.test.assertEquals

class NfcTagDataTest {

    @Test
    fun testNfcTagDataCreation() {
        val tagData = NfcTagData(
            id = "12345678",
            type = "Android UID",
            technologies = listOf("IsoDep", "NfcA"),
            ndefMessage = "Hello",
            rawData = byteArrayOf(0x01, 0x02, 0x03)
        )

        assertEquals("12345678", tagData.id)
        assertEquals("Android UID", tagData.type)
        assertEquals(2, tagData.technologies.size)
        assertEquals("Hello", tagData.ndefMessage)
    }

    @Test
    fun testNfcTagDataEquality() {
        val tag1 = NfcTagData(
            id = "ABC123",
            type = "Type A",
            technologies = listOf("NfcA"),
            ndefMessage = "Test"
        )

        val tag2 = NfcTagData(
            id = "ABC123",
            type = "Type A",
            technologies = listOf("NfcA"),
            ndefMessage = "Test"
        )

        assertEquals(tag1, tag2)
    }

    @Test
    fun testNfcTagDataWithoutNdef() {
        val tagData = NfcTagData(
            id = "XYZ789",
            type = "Type B",
            technologies = listOf("NfcB"),
            ndefMessage = null
        )

        assertEquals(null, tagData.ndefMessage)
    }
}
