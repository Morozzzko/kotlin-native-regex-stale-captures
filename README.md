# Kotlin/Native and Wasm Regex stale capture regression

Kotlin/Native and Kotlin/Wasm `Regex` can return a capture from an abandoned backtracking attempt.

**TL;DR:** The last good version is 2.3.10; 2.3.20 through 2.4.21 are broken on Native and Wasm.

```kotlin
Regex("(a)?a").matchEntire("a")!!.groups[1]
```

| Target | Result |
|---|---|
| JVM | ✅ `null` |
| JS | ✅ `null` |
| Native | ❌ `MatchGroup(value=a, range=0..0)` |
| Wasm | ❌ `MatchGroup(value=a, range=0..0)` |

```sh
./gradlew jvmTest jsNodeTest macosArm64Test wasmJsNodeTest --continue   # 2.4.21: JVM and JS pass, Native and Wasm fail
./gradlew macosArm64Test wasmJsNodeTest -PkotlinVersion=2.3.10         # Native and Wasm pass
```

Details: [REPORT.md](REPORT.md)
