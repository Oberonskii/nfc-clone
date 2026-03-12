# 📱 NFC Clone - Android App für NFC-Lesen und -Emulation

Eine vollständige, produktionsreife Android-App zum Lesen von NFC-Tags und zum Emulieren von NFC-Cards mit Host Card Emulation (HCE).

## 🚀 Features

✅ **NFC-Tags lesen** - Erfasse und dekodiere NFC-Tags
- Auslesen von Tag-Informationen (ID, Typ, Technologien)
- Dekodierung von NDEF-Nachrichten
- Unterstützung für Text- und URI-Records

✅ **NFC-Emulation** - Emuliere eine virtuelle NFC-Card
- Host Card Emulation (HCE) mit voller APDU-Verarbeitung
- SELECT, READ BINARY und UPDATE BINARY-Befehle
- Erweiterbar für Custom-Befehle

✅ **Moderne UI** - Mit Jetpack Compose erstellt
- Material Design 3
- Intuitive Bedienung
- Echtzeit-Status-Updates

✅ **Professionelle Qualität**
- Unit-Tests & Test-Setup
- Ausführliche Dokumentation
- Logging mit Timber
- GitHub-Ready (mit Actions etc.)

---

## 📁 Projektstruktur

```
nfc-clone/
├── 📄 README.md                    # Hauptdokumentation
├── 📄 QUICKSTART.md               # 5-Minuten-Anleitung
├── 📄 TECHNICAL.md                # Technische Details
├── 📄 ARCHITECTURE.md             # Architektur-Design
├── 📄 FAQ.md                      # Häufig Gestellte Fragen
├── 📄 EXAMPLES.md                 # Code-Beispiele
├── 📄 CONTRIBUTING.md             # Beitrags-Richtlinien
├── 📄 DEPLOYMENT.md               # Veröffentlichungs-Guide
├── 📄 CHANGELOG.md                # Versionsverlauf
├── 📄 LICENSE                     # MIT License
├── 📄 .gitignore                  # Git Excludes
├── 📄 .editorconfig               # Code-Stil-Konfiguration
│
├── 📦 build.gradle.kts            # Root Gradle
├── 📦 settings.gradle.kts         # Gradle Settings
├── 📦 gradle.properties           # Gradle Properties
│
└── 📂 app/                        # Android App Module
    ├── 📦 build.gradle.kts        # App Gradle Konfiguration
    ├── 📄 proguard-rules.pro      # ProGuard Optimierung
    │
    └── 📂 src/
        ├── 📂 main/
        │   ├── 📄 AndroidManifest.xml
        │   │
        │   ├── 📂 kotlin/com/example/nfcclone/
        │   │   ├── 📄 MainActivity.kt              # App-Einstiegspunkt
        │   │   ├── 📄 NFCCloneApplication.kt      # App-Konfiguration
        │   │   ├── 📂 nfc/
        │   │   │   ├── 📄 NfcManager.kt           # NFC-Verwaltung
        │   │   │   ├── 📄 NfcTagData.kt           # Tag-Datenmodell
        │   │   │   ├── 📄 NfcDiscoveryActivity.kt # Tag-Discovery
        │   │   │   └── 📄 HostCardEmulationService.kt # HCE Service
        │   │   └── 📂 ui/
        │   │       ├── 📂 screens/
        │   │       │   └── 📄 HomeScreen.kt       # Hauptbildschirm
        │   │       └── 📂 theme/
        │   │           ├── 📄 Theme.kt            # Farben & Design
        │   │           └── 📄 Type.kt             # Typografie
        │   │
        │   └── 📂 res/
        │       ├── 📂 values/
        │       │   ├── 📄 strings.xml             # Text-Ressourcen
        │       │   ├── 📄 colors.xml              # Farbe
        │       │   └── 📄 themes.xml              # Themes
        │       └── 📂 xml/
        │           ├── 📄 nfc_tech_filter.xml     # NFC-Filter
        │           ├── 📄 nfc_applet_config.xml   # HCE-Config
        │           ├── 📄 backup_rules.xml
        │           └── 📄 data_extraction_rules.xml
        │
        └── 📂 test/
            └── 📂 kotlin/com/example/nfcclone/nfc/
                ├── 📄 NfcManagerTest.kt           # NfcManager Tests
                └── 📄 NfcTagDataTest.kt           # NfcTagData Tests
```

---

## 🛠 Technologie-Stack

- **Sprache**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose 1.6.4
- **Design**: Material Design 3
- **Android API**: Target 34, Min. 21
- **NFC APIs**: Android NFC (androidx.nfc:nfc:1.1.0)
- **Coroutines**: Kotlinx Coroutines 1.7.3
- **Logging**: Timber 5.0.1
- **Build System**: Gradle 8.2.0
- **Testing**: JUnit 4, Mockito, Robolectric

---

## 🚫 Was ist NICHT enthalten

❌ Datenbank (Room) - für zukünftige Versionen geplant
❌ Cloud-Synchronisation - für Pro-Version geplant
❌ NFC-P2P - wird später implementiert
❌ Responsive UI für Tablets - wird später optimiert

---

## 📊 Dateiübersicht

| Kategorie | Dateien | Zeilen |
|-----------|---------|--------|
| Kotlin Source Code | 8 | ~1,200 |
| XML Resources | 8 | ~300 |
| Gradle Configs | 3 | ~180 |
| Documentation | 9 | ~2,500 |
| Tests | 2 | ~100 |
| **Total** | **30+** | **~4,280** |

---

## 📖 Dokumentation

### Für Anfänger
- 👉 **[QUICKSTART.md](QUICKSTART.md)** - 5-Minuten Einstieg

### Für Entwickler
- 👉 **[TECHNICAL.md](TECHNICAL.md)** - Technische Deep Dive
- 👉 **[ARCHITECTURE.md](ARCHITECTURE.md)** - App-Architektur
- 👉 **[EXAMPLES.md](EXAMPLES.md)** - Code-Beispiele

### Für Beiträge
- 👉 **[CONTRIBUTING.md](CONTRIBUTING.md)** - Beitrags-Richtlinien
- 👉 **[DEPLOYMENT.md](DEPLOYMENT.md)** - Veröffentlichungs-Guide

### Häufig gefragt
- 👉 **[FAQ.md](FAQ.md)** - Häufig Gestellte Fragen

---

## 🎯 Nächste Schritte

### 1. **Projekt öffnen**
```bash
git clone https://github.com/Oberonskii/nfc-clone.git
cd nfc-clone
# Android Studio öffnen
```

### 2. **Dokumentation lesen**
Beginnen Sie mit [QUICKSTART.md](QUICKSTART.md)

### 3. **App bauen & testen**
```bash
./gradlew installDebug
# NFC-Gerät anschließen und App starten
```

### 4. **Code erkunden**
Siehe [ARCHITECTURE.md](ARCHITECTURE.md) für Projekt-Übersicht

---

## 🎓 Lern-Ressourcen

- [Android NFC Developer Guide](https://developer.android.com/guide/topics/connectivity/nfc)
- [HCE Implementation](https://developer.android.com/guide/topics/connectivity/nfc/hce)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

---

## 📝 Lizenz

MIT License - Siehe [LICENSE](LICENSE) für Details

---

## 👤 Autor

**Oberonskii** - Entwickler

---

## 🤝 Beiträge willkommen

Sehen Sie [CONTRIBUTING.md](CONTRIBUTING.md) für Details

---

## 📧 Support

- 🐛 **Bugs melden**: GitHub Issues
- 💬 **Fragen**: GitHub Discussions
- 📚 **Dokumentation**: Siehe oben

---

✨ **Viel Spaß mit NFC Clone!** ✨