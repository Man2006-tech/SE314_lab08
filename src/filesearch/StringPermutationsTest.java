
package filesearch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class StringPermutationsTest {

    @Test
    void testThreeCharacters() {
        List<String> result =
                StringPermutations.generatePermutations("ABC", true);

        assertEquals(6, result.size());
    }

    @Test
    void testIncludeDuplicates() {
        List<String> result =
                StringPermutations.generatePermutations("AAB", true);

        assertEquals(6, result.size());
    }

    @Test
    void testExcludeDuplicates() {
        List<String> result =
                StringPermutations.generatePermutations("AAB", false);

        assertEquals(3, result.size());
    }

    @Test
    void testEmptyInput() {
        assertThrows(IllegalArgumentException.class, () ->
                StringPermutations.generatePermutations("", true));
    }

    @Test
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () ->
                StringPermutations.generatePermutations(null, true));
    }

    @Test
    void testIterativeAlgorithm() {
        List<String> result =
                StringPermutations.generatePermutationsIterative("ABC", true);

        assertEquals(6, result.size());
    }

    @Test
    void testBothAlgorithmsProduceSameResults() {
        Set<String> recursive = new HashSet<>(
                StringPermutations.generatePermutations("ABC", true));

        Set<String> iterative = new HashSet<>(
                StringPermutations.generatePermutationsIterative("ABC", true));

        assertEquals(recursive, iterative);
    }
}
