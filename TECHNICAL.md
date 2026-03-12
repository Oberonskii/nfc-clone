# Technische Dokumentation - NFC Clone App

## Architektur-Übersicht

Die App folgt einer sauberen Architektur mit folgenden Layern:

```
UI Layer (Compose)
        ↓
Business Logic Layer (ViewModels/Managers)
        ↓
Data Layer (NFC APIs, Manager-Utilities)
        ↓
Hardware Layer (Android NFC System)
```

## NFC-Kommunikation

### NFC-Tag-Lesen

**Ablauf:**

1. **Intent-Empfang** (`NfcDiscoveryActivity.kt`)
   - System sendet `ACTION_TAG_DISCOVERED` oder `ACTION_TECH_DISCOVERED`
   
2. **Tag-Parsing** (`NfcManager.getNfcTagFromIntent()`)
   - Extrahiere Tag-ID (Hex-String)
   - Lese verfügbare Technologien
   - Dekodiere NDEF-Nachrichten
   
3. **NDEF-Dekodierung**
   - Text-Records (RTD_TEXT)
   - URI-Records (RTD_URI)
   - Custom-Records

**Beispiel-Code:**

```kotlin
val tagData = NfcManager.getNfcTagFromIntent(intent)
println("Tag ID: ${tagData.id}")
println("Technologien: ${tagData.technologies}")
println("NDEF: ${tagData.ndefMessage}")
```

### Host Card Emulation (HCE)

**Ablauf:**

1. **Service-Registrierung** (`HostCardEmulationService.kt`)
   - System erkennt App als NFC-Card-Emulator
   
2. **APDU-Verarbeitung**
   - `processCommandApdu()` wird aufgerufen
   - Befehl wird geparst (CLA, INS, P1, P2)
   - Entsprechende Handler werden aufgerufen
   
3. **APDU-Antwort**
   - Daten + Status Word (SW) senden
   - SW 0x9000 = Erfolg
   - SW 0x6700 = Fehler

**APDU-Befehl-Struktur:**

```
CLA | INS | P1 | P2 | [Lc] | [Data] | [Le]

CLA: Class of instruction
INS: Instruction code
P1/P2: Parameters
Lc: Length of command data
Data: Kommando-Daten
Le: Expected response length
```

**Implementierte Befehle:**

| INS | Befehl | Funktion |
|-----|--------|----------|
| 0xA4 | SELECT | Datei/Anwendung auswählen |
| 0xB0 | READ BINARY | Daten aus Speicher lesen |
| 0xD0 | UPDATE BINARY | Daten in Speicher schreiben |

## Wichtige Klassen

### NfcManager

```kotlin
object NfcManager {
    // Tag-Daten aus Intent extrahieren
    fun getNfcTagFromIntent(intent: Intent): NfcTagData?
    
    // Mit NFC-Tag kommunizieren
    fun readNfcTag(tag: Tag): NfcTagData?
    
    // NDEF-Nachrichten erstellen
    fun createNdefMessage(text: String): NdefMessage
    fun createNdefUriMessage(uri: String): NdefMessage
    
    // In Tag schreiben
    fun writeNdefToTag(tag: Tag, message: NdefMessage): Boolean
}
```

### NfcTagData

```kotlin
data class NfcTagData(
    val id: String,              // Hex-String der Tag-ID
    val type: String,            // Tag-Typ (z.B. "Android UID")
    val technologies: List<String>, // NFC-Technologien
    val ndefMessage: String?,    // Dekodierte NDEF-Nachricht
    val rawData: ByteArray       // Roh-Tag-Daten
)
```

### HostCardEmulationService

```kotlin
open class HostCardEmulationService : HostApduService {
    // In dieser Methode APDU-Befehle verarbeiten
    open fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray
    
    // Wenn Emulation beendet wird
    open fun onDeactivated(reason: Int)
}
```

## Android-Manifest-Einträge

### NFC-Permissions

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-feature android:name="android.hardware.nfc" android:required="true" />
<uses-feature android:name="android.hardware.nfc.hce" android:required="true" />
```

### Intent-Filter

```xml
<!-- NFC Tag Discovery -->
<intent-filter>
    <action android:name="android.nfc.action.TECH_DISCOVERED" />
    <category android:name="android.intent.category.DEFAULT" />
</intent-filter>

<!-- HCE Service -->
<service android:name=".nfc.HostCardEmulationService">
    <intent-filter>
        <action android:name="android.nfc.cardemulation.action.HOST_APDU_SERVICE" />
    </intent-filter>
</service>
```

## Datenflusss

### Tag-Leseprozess

```
Physischer NFC-Tag
    ↓
Android NFC-System erkennt Tag
    ↓
Intent mit TAG_DISCOVERED/TECH_DISCOVERED wird gesendet
    ↓
NfcDiscoveryActivity empfängt Intent
    ↓
NfcManager parst Tag-Daten
    ↓
NfcTagData wird erstellt
    ↓
UI wird aktualisiert (Compose)
    ↓
Nutzer sieht Tag-Informationen
```

### Emulations-Prozess

```
Externes NFC-Lesegerät
    ↓
Android NFC-System leert Lesegerät
    ↓
HostCardEmulationService wird aktiviert
    ↓
processCommandApdu() wird aufgerufen
    ↓
APDU-Befehl wird geparst
    ↓
Entsprechender Handler wird aufgerufen (SELECT, READ, etc.)
    ↓
Antwort (Daten + SW) wird gesendet
    ↓
Externes Lesegerät empfängt Antwort
```

## Debug-Logging

Die App verwendet Timber für strukturiertes Logging:

```kotlin
Timber.d("Tag gelesen: ${tagData.id}")
Timber.e(exception, "Fehler beim NFC-Zugriff")
Timber.w("Warnung: Unbekannter APDU-Befehl")
```

Alle Logs erscheinen in Android Studio Logcat.

## Sicherheitsüberlegungen

1. **NDEF-Dekodierung**
   - Validiere alle NDEF-Records vor Verarbeitung
   - Behandle ungültige Daten mit Exception-Handling

2. **APDU-Verarbeitung**
   - Validiere Eingabe-Array-Größe
   - Überprüfe Befehls-Struktur
   - Sende korrekte Status Words (SW)

3. **Permissions**
   - NFC-Permission ist erforderlich
   - Kann zur Laufzeit nicht angefordert werden (normale Permission)

## Performance-Tipps

1. **Foreground Dispatch**
   - Ermöglicht schnelles Tag-Lesen in Foreground
   - Wird in `MainActivity.onResume()` aktiviert

2. **NDEF-Dekodierung**
   - Nutze Coroutines für lange Operationen
   - Verhindere UI-Blockierung

3. **HCE-Service**
   - Leicht (kleine APDU-Antworten)
   - Sollte <= 1ms Latenz haben

## Tested Devices

- Samsung Galaxy S10+
- OnePlus 8Pro
- Pixel 6

(Unterschiedliche Geräte können unterschiedliche NFC-Fähigkeiten haben)