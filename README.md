# Attendance Manager

A complete native Android application for managing attendance records for approximately 110 members.

## Features

- **Dashboard**: Overview of attendance statistics
- **Member Management**: Add, edit, search, and manage member information
- **Daily Attendance**: Fast attendance marking suitable for 110+ members
- **Holiday Management**: Date-level holiday settings
- **Absentee List**: View and share daily absentees
- **Monthly Reports**: Generate attendance reports
- **Individual Member History**: Track attendance history per member
- **WhatsApp Sharing**: Share absentee lists via WhatsApp
- **Backup & Restore**: Local backup and restore functionality
- **Offline First**: Works completely offline without internet
- **Data Safety**: Uses Member IDs for attendance tracking, not names

## Technology Stack

- **Language**: Java/Kotlin
- **Database**: SQLite (local persistent storage)
- **UI**: Android Material Design
- **Build System**: Gradle
- **Minimum SDK**: API 21 (Android 5.0)
- **Target SDK**: API 34 (Android 15)

## Building Locally

### Prerequisites

- JDK 11 or higher
- Android SDK (API level 34)
- Gradle 8.4

### Build Steps

```bash
# Clone the repository
git clone https://github.com/sumanthdatasci/Techno--attendance-.git
cd Techno--attendance-

# Build the debug APK
./gradlew assembleDebug
```

The generated APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## Running on Device

```bash
# Install the APK on a connected device
adb install app/build/outputs/apk/debug/app-debug.apk

# Or run directly
./gradlew installDebug
```

## GitHub Actions Build

The repository includes an automated build workflow that:

1. Triggers manually via `workflow_dispatch`
2. Checks out the code
3. Sets up Java and Gradle
4. Builds the debug APK
5. Uploads the APK as an artifact

### Running the Workflow

1. Go to **Actions** tab on GitHub
2. Select **Build APK** workflow
3. Click **Run workflow**
4. Download the generated artifact

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/techno/attendance/
│   │   │       ├── MainActivity.java
│   │   │       ├── activities/
│   │   │       ├── database/
│   │   │       ├── models/
│   │   │       ├── utils/
│   │   │       └── adapters/
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   ├── drawable/
│   │   │   └── menu/
│   │   └── AndroidManifest.xml
│   └── test/
├── build.gradle
└── proguard-rules.pro
```

## Features Implemented

✅ Dashboard with statistics
✅ Member management (add, edit, search, deactivate)
✅ Daily attendance marking
✅ Holiday management
✅ Date navigation
✅ Daily summary
✅ Absentee list with WhatsApp sharing
✅ Monthly reports
✅ Individual member history
✅ Search functionality
✅ Backup and restore
✅ Offline-first architecture
✅ Sample test data (10 members)

## Development

This project is built with Android Studio compatibility and can be opened directly in Android Studio.

## License

MIT License
