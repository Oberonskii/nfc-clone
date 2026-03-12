# Häufig Gestellte Fragen (FAQ)

## Installation & Setup

### F: Ich kann die App nicht installieren. Was ist das Problem?

A: Überprüfen Sie folgende Punkte:
1. Android SDK 21+ ist installiert (`adb --version` ausführen)
2. Gradle ist aktualisiert (`./gradlew --version` sollte 8.2.0+ zeigen)
3. USB-Debugging ist auf dem Gerät aktiviert
4. Das Gerät wird von `adb devices` erkannt

Falls weiterhin Probleme:
```bash
./gradlew clean
./gradlew build
```

### F: Gradle-Sync schlägt fehl

A: Probieren Sie:
```bash
# Gradle-Cache leeren
rm -rf ~/.gradle/caches

# Neu builden
./gradlew clean build
```

In Android Studio: `File → Invalidate Caches → Restart`

### F: Kann ich die App auf einem Emulator testen?

A: **Nein, NFC funktioniert nicht im Emulator.** Sie benötigen ein echtes Gerät mit NFC-Hardware.

---

## NFC-Funktionalität

### F: Warum werden meine NFC-Tags nicht erkannt?

A: 
1. **NFC aktivieren** - Einstellungen → Wireless & networks → NFC
2. **App starten** - MainActivity muss laufen
3. **Tab wählen** - "NFC Tags lesen" wählen
4. **Start antippen** - Button drücken
5. **Tag halten** - 3-5 Sekunden an der Rückseite halten

Falls immer noch nichts:
- Überprüfen Sie `adb logcat | grep NFC`
- Probieren Sie ein anderes NFC-Tag

### F: Kann ich ISO/IEC 7816-4 Tags lesen?

A: Die App unterstützt derzeit:
- NDEF-Tags
- NFC-A, NFC-B, NFC-F, NFC-V
- IsoDep (teilweise)

Für vollständige ISO/IEC 7816-4 ist zusätzlicher Code erforderlich.

### F: Wie kann ich Daten in ein Tag schreiben?

A: Nutzen Sie `NfcManager.writeNdefToTag()`:

```kotlin
val message = NfcManager.createNdefMessage("Neue Daten")
val success = NfcManager.writeNdefToTag(tag, message)
if (success) {
    println("Erfolgreich geschrieben!")
}
```

### F: Was ist der Unterschied zwischen NFC A, B, F, V?

A:
- **NFC Type A** - ISO14443-A (Samsung, Android)
- **NFC Type B** - ISO14443-B (bestimmte Systeme)
- **NFC Type F** - FeliCa (Japan, Public Transport)
- **NFC Type V** - ISO15693 (Long-Range)

---

## Emulation

### F: Wie nutze ich die NFC-Emulation?

A: 
1. App starten
2. "NFC emulieren" Tab
3. "Emulation starten" antippen
4. Mit anderem NFC-Lesegerät nähern
5. Daten werden emuliert

**Wichtig**: Nicht alle Geräte unterstützen HCE (Host Card Emulation).

### F: Kann ich benutzerdefinierte Befehle emulieren?

A: Ja! Ändern Sie `HostCardEmulationService.kt`:

```kotlin
if (ins == 0xXX.toByte()) {
    return handleMyCommand(commandApdu)
}
```

### F: Welche APDU-Befehle werden unterstützt?

A: Derzeit:
- **0xA4** - SELECT (Datei wählen)
- **0xB0** - READ BINARY (Daten lesen)
- **0xD0** - UPDATE BINARY (Daten schreiben)

Weitere können hinzugefügt werden.

---

## Entwicklung

### F: Wie kann ich Logs anschauen?

A: Mit Android Studio Terminal:
```bash
adb logcat | grep -i nfc
```

Oder mit Filterung nach App:
```bash
adb logcat | grep nfcclone
```

### F: Kann ich Timber-Logs in Dateien speichern?

A: Ja, mit CustomFileTree:

```kotlin
val file = File(cacheDir, "nfc_logs.txt")
Timber.plant(Timber.DebugTree())
// + Custom File Logging
```

### F: Wie schreibe ich Unit-Tests?

A: Siehe `app/src/test/kotlin/`. Beispiel:

```kotlin
@Test
fun testNfcTagCreation() {
    val tag = NfcTagData("ID", "Type", listOf("NfcA"))
    assertEquals("ID", tag.id)
}
```

Führen Sie Tests aus:
```bash
./gradlew test
```

### F: Kann ich die App für F-Droid verpacken?

A: Ja, es ist Open Source (MIT). Für F-Droid:
1. Metadaten in `metadata/` hinzufügen
2. Reproduzierbar bauen
3. F-Droid Pull Request

---

## Sicherheit

### F: Ist meine Datennutzung sicher?

A: Die App:
- Sendet keine Daten external
- Braucht keine Internet-Permission
- Speichert Daten lokal

**Hinweis**: Achten Sie auf NFC-Sicherheit! NFC kann "gehackt" werden.

### F: Kann ich Tags verschlüsseln?

A: NDEF bietet keine native Verschlüsselung. Sie können aber:
1. Daten vor dem Schreiben verschlüsseln
2. Custom APDU-Befehle mit Authentifizierung schreiben

---

## Performance

### F: Warum ist das Tag-Lesen langsam?

A: NDEF-Dekodierung kann Zeit brauchen. Optimierungen:
- Coroutines verwenden
- UI nicht blocken
- NDEF auf dem Hintergrund-Thread dekodieren

### F: Kann ich mehrere Tags gleichzeitig lesen?

A: Nein, der NFC-Adapter ermöglicht nur ein Tag zur Zeit.

---

## Bekannte Probleme

### Tags werden nach Update nicht erkannt

**Behebung:**
```bash
adb uninstall com.example.nfcclone
./gradlew installDebug
```

### HCE funktioniert nicht auf bestimmtem Gerät

**Überprüfen Sie:**
```bash
adb shell getprop | grep nfc
```

Falls `android.hardware.nfc.hce: false` → Gerät unterstützt HCE nicht.

### App crasht beim Emulation starten

**Logcat überprüfen:**
```bash
adb logcat | grep "AndroidRuntime"
```

Posten Sie den Fehler im GitHub Issue.

---

## Feature-Anfragen

### Kann ich ein neues Feature vorschlagen?

A: Ja! Öffnen Sie ein GitHub Issue mit:
- Beschreibung
- Use Case
- Beispiel-Code (optional)

Siehe `CONTRIBUTING.md` für Details.

---

## Support

Haben Sie noch Fragen?

1. 📖 Lesen Sie `README.md` und `TECHNICAL.md`
2. 🚀 Siehe `QUICKSTART.md` für schnelle Hilfe
3. 💬 Öffnen Sie ein GitHub Issue
4. 👥 Diskutieren Sie in GitHub Discussions

**Vielen Dank für die Nutzung von NFC Clone!** 🎉