<p align="center">
<a href="https://travis-ci.com/SimonSiefke/gradle-java-example"><img src="https://travis-ci.com/SimonSiefke/gradle-java-example.svg?branch=master" alt="Build Status"></a>
<a href="https://codecov.io/gh/SimonSiefke/gradle-java-example">
<img src="https://codecov.io/gh/SimonSiefke/gradle-java-example/branch/master/graph/badge.svg" alt="Coverage Status"></a>
<a href="https://raw.githubusercontent.com/SimonSiefke/gradle-java-example/master/LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License"></a>
<a href="https://github.com/SimonSiefke/gradle-java-example/issues"><img src="https://img.shields.io/github/issues/SimonSiefke/gradle-java-example.svg"></a></p>

# Java & Gradle Example Project

> testing out java with gradle 🙄

## Get started 🚀

```bash
git clone https://github.com/SimonSiefke/gradle-java-example
cd gradle-java-example
./gradlew build
```

## Scripts 📃

```bash
./gradlew test  # run the tests
./gradlew jmh   # run the benchmarks
./gradlew run   # run the visualization
./gradlew build # build
```

## Usage with Eclipse IDE

1. Make sure you have the latest eclipse version (2018-12 or newer) installed
1. Install Buildship Gradle Integration 3.0 from the marketplace
1. `File -> Import -> Gradle -> Existing Gradle Project -> Next -> Next -> Finish`
1. Should should now see a panel named `Gradle tasks`

## Usage with IntelliJ IDE

1. `File -> New -> Project from existing sources -> Select the build.gradle file inside the gradle-java-example folder -> Ok -> Ok`
