package com.example.nfcclone.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import timber.log.Timber

/**
 * Host Card Emulation (HCE) Service für NFC-F
 * Diese Service emuliert einen virtuellen NFC-Card und antwortet auf Befehle
 */
class HostCardEmulationService : HostApduService() {

    private val systemCode = "888B" // System Code für NFC-F

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Timber.d("APDU empfangen: ${commandApdu.toHexString()}")

        // APDU-Struktur analysieren
        if (commandApdu.size < 4) {
            Timber.w("APDU zu kurz: ${commandApdu.size}")
            return getSw(0x6700) // Invalid instruction length
        }

        val cla = commandApdu[0]
        val ins = commandApdu[1]
        val p1 = commandApdu[2]
        val p2 = commandApdu[3]

        // Beispiel: SELECT-Befehl (0xA4)
        if (ins == 0xA4.toByte()) {
            return handleSelectCommand(commandApdu)
        }

        // Beispiel: READ BINARY (0xB0)
        if (ins == 0xB0.toByte()) {
            return handleReadBinary(commandApdu)
        }

        // Beispiel: UPDATE BINARY (0xD0)
        if (ins == 0xD0.toByte()) {
            return handleUpdateBinary(commandApdu)
        }

        Timber.d("Unbekannter Befehl: CLA=$cla, INS=$ins")
        return getSw(0x6D00) // Instruction code not supported
    }

    private fun handleSelectCommand(commandApdu: ByteArray): ByteArray {
        Timber.d("SELECT Command")
        val response = byteArrayOf(
            0x61, 0x10, // Response: File control information
            0x80, 0x0E, // FCIs Tag
            0x00, 0x02, 0x00, 0x00, // ...
        )
        return response + getSw(0x9000) // Success
    }

    private fun handleReadBinary(commandApdu: ByteArray): ByteArray {
        Timber.d("READ BINARY Command")
        val data = "NFC Clone - Emulated Card".toByteArray()
        return data + getSw(0x9000)
    }

    private fun handleUpdateBinary(commandApdu: ByteArray): ByteArray {
        Timber.d("UPDATE BINARY Command")
        return getSw(0x9000) // Success
    }

    private fun getSw(code: Int): ByteArray {
        return byteArrayOf(
            ((code shr 8) and 0xFF).toByte(),
            (code and 0xFF).toByte()
        )
    }

    override fun onDeactivated(reason: Int) {
        Timber.d("Emulation deactivated. Reason: $reason")
    }
}

private fun ByteArray.toHexString(): String {
    return joinToString("") { "%02X".format(it) }
}
