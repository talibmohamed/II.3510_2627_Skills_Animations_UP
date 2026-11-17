# II.3510 - Animated listings exercise (UP)

This is the **unfinished project** for the class exercise. It already runs. Your task is to implement the missing animations, not to build a marketplace backend.

## Setup

Open this folder in Android Studio and sync Gradle. Install Android SDK Platform 37. Let Android Studio provision the JDK 25 Gradle toolchain declared in `gradle/gradle-daemon-jvm.properties`. Run `app` on Android 8.0 (API 26) or newer. The initial Gradle sync needs internet. The running demo uses local fictional data only.

## Exercise

Read [EXERCISE.md](EXERCISE.md). Find TODO 1a, 1b, 2, 3, and 4 in `Listing.kt` and `CampusDemo.kt`. Estimated time: 25-35 minutes, outside the 10-minute presentation.

The interface, filtering, sorting, favourite buttons, navigation, replay, listing count, and chevron rotation already work. Four animation mechanisms are intentionally incomplete. Favourite and count animations remain as examples.


## Checks

Windows: `gradlew.bat :app:assembleDebug :app:testDebugUnitTest`

macOS/Linux: `chmod +x gradlew` then `./gradlew :app:assembleDebug :app:testDebugUnitTest`

Device checks: `gradlew.bat :app:connectedDebugAndroidTest`

The timing test is deliberately ignored until TODO 1a is complete. Remove its `@Ignore` annotation to check your timing implementation. Passing the other tests checks behavior, not animation quality. Use the acceptance checklist in EXERCISE.md to inspect the animation itself.

Repository: https://github.com/talibmohamed/II.3510_2627_Skills_Animations_UP

## Presentation

Download the [final class presentation](docs/Animated_Listings_Presentation.pptx).
