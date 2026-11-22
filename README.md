# Kotlin/Native Regex stale capture regression

Kotlin/Native `Regex` can return a capture from an abandoned backtracking attempt.

**TL;DR:** The last good Kotlin/Native version is 2.3.10; 2.3.20 through 2.4.21 are broken. Kotlin/JVM passes.

```kotlin
Regex("(a)?a").matchEntire("a")!!.groups[1]
```

Expected: `null`
Actual: `MatchGroup(value=a, range=0..0)`

```sh
./gradlew jvmTest macosArm64Test                    # 2.4.21: JVM passes, Native fails
./gradlew macosArm64Test -PkotlinVersion=2.3.10     # Native passes
```

Details: [REPORT.md](REPORT.md)
