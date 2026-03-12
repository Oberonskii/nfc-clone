# Changelog

Alle wichtigen Änderungen an diesem Projekt werden in dieser Datei dokumentiert.

Das Format basiert auf [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
und dieses Projekt folgt [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-03-12

### Added
- ✅ NFC-Tag-Lesen
  - Tag-ID Auslesen (Hex-Format)
  - Tag-Typ und Technologien erkennen
  - NDEF-Nachricht dekodieren (Text & URI)
  
- ✅ NFC-Emulation (HCE)
  - Host Card Emulation für NFC-F implementiert
  - APDU-Befehle verarbeiten
  - SELECT, READ BINARY, UPDATE BINARY Befehle
  
- ✅ Benutzeroberfläche
  - Jetpack Compose UI
  - Material Design 3
  - Tab-Navigation (Lesen/Emulation)
  
- ✅ Dokumentation
  - Umfassende README.md
  - Technische Dokumentation (TECHNICAL.md)
  - Schnellstart-Anleitung (QUICKSTART.md)
  - Code-Beispiele (EXAMPLES.md)
  - Contributing Guide (CONTRIBUTING.md)

- ✅ Development Tools
  - Timber Logging Integration
  - Gradle Build-Konfiguration
  - ProGuard Rules

### Dependencies
- Kotlin 1.9.22
- Android API 34 target, API 21 minimum
- Jetpack Compose 1.6.4
- Material 3
- Coroutines 1.7.3
- Timber 5.0.1

---

## Geplant für zukünftige Versionen

### v1.1.0 - Daten-Persistierung
- [ ] Room Database für Tag-Speicherung
- [ ] Tag-Verlauf anzeigen
- [ ] Favoriten/Labels
- [ ] Export als JSON/CSV

### v1.2.0 - Erweiterte NFC
- [ ] NFC-P2P Support
- [ ] Android Beam Integration
- [ ] Custom NDEF-Record-Typen
- [ ] Tag-Schreib-Funktionalität

### v1.3.0 - Erweiterte Emulation
- [ ] ISO/IEC 7816-4 Kommandos
- [ ] User-definierte APDU-Befehle
- [ ] Mehrsprachigen Emulation
- [ ] Authentifizierung

### v2.0.0 - Pro Features
- [ ] Cloud-Synchronisation
- [ ] Tag-Templates
- [ ] Batch-Operationen
- [ ] Erweiterte Statistiken
- [ ] Dark Mode

---

## Format-Erklärung

- **Added** - Neue Funktionen
- **Changed** - Änderungen an bestehenden Funktionen
- **Deprecated** - Kommende Entfernung
- **Removed** - Entfernte Funktionen
- **Fixed** - Fehlerkorrektionen
- **Security** - Sicherheitsupdates