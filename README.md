# Penny Drop

Penny Drop is a small dice-and-coin Android game: roll a six-sided die,
each face fills a slot, and pass the dice before busting to keep your
coins. The board, AI players, and game state live entirely in the app.

The project was originally built as a fragment-based Kotlin app from the
Pragmatic Bookshelf title *Kotlin and Android Development featuring
Jetpack* by Michael Fazio, and has since been ported to Jetpack Compose
with navigation-compose and Hilt for DI.

## Tech stack

- Kotlin 2.x on JDK 21, targeting Android SDK 36 (min SDK 26)
- Jetpack Compose (Material 3) + navigation-compose for the UI
- Hilt for dependency injection
- Room + Kotlin Flow / StateFlow for persistence and state
- JUnit for game logic unit tests
- R8 minification and resource shrinking for release builds

## Build and run

```sh
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

Or open the project in Android Studio Hedgehog (or newer) and run the
`app` configuration.

### Nix users

A `flake.nix` is provided that pins JDK 21 and the Android command-line
tools. With direnv installed:

```sh
direnv allow   # picks up .envrc and enters the devshell
```

## Tests

```sh
./gradlew :app:testDebugUnitTest
```

Covers `GameHandler`, the AI personalities, and the slot model.
