# Kotlin/Native Regex retains stale captures (2.3.20 regression)

Since 2.3.20, Kotlin/Native `Regex` can report a group as matched with a value from an abandoned backtracking attempt; Kotlin/JVM and Kotlin/Native 2.3.10 report it as unmatched.

## Reproduction

```kotlin
Regex("(a)?a").matchEntire("a")!!.groups[1]       // greedy optional rolled back to zero occurrences
Regex("(a){2}|a").matchEntire("a")!!.groups[1]    // greedy group fails before reaching its minimum
Regex("(a)+b|a").matchEntire("a")!!.groups[1]     // greedy group matches, then what follows it fails
Regex("(a)??b|a").matchEntire("a")!!.groups[1]    // reluctant group fails
```

Expected `groups[1]`: `null`.
Actual on Kotlin/Native 2.3.20+: `MatchGroup(value=a, range=0..0)`.

| Kotlin | `jvmTest` | `macosArm64Test` |
|---|---|---|
| 2.3.10 | – | 4/4 pass |
| 2.3.20, 2.3.21 | – | 4/4 fail |
| 2.4.0, 2.4.20 | – | 4/4 fail |
| 2.4.21 | 4/4 pass | 4/4 fail |

Environment: macOS 27.2 (arm64), Xcode 27.2, Gradle 8.14.3, JDK 21 (Gradle daemon). The same behavior shows up on `iosSimulatorArm64` in a production app.

Real-world impact: a 12-hour time parser, `(0?\d|1[0-2])([0-5]\d)?([0-5]\d)?([ap])` on `1030p`, returns `03` as the seconds group on iOS, so `10:30 pm` becomes `22:30:03`.

## Expected behavior

`MatchGroupCollection.get` returns a `MatchGroup` "if the group with the specified index was matched or `null` otherwise." In all four cases the final match is the second branch, or zero occurrences of the group, so group 1 did not participate.

This is not the "retain the previously captured value" behavior for repeated groups (e.g. `(a(b)?)+`). That applies to earlier iterations that remain part of the successful match; here the value comes from an attempt that was discarded entirely.

## Likely cause

f213a9f11e "KT-78089 match quantified groups without recursion, when possible" (in 2.3.20-Beta2) added `TrivialGroupQuantifierSet` and `ReluctantTrivialGroupQuantifierSet`.

- `TrivialGroupQuantifierSet` saves the group's boundaries after `innerSet.matches(...)` succeeds, so rolling an occurrence back "restores" the occurrence being dropped (`(a)?a`).
- When the quantifier as a whole fails, before reaching the minimum (`(a){2}|a`) or because `next.matches(...)` fails (`(a)+b|a`), it returns `-1` without restoring the boundaries the group had on entry.
- `ReluctantTrivialGroupQuantifierSet` returns `-1` on failure without restoring the group (`(a)??b|a`).

Suggested fix: save the group's boundaries before each occurrence, and restore the entry boundaries on every failure path in both sets.

## Related

- KT-89187: Regex fails to backtrack across unequal-length alternatives (same engine area; different symptom).
- KT-58198: Native Regex issues (umbrella).
