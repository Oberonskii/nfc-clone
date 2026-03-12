package com.example.nfcclone.nfc

data class NfcTagData(
    val id: String,
    val type: String,
    val technologies: List<String>,
    val ndefMessage: String? = null,
    val rawData: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NfcTagData

        if (id != other.id) return false
        if (type != other.type) return false
        if (technologies != other.technologies) return false
        if (ndefMessage != other.ndefMessage) return false
        if (!rawData.contentEquals(other.rawData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + technologies.hashCode()
        result = 31 * result + (ndefMessage?.hashCode() ?: 0)
        result = 31 * result + rawData.contentHashCode()
        return result
    }
}
