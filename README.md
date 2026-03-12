# NFC Clone - Android App für NFC-Lesen und -Emulation

Eine vollständige Android-Anwendung zum Lesen von NFC-Tags und zum Emulieren von NFC-Cards mit Host Card Emulation (HCE).

## Features

- 📖 **NFC-Tags lesen** - Erfasse und dekodiere NFC-Tags
  - Auslesen von Tag-Informationen (ID, Typ, Technologien)
  - Dekodierung von NDEF-Nachrichten
  - Unterstützung für Text- und URI-Records
  
- 🎭 **NFC-Emulation** - Emuliere eine virtuelle NFC-Card
  - Host Card Emulation (HCE) für NFC-F
  - APDU-Befehlsverarbeitung
  - SELECT, READ BINARY und UPDATE machen BINARY-Befehle

- 🎨 **Moderne UI** - Mit Jetpack Compose erstellt
  - Material Design 3
  - Intuitive Bedienung
  - Echtzeit-Status-Updates

- 🔍 **Detaillierte Logs** - Timber-Integration für umfassende Debugging-Informationen

## Systemanforderungen

- **Android SDK:** API 21+
- **Target SDK:** API 34
- **Kotlin Version:** 1.9.22+
- **Gradle Version:** 8.2.0+
- **NFC-Hardware:** NFC-fähiges Gerät (nicht in Emulatoren verfügbar)

## Projektstruktur

```
app/
├── src/main/
│   ├── kotlin/com/example/nfcclone/
│   │   ├── MainActivity.kt                 # Hauptaktivität
│   │   ├── NFCCloneApplication.kt          # App-Konfiguration
│   │   ├── nfc/
│   │   │   ├── NfcManager.kt              # NFC-Hilfsfunktionen
│   │   │   ├── NfcTagData.kt              # Datenmodell für Tags
│   │   │   ├── NfcDiscoveryActivity.kt    # Tag-Discovery Handler
│   │   │   └── HostCardEmulationService.kt # HCE-Service
│   │   └── ui/
│   │       ├── screens/
│   │       │   └── HomeScreen.kt          # Hauptoberfläche
│   │       └── theme/
│   │           ├── Theme.kt               # Farben & Design
│   │           └── Type.kt                # Typografie
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml                # Texte
│   │   │   ├── colors.xml                 # Farben
│   │   │   └── themes.xml                 # Themes
│   │   └── xml/
│   │       ├── nfc_tech_filter.xml        # NFC-Technologie-Filter
│   │       ├── nfc_applet_config.xml      # HCE-Konfiguration
│   │       ├── backup_rules.xml           # Backup-Regeln
│   │       └── data_extraction_rules.xml  # Daten-Extraktions-Regeln
│   └── AndroidManifest.xml                # App-Manifest
└── build.gradle.kts                       # Gradle-Konfiguration
```

## Installation & Aufbau

### Voraussetzungen

- Android Studio 4.0+
- JDK 17+
- NFC-fähiges Android-Gerät

### Schritt für Schritt

1. **Repository klonen**
```bash
git clone https://github.com/Oberonskii/nfc-clone.git
cd nfc-clone
```

2. **Android Studio öffnen**
   - Projekt öffnen
   - Gradle-Sync durchführen
   - Build-Fehler beheben (falls erforderlich)

3. **Mit Gerät verbinden**
   - Android-Gerät über USB anschließen
   - USB-Debugging aktivieren
   - Gerät in Android Studio erkennen

4. **App kompilieren und starten**
```bash
./gradlew build
./gradlew installDebug
```

## Verwendung

### NFC-Tags lesen

1. App starten
2. "NFC Tags lesen" auswählen
3. "Start" antippen
4. NFC-Tag an die Rückseite des Geräts halten
5. Tag-Informationen werden angezeigt

### NFC emulieren

1. App starten
2. "NFC emulieren" auswählen
3. "Emulation starten" antippen
4. Die App emuliert jetzt eine NFC-Card
5. Mit einem anderen NFC-Lesegerät nähern

## NFC-Technologien

Die App unterstützt folgende NFC-Standards:

- **NFC Type A** - ISO/IEC 14443-3 Type A
- **NFC Type B** - ISO/IEC 14443-3 Type B
- **NFC Type F** - JIS X 6319-4
- **NFC Type V** - ISO/IEC 15693
- **NDEF** - NFC Data Exchange Format
- **ISO-DEP** - ISO/IEC 7816-4 compatible

## Abhängigkeiten

```kotlin
// AndroidX
androidx.core:core-ktx:1.12.0
androidx.activity:activity-ktx:1.8.1
androidx.lifecycle:lifecycle-runtime-ktx:2.7.0

// Jetpack Compose
androidx.compose.ui:ui:1.6.4
androidx.compose.material3:material3:1.2.0
androidx.activity:activity-compose:1.8.1

// NFC
androidx.nfc:nfc:1.1.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// Logging
com.jakewharton.timber:timber:5.0.1
```

## Berechtigungen

Die App erfordert folgende Berechtigungen:

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-feature android:name="android.hardware.nfc" android:required="true" />
<uses-feature android:name="android.hardware.nfc.hce" android:required="true" />
```

## API-Übersicht

### NfcManager

```kotlin
// Tag aus Intent auslesen
val tagData = NfcManager.getNfcTagFromIntent(intent)

// NDEF-Nachricht erstellen
val message = NfcManager.createNdefMessage("Hello World")

// URI-Nachricht erstellen
val uriMessage = NfcManager.createNdefUriMessage("https://example.com")

// In Tag schreiben
val success = NfcManager.writeNdefToTag(tag, message)
```

### HostCardEmulationService

Der HCE-Service verarbeitet APDU-Befehle:

- **SELECT (0xA4)** - Datei auswählen
- **READ BINARY (0xB0)** - Daten lesen
- **UPDATE BINARY (0xD0)** - Daten schreiben

## Entwicklung & Erweiterung

### Weitere NFC-Features hinzufügen

1. **Eigene NDEF-Record-Typen**
   - Im `NfcManager` neue Record-Handler hinzufügen

2. **APDU-Befehle erweitern**
   - Im `HostCardEmulationService` neue Befehle implementieren

3. **Weitere NFC-Technologien**
   - NfcV, TypeF-Unterstützung erweitern

## Debugging

Die App nutzt Timber für Logging. Alle wichtigen Operationen werden geloggt:

```
D NFC Clone Application started
D NFC Tag discovered
D Tag ID: 12345678ABCDEF
D Tag Type: Android UID
D Tag Technologies: IsoDep, NfcA, Ndef
```

## Häufige Probleme

### App erkennt NFC-Tag nicht
- Stelle sicher, dass NFC im Gerät aktiviert ist
- Halte das Tag länger an das Gerät
- Überprüfe die NFC-Antenne des Geräts

### HCE-Emulation funktioniert nicht
- Nicht alle Geräte unterstützen HCE
- Überprüfe `android.hardware.nfc.hce` im Manifest
- Verwende ein kompatibles Lesegerät

### Gradle-Build schlägt fehl
- `gradle --version` überprüfen (min. 8.2.0)
- `Invalidate Caches / Restart` in Android Studio
- `.gradle` und `build`-Ordner löschen

## Lizenz

MIT License - Siehe LICENSE Datei für Details

## Autor

Oberonskii

## Support & Beiträge

Fehler-Berichte und Pull Requests sind willkommen!

## Ressourcen

- [Android NFC Documentation](https://developer.android.com/guide/topics/connectivity/nfc)
- [NDEF Message Format](https://nfc-tools.org/index.php/Android_NDEF_Message_Format)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)

## Versionsverlauf

### v1.0.0 (Aktuell)
- ✅ NFC-Tag-Lesen implementiert
- ✅ Host Card Emulation (HCE) implementiert
- ✅ Material Design 3 UI
- ✅ NDEF-Dekodierung
- ✅ Timber-Logging

### Geplant für zukünftige Versionen
- Tag-Speicher in lokaler Datenbank
- Export/Import von Tag-Daten
- Erweiterte APDU-Verarbeitung
- NFC-P2P-Funktionalität
- Tag-Emulation mit benutzerdefinierten Daten