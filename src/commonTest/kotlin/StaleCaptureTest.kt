import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class StaleCaptureTest {
    private fun assertGroupUnmatched(pattern: String, input: String) {
        val match = assertNotNull(Regex(pattern).matchEntire(input))
        assertNull(match.groups[1], "group 1 of /$pattern/ on \"$input\"")
    }

    @Test
    fun greedyOptionalRolledBackToZeroOccurrences() = assertGroupUnmatched("(a)?a", "a")

    @Test
    fun greedyFailsBeforeReachingMinimum() = assertGroupUnmatched("(a){2}|a", "a")

    @Test
    fun greedyFailsAfterReachingMinimum() = assertGroupUnmatched("(a)+b|a", "a")

    @Test
    fun reluctantFails() = assertGroupUnmatched("(a)??b|a", "a")
}
