# Kotlin/Native regex keeps stale capture groups (2.3.20+)

Since Kotlin 2.3.20, Kotlin/Native's `Regex` can report a group as matched with a value from an abandoned backtracking attempt. Kotlin/JVM and Native 2.3.10 behave correctly.

```sh
./gradlew jvmTest macosArm64Test                        # Kotlin 2.4.21: JVM passes, macOS fails 4/4
./gradlew macosArm64Test -PkotlinVersion=2.3.10         # last good version: passes
```

Details, versions tested and the likely cause: [REPORT.md](REPORT.md).
