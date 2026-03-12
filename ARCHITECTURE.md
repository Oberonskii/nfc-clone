# NFC Clone - Projektarchitektur

## Übersicht

Die Anwendung folgt einer sauberen **Schichten-Architektur** mit klarer Trennung von:

1. **Presentation Layer** - UI mit Jetpack Compose
2. **Business Logic Layer** - Manager und Helfer-Klassen
3. **Data Layer** - NFC APIs und Datenmodele
4. **Hardware Layer** - Android NFC System

```
┌─────────────────────────────────┐
│   Presentation (UI - Compose)   │
│  HomeScreen, ReadNfcScreen ...  │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  Business Logic (Managers)      │
│  NfcManager, ViewModels ...     │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  Data Layer (Models, Services)  │
│  NfcTagData, Services ...       │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  Hardware (Android NFC APIs)    │
│  NfcAdapter, Tag, APDU ...      │
└─────────────────────────────────┘
```

## Komponenten

### 1. Presentation Layer

**Verantwortung**: Benutzeroberfläche und Benutzerinteraktion

**Dateien**:
- `MainActivity.kt` - App-Einstiegspunkt, NFC-Setup
- `HomeScreen.kt` - Hauptbildschirm mit Tabs
- `Theme.kt` / `Type.kt` - Design-Tokens

**Funktionalität**:
```kotlin
// MainActivity initialisiert NFC und Compose
class MainActivity : ComponentActivity {
    override fun onResume() {
        nfcAdapter?.enableForegroundDispatch(...)
    }
}

// HomeScreen zeigt zwei Tabs
@Composable
fun HomeScreen(nfcAdapter: NfcAdapter?) {
    TabRow {
        Tab { ReadNfcScreen(...) }
        Tab { EmulateNfcScreen(...) }
    }
}
```

### 2. Business Logic Layer

**Verantwortung**: Business-Logic, Manager-Funktionen

**Dateien**:
- `NfcManager.kt` - Zentrale NFC-Verwaltung

**Funktionalität**:
```kotlin
object NfcManager {
    // Tag-Daten auslesen
    fun getNfcTagFromIntent(intent: Intent): NfcTagData?
    
    // NDEF-Nachrichten erstellen
    fun createNdefMessage(text: String): NdefMessage
    
    // In Tag schreiben
    fun writeNdefToTag(tag: Tag, message: NdefMessage): Boolean
}
```

### 3. Data Layer

**Verantwortung**: Datenmodele, Services, APIs

**Dateien**:
- `NfcTagData.kt` - Datenmodell für Tag-Informationen
- `NfcDiscoveryActivity.kt` - Tag-Discovery Handler
- `HostCardEmulationService.kt` - HCE-Emulation

**Datenmodell**:
```kotlin
data class NfcTagData(
    val id: String,              // Tag-ID
    val type: String,            // Tag-Typ
    val technologies: List<String>, // Technologien
    val ndefMessage: String?,    // NDEF-Inhalt
    val rawData: ByteArray       // Roh-Daten
)
```

### 4. Hardware Layer

**Verantwortung**: Gerätehardware-Zugriff

**Komponenten**:
- Android NFC System Services
- `NfcAdapter` - Gerät-NFC-Interface
- `Tag` - NFC-Tag-Objekt
- APDU-Interface

---

## Kommunikationsflüsse

### Tag-Lesen (Happy Path)

```
Physical NFC Tag
    ↓
[NfcAdapter.ACTION_TAG_DISCOVERED]
    ↓
NfcDiscoveryActivity empfängt Intent
    ↓
NfcManager.getNfcTagFromIntent()
    │
    ├─→ Extrahiere Tag-ID
    ├─→ Lese Technologien
    ├─→ Dekodiere NDEF
    │
    ↓
NfcTagData Objekt erstellt
    ↓
HomeScreen zeigt Daten an
    ↓
Nutzer sieht Tag-Informationen
```

### HCE-Emulation (Happy Path)

```
Externes NFC-Lesegerät
    ↓
[HostCardEmulationService aktiviert]
    ↓
processCommandApdu() aufgerufen
    │
    ├─→ Parse APDU (CLA, INS, P1, P2)
    ├─→ Rufe Handler auf (SELECT, READ, etc.)
    └─→ Erstelle Antwort (Daten + SW)
    ↓
Antwort an Lesegerät
    ↓
Lesegerät verarbeitet Antwort
```

---

## Design Patterns

### 1. Singleton Pattern

```kotlin
// NfcManager als Singleton für zentrale Verwaltung
object NfcManager {
    fun getNfcTagFromIntent(intent: Intent): NfcTagData?
    // ...
}
```

### 2. Service Pattern

```kotlin
// HostCardEmulationService für APDU-Verarbeitung
class HostCardEmulationService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray): ByteArray
}
```

### 3. Activity Pattern

```kotlin
// NfcDiscoveryActivity für Tag-Discovery
class NfcDiscoveryActivity : Activity() {
    override fun onNewIntent(intent: Intent) {
        handleNfcIntent(intent)
    }
}
```

### 4. Composable Pattern

```kotlin
// Compose für deklarative UI
@Composable
fun HomeScreen(nfcAdapter: NfcAdapter?) {
    // State, Layout, Navigation
}
```

---

## Datenfluss

### State Management

**UI State**:
```kotlin
var selectedTab by remember { mutableIntStateOf(0) }
var tagData by remember { mutableStateOf<NfcTagData?>(null) }
var isReading by remember { mutableStateOf(true) }
```

**Zukünftig**: ViewModel mit StateFlow für komplexere Apps.

---

## Abhängigkeitsdiagramm

```
MainActivity
    ↓
HomeScreen
    ├─→ ReadNfcScreen
    │   └─→ TagDataDisplay
    │
    └─→ EmulateNfcScreen

NfcDiscoveryActivity
    ↓
NfcManager
    └─→ NfcTagData

HostCardEmulationService
    └─→ APDU Handlers
```

---

## Threading Modell

### Main Thread
- UI-Operationen (Compose)
- Intent-Verarbeitung
- Foreground Dispatch

### Background Thread
- NDEF-Dekodierung (wenn länglich)
- APDU-Verarbeitung (HostApduService)
- Datenbankzugriffe (future)

---

## Fehlerbehandlung

### Try-Catch Pattern

```kotlin
try {
    val ndef = Ndef.get(tag)
    val message = ndef.cachedNdefMessage
    // ...
} catch (e: IOException) {
    Timber.e(e, "NFC Fehler")
} catch (e: Exception) {
    Timber.e(e, "Unerwarteter Fehler")
}
```

### Logging mit Timber

```kotlin
Timber.d("Debug: Tag gelesen")
Timber.e(exception, "Fehler: %s", message)
Timber.w("Warnung: Unbekannter Befehl")
```

---

## Zukünftige Architektur-Improvements

1. **MVVM Pattern**
   - ViewModels für State Management
   - LiveData / StateFlow für Reaktivität

2. **Dependency Injection**
   - Hilt für Dependency Injection
   - Testbarkeit verbessern

3. **Repository Pattern**
   - Abstraktion von Datenquellen
   - Room Database Integration

4. **Event Bus**
   - Entkopplung von Komponenten
   - SharedFlow für Events

```kotlin
// Zukünftiges MVVM-Pattern
class NfcViewModel : ViewModel() {
    private val _tagData = MutableStateFlow<NfcTagData?>(null)
    val tagData = _tagData.asStateFlow()
    
    fun processTag(intent: Intent) {
        viewModelScope.launch {
            _tagData.value = NfcManager.getNfcTagFromIntent(intent)
        }
    }
}
```

---

## Performance Überlegungen

1. **Foreground Dispatch** - Schnelllesen wenn App aktiv
2. **Coroutines** - Nicht-blockierende NDEF-Dekodierung
3. **Caching** - Wiederverwendung von NDEF-Dekodierungen
4. **APDU-Latenz** - < 1ms für HCE-Antworten angestrebt

---

## Sicherheit

- Validierung von NDEF-Records
- Validierung von APDU-Befehlen
- Exception-Handling ohne Crash
- Keine sensiblen Daten in Logs