# Beispiele & Rezepte

## Beispiel 1: NFC-Tag lesen und speichern

```kotlin
// In NfcManager.kt eine neue Methode hinzufügen
fun readAndStoreTag(tag: Tag): NfcTagData? {
    val tagData = getNfcTagFromIntent(
        Intent().apply { putExtra(NfcAdapter.EXTRA_TAG, tag) }
    ) ?: return null
    
    // Speichern Sie in Ihrer Datenbank
    // database.insert(tagData)
    
    return tagData
}
```

## Beispiel 2: Custom NDEF-Record erstellen

```kotlin
// Text mit Sprache
val record = NdefRecord.createTextRecord("de", "Hallo Welt")

// Mit Mehrfachsprachigkeit
val messages = NdefMessage(arrayOf(
    NdefRecord.createTextRecord("de", "Deutsch"),
    NdefRecord.createTextRecord("en", "English"),
    NdefRecord.createUri("https://example.com")
))
```

## Beispiel 3: APDU-Befehl für Authentifizierung

```kotlin
// In HostCardEmulationService.kt
private fun handleAuthenticate(commandApdu: ByteArray): ByteArray {
    val pin = "1234"
    val pinData = commandApdu.drop(5).toByteArray()
    
    return if (pinData.contentEquals(pin.toByteArray())) {
        byteArrayOf(0x61, 0x00) + getSw(0x9000)  // Success
    } else {
        getSw(0x6300)  // Wrong password
    }
}
```

## Beispiel 4: Tag-Daten visualisieren

```kotlin
// In HomeScreen.kt - Erweiterte TagDataDisplay
@Composable
fun AdvancedTagDisplay(tagData: NfcTagData) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Text("NFC Tag Details", style = MaterialTheme.typography.titleMedium)
            
            // ID (hex)
            SelectableText(
                stringResource(R.string.id) + ": " + tagData.id,
                modifier = Modifier.padding(8.dp)
            )
            
            // Technologien als Chips
            Row {
                tagData.technologies.forEach { tech ->
                    AssistChip(
                        onClick = {},
                        label = { Text(tech) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            
            // NDEF-Inhalt
            tagData.ndefMessage?.let {
                Divider()
                Text("NDEF Content", style = MaterialTheme.typography.labelSmall)
                SelectableText(it, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
```

## Beispiel 5: Room Database Integration

```kotlin
// TagEntity.kt
@Entity(tableName = "nfc_tags")
data class TagEntity(
    @PrimaryKey val id: String,
    val type: String,
    val technologies: String,
    val ndefMessage: String?,
    val timestamp: Long,
    val notes: String? = null
)

// TagDao.kt
@Dao
interface TagDao {
    @Insert
    suspend fun insert(tag: TagEntity)
    
    @Query("SELECT * FROM nfc_tags ORDER BY timestamp DESC")
    fun getAllTags(): Flow<List<TagEntity>>
    
    @Query("SELECT * FROM nfc_tags WHERE id = :id")
    suspend fun getTagById(id: String): TagEntity?
}
```

## Beispiel 6: ViewModel für Tag-Management

```kotlin
class NfcViewModel : ViewModel() {
    private val _tagData = MutableStateFlow<NfcTagData?>(null)
    val tagData = _tagData.asStateFlow()
    
    private val _lastError = MutableStateFlow<String?>(null)
    val lastError = _lastError.asStateFlow()
    
    fun processTag(intent: Intent) {
        try {
            val data = NfcManager.getNfcTagFromIntent(intent)
            _tagData.value = data
        } catch (e: Exception) {
            _lastError.value = e.message
        }
    }
    
    fun clearError() {
        _lastError.value = null
    }
}
```

## Beispiel 7: NFC-Tag-Schreib-Dialog

```kotlin
@Composable
fun WriteTagDialog(
    onDismiss: () -> Unit,
    onWrite: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tag schreiben") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Text") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = {
                onWrite(text)
                onDismiss()
            }) {
                Text("Schreiben")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}
```

## Beispiel 8: Tag-Emulation mit Daten

```kotlin
// In HostCardEmulationService
private var emulatedData = "Default Card Data"

private fun handleReadBinary(commandApdu: ByteArray): ByteArray {
    val offset = ((commandApdu[2].toInt() shl 8) or commandApdu[3].toInt())
    val length = commandApdu[4].toInt()
    
    val data = emulatedData.toByteArray()
    val response = data.slice(offset until minOf(offset + length, data.size)).toByteArray()
    
    return response + getSw(0x9000)
}

fun setEmulationData(data: String) {
    emulatedData = data
}
```

## Beispiel 9: Fehlerbehandlung beim Tag-Lesen

```kotlin
fun readTagSafely(tag: Tag): NfcTagData? {
    return try {
        val nfcA = NfcA.get(tag)
        nfcA.connect()
        
        try {
            // Lese Tag-Daten
            val response = nfcA.transceive(byteArrayOf(0x30, 0x00))
            Timber.d("Response: ${response?.toHexString()}")
        } finally {
            nfcA.close()
        }
        
        getNfcTagFromIntent(Intent().apply {
            putExtra(NfcAdapter.EXTRA_TAG, tag)
        })
    } catch (e: IOException) {
        Timber.e(e, "IO-Fehler beim Tag-Lesen")
        null
    } catch (e: Exception) {
        Timber.e(e, "Unerwarteter Fehler beim Tag-Lesen")
        null
    }
}
```

## Beispiel 10: Logging-Utility

```kotlin
object NfcLogger {
    fun logTagData(tag: NfcTagData) {
        Timber.d("=== Tag Information ===")
        Timber.d("ID: ${tag.id}")
        Timber.d("Type: ${tag.type}")
        Timber.d("Technologies: ${tag.technologies.joinToString(", ")}")
        Timber.d("NDEF: ${tag.ndefMessage ?: "None"}")
        Timber.d("Raw Data: ${tag.rawData.toHexString()}")
        Timber.d("======================")
    }
    
    fun logApduCommand(apdu: ByteArray) {
        Timber.d("APDU Command: ${apdu.toHexString()}")
        Timber.d("  CLA: 0x${apdu[0].toString(16)}")
        Timber.d("  INS: 0x${apdu[1].toString(16)}")
        Timber.d("  P1: 0x${apdu[2].toString(16)}")
        Timber.d("  P2: 0x${apdu[3].toString(16)}")
    }
}
```

Diese Beispiele können als Ausgangspunkt für Ihre eigenen Implementierungen verwendet werden!