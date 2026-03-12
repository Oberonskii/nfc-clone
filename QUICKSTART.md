# Schnellstart-Anleitung

## 5-Minuten Installation

### 1. Projekt klonen

```bash
git clone https://github.com/Oberonskii/nfc-clone.git
cd nfc-clone
```

### 2. Android Studio öffnen

```bash
android-studio .
```

oder über die GUI:
- File → Open → nfc-clone-Ordner wählen

### 3. Projekt konfigurieren

- Android Studio wartet auf Gradle-Sync
- Warten Sie, bis der Sync abgeschlossen ist
- Falls Fehlermeldungen: `File → Invalidate Caches → Restart`

### 4. Gerät verbinden

```bash
adb devices
```

Ausgabe sollte Ihr Gerät zeigen:
```
List of attached devices
ABC123DEF456           device
```

Falls nicht:
- USB-Debugging in Einstellungen aktivieren
- USB-Kabel anschließen

### 5. App starten

**Option A: Android Studio**
- Run → Run 'app'
- Oder Play-Button oben rechts drücken

**Option B: Terminal**
```bash
./gradlew installDebug
adb shell am start -n com.example.nfcclone/.MainActivity
```

## Erste Schritte mit der App

### NFC-Tag lesen

1. **App starten** - Home-Bildschirm wird angezeigt
2. **"NFC Tags lesen" Tab wählen**
3. **"Start" antippen** - App wartet auf Tag
4. **NFC-Tag an Rückseite halten** - etwa 3-5 Sekunden
5. **Tag-Info anschauen** - ID, Typ, Technologien werden angezeigt

### NFC emulieren

1. **"NFC emulieren" Tab wählen**
2. **"Emulation starten" antippen** - Service wird aktiviert
3. **Anderes NFC-Lesegerät verwenden** - Zum Lesen der emulierten Card
4. **Status wird angezeigt** - "Aktiv" zeigt emulation an

## Troubleshooting

### `Gradle sync failed`

Ursache: Gradle-Version stimmt nicht

Lösung:
```bash
./gradlew wrapper --gradle-version 8.2.0
```

### `error: cannot find symbol`

Ursache: Kotlin-Symbol nicht bekannt

Lösung:
```bash
./gradlew clean
./gradlew build
```

### `NFC wird nicht erkannt`

Ursache: NFC ist deaktiviert oder nicht verfügbar

Lösung:
- Einstellungen → Wireless & networks → NFC → Aktivieren
- Überprüfen Sie, ob Gerät NFC hat: `adb shell getprop | grep nfc`

### `Tag werden nicht gelesen`

Ursache: Foreground Dispatch ist nicht aktiviert

Lösung:
- Neustart der App
- Überprüfen Sie logcat: `adb logcat | grep NFC`

### `App crashed beim Starten`

Ursache: Permission-Fehler oder Manifest-Problem

Lösung:
```bash
adb logcat | head -100
# Fehler suchen und beheben
./gradlew build
```

## Nützliche Befehle

```bash
# App kompilieren
./gradlew build

# App installieren & starten
./gradlew installDebug
adb shell am start -n com.example.nfcclone/.MainActivity

# Logs anschauen
adb logcat | grep "NFC\|nfcclone"

# App deinstallieren
adb uninstall com.example.nfcclone

# APK bauen (Release)
./gradlew bundleRelease

# Gradle-Tasks anzeigen
./gradlew tasks
```

## Entwicklung

### Code-Struktur verstehen

```
MainActivity.kt          - App-Einstiegspunkt
  ↓
HomeScreen.kt          - UI mit Tabs
  ├→ ReadNfcScreen     - Tag-Lesen UI
  └→ EmulateNfcScreen  - Emulations-UI
  
NfcManager.kt          - NFC-Logik
  ├→ getNfcTagFromIntent()    - Tag-Dekodierung
  ├→ createNdefMessage()      - NDEF erstellen
  └→ writeNdefToTag()         - In Tag schreiben

HostCardEmulationService.kt - HCE-Emulation
  ├→ processCommandApdu() - APDU verarbeiten
  ├→ handleSelectCommand() - SELECT-Befehl
  ├→ handleReadBinary()   - READ-Befehl
  └→ handleUpdateBinary() - UPDATE-Befehl
```

### Einen neuen APDU-Befehl hinzufügen

1. Öffne `HostCardEmulationService.kt`
2. Eine neue Zeile in `processCommandApdu()` hinzufügen:

```kotlin
if (ins == 0xXX.toByte()) {
    return handleMyCommand(commandApdu)
}
```

3. Handler implementieren:

```kotlin
private fun handleMyCommand(commandApdu: ByteArray): ByteArray {
    Timber.d("MY Command")
    val response = "Hello".toByteArray()
    return response + getSw(0x9000)
}
```

## Weitere Ressourcen

- [Android NFC Developer Guide](https://developer.android.com/guide/topics/connectivity/nfc)
- [HCE Implementierung](https://developer.android.com/guide/topics/connectivity/nfc/hce)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose/documentation)

## Support

Falls Sie Probleme haben:

1. Überprüfen Sie die logcat-Ausgabe
2. Siehe `TECHNICAL.md` für technische Details
3. Öffnen Sie ein Issue auf GitHub