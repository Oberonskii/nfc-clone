# Deployment & Veröffentlichung

## Lokal Testen

### Build & Installation

```bash
# Debug Build
./gradlew installDebug
adb shell am start -n com.example.nfcclone/.MainActivity

# Release Build (Test-Signing)
./gradlew installRelease
```

### Automatisches Neuladen

```bash
# Watch-Mode für automatisches Rebuild
./gradlew installDebug --watch
```

---

## Release Build

### 1. Version Aktualisieren

**In `app/build.gradle.kts`:**

```kotlin
defaultConfig {
    versionCode = 2  // Erhöhen
    versionName = "1.1.0"  // Semantic Versioning
}
```

**In `CHANGELOG.md`:**

```markdown
## [1.1.0] - 2026-03-20

### Added
- Neue Features...

### Fixed
- Fehlerkorrektionen...
```

### 2. Release Build erstellen

```bash
# Bundle für Google Play
./gradlew bundleRelease

# APK für Sideloading
./gradlew assembleRelease

# Output:
# - app/build/outputs/bundle/release/app-release.aab
# - app/build/outputs/apk/release/app-release.apk
```

### 3. Signieren

**Keystore erstellen** (einmalig):

```bash
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias nfc-clone-key
```

**Gradle konfigurieren** (in `local.properties`):

```properties
storeFile=release.keystore
storePassword=<password>
keyAlias=nfc-clone-key
keyPassword=<password>
```

Dann:
```bash
./gradlew bundleRelease  # Automatisch signiert
```

---

## Google Play Store

### 1. App konfigurieren

**In `app/build.gradle.kts`:**

```kotlin
android {
    namespace = "com.example.nfcclone"
    compileSdk = 34
}
```

### 2. Play Store Einträge

- **Appname**: "NFC Clone"
- **Kurzbeschreibung**: "NFC Tags lesen und emulieren"
- **Beschreibung**: (siehe README.md)
- **Kategorie**: Dienstprogramme
- **Altersfreigabe**: 4+

### 3. Screenshots

Benötigt:
- 2-8 Screenshots pro Sprache
- Größe: 1080x1920 Pixel
- Format: PNG

### 4. Upload

```bash
# In Android Studio:
# Build → Generate Signed Bundle/APK → Release
# → Dem Wizard folgen
# → App Bundle hochladen im Play Console
```

---

## F-Droid

### 1. Metadaten erstellen

**`metadata/en-US/fdroid/metadata.txt`:**

```
Name: NFC Clone
Summary: Read and emulate NFC cards
Description:
A full-featured Android app for reading NFC tags and emulating
NFC cards with Host Card Emulation (HCE).

License: MIT
Web Site: https://github.com/Oberonskii/nfc-clone
Source Code: https://github.com/Oberonskii/nfc-clone
Issue Tracker: https://github.com/Oberonskii/nfc-clone/issues
```

### 2. Build-Konfiguration

**Siehe**: F-Droid's [Metadata Format](https://f-droid.org/docs/Build_Metadata_Reference/)

### 3. Einreichen

- F-Droid Pull Request erstellen
- Oder zu F-Droid Admin-Team kontaktieren

---

## GitHub Releases

### 1. Tag erstellen

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### 2. Release Generator (GitHub Actions)

```yaml
# .github/workflows/release.yml
name: Release

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: gradle/gradle-build-action@v2
      - run: ./gradlew bundleRelease
      - uses: softprops/action-gh-release@v1
        with:
          files: app/build/outputs/**/*.aab
```

---

## Continuous Integration (GitHub Actions)

### Automatisiertes Testen

```yaml
# .github/workflows/ci.yml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: 17
      - run: ./gradlew build test lint
```

---

## Versionierungs-Schema

Folgt [Semantic Versioning](https://semver.org/):

```
MAJOR.MINOR.PATCH

v1.0.0    - Initial Release
v1.0.1    - Bugfix
v1.1.0    - Minor Feature
v2.0.0    - Breaking Changes
```

---

## Checkliste vor Release

- [ ] Alle Tests bestehen: `./gradlew test`
- [ ] Lint-Fehler korrigiert: `./gradlew lint`
- [ ] README.md aktualisiert
- [ ] CHANGELOG.md aktualisiert
- [ ] Version in Gradle erhöht
- [ ] Git Tag erstellt
- [ ] Release Notes geschrieben
- [ ] Screenshots aktualisiert (falls UI-Änderungen)
- [ ] Privacy Policy aktualisiert (falls nötig)

---

## Post-Release

### Ankündigung

- [ ] GitHub Release erstellen
- [ ] Tweet/Social Media
- [ ] Dokumentation aktualisieren

### Monitoring

```bash
# Crashes überwachen
adb logcat | grep FATAL

# User Reports überprüfen
# - GitHub Issues
# - Play Store Reviews
```

---

## Rollback

Falls kritischer Bug gefunden:

```bash
# Vorherige Version taggen
git tag -a v1.0.1-hotfix -m "Hotfix for critical bug"
git push origin v1.0.1-hotfix

# Bugfix durchführen
# ...
# v1.0.2 releasen
```

---

## Distribution-Kanäle

| Kanal | Prozess | Zielgruppe |
|-------|---------|-----------|
| **Debug APK** | adb install | Entwickler |
| **Google Play** | Play Console | Alle Android-Nutzer |
| **F-Droid** | Community Review | FOSS-Nutzer |
| **GitHub Releases** | Direct Download | Entwickler/Enthusiasten |
| **Amazon Appstore** | Developer Portal | Kindle Fire Nutzer |

---

## Tipps & Best Practices

1. **Beta-Testing** vor Release
   - Closed Beta in Play Store
   - Feedback von Testern einholen

2. **Graduelle Rollouts**
   - Play Store: 10% → 25% → 50% → 100%
   - Zeit zum Überwachen von Crashes

3. **User Feedback**
   - Ratings & Reviews monitoren
   - Bug Reports schnell beheben

4. **Documentation**
   - Release Notes für jede Version
   - Changelog aktuell halten

5. **Kommunikation**
   - User über Breaking Changes informieren
   - Upgrade-Pfade dokumentieren