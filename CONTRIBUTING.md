# Beitrag zu NFC Clone

Vielen Dank für Ihren Interesse an diesem Projekt! Hier sind Richtlinien für Beiträge.

## Code-Standard

### Kotlin-Stil

- Verwenden Sie die [Kotlin Official Style Guide](https://kotlinlang.org/docs/coding-conventions.html)
- 4 Leerzeichen Einrückung
- Max 120 Zeichen pro Zeile
- Aussagekräftige Variablen-Namen

```kotlin
// Gut
val nfcTagData = NfcManager.getNfcTagFromIntent(intent)

// Nicht gut
val d = NfcManager.getNfcTagFromIntent(intent)
```

### Dokumentation

- Schreiben Sie KDoc für public Funktionen
- Geben Sie Beispiele an

```kotlin
/**
 * Liest ein NFC-Tag und gibt seine Daten zurück.
 *
 * @param intent Intent mit NFC-Tag
 * @return NfcTagData oder null, falls Fehler
 * @throws IOException Wenn Tag nicht erreichbar
 *
 * @sample
 * val tag = NfcManager.getNfcTagFromIntent(intent)
 * println("Tag ID: ${tag?.id}")
 */
fun getNfcTagFromIntent(intent: Intent): NfcTagData?
```

### Commits

- Commit-Nachrichten sollten aussagekräftig sein
- Format: `[Type]: Beschreibung`

```
[Feature]: Add support for NDEF URI records
[Fix]: Correct tag ID parsing for NFC-F
[Docs]: Update installation guide
[Refactor]: Simplify APDU response handling
[Test]: Add unit tests for NfcManager
```

## Pull Request Prozess

1. **Fork** das Repository
2. **Feature Branch** erstellen
   ```bash
   git checkout -b feature/meine-neue-funktion
   ```
3. **Änderungen** durchführen
4. **Tests** schreiben (falls möglich)
5. **Commit** mit aussagekräftiger Nachricht
6. **Push** zum Fork
7. **Pull Request** zum Haupt-Repository

## PR-Anforderungen

- [ ] Code folgt den Style-Richtlinien
- [ ] Dokumentation ist aktualisiert
- [ ] Keine neuen Warnungen einführen
- [ ] Logcat-Ausgaben sind sauber (keine unerwarteten Fehler)

## Arten von Beiträgen

### Bug Fixes
- Beschreiben Sie den Bug deutlich
- Geben Sie Reproduktionsschritte an
- Include logcat-Fehler

### Neue Features
- Diskutieren Sie zuerst im Issue
- Implementieren Sie mit Tests
- Aktualisieren Sie die Dokumentation

### Dokumentation
- Verbessern Sie Klarheit und Genauigkeit
- Fügen Sie Beispiele hinzu
- Überprüfen Sie auf Tippfehler

### Tests
- Unit Tests für neue Funktionen
- Integration Tests für wichtige Workflows

## Berichterstattung von Bugs

Erstellen Sie einen Issue mit:

1. **Titel**: Kurze Beschreibung
2. **Umgebung**:
   - Android-Version
   - Gerät/Emulator
   - App-Version
3. **Schritte zur Reproduktion**:
   - Schritt 1
   - Schritt 2
   - Etc.
4. **Erwartetes Verhalten**
5. **Tatsächliches Verhalten**
6. **Logcat-Ausgabe**

```markdown
## Bug: Tags werden nicht gelesen

### Environment
- Android 12
- Samsung Galaxy S10
- App v1.0.0

### Steps to Reproduce
1. Öffne App
2. Wähle "NFC Tags lesen"
3. Drücke "Start"
4. Halte NFC-Tag an Gerät
5. Tag wird nicht erkannt

### Expected Behavior
Tag sollte gelesen werden und Informationen anzeigen

### Actual Behavior
App zeigt "Halte ein NFC-Tag an dein Gerät..."

### Logcat
```
E/NfcClone: Tag not discovered
```
```

## Fragen & Diskussionen

- GitHub Discussions für allgemeine Fragen
- GitHub Issues für Bugs und Features

## Lizenz

Durch Beitrag erklären Sie sich damit einverstanden, dass Ihr Code unter der MIT-Lizenz veröffentlicht wird.

---

Danke an all unsere Beitragenden! 🎉