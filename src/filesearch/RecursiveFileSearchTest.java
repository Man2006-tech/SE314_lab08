
package filesearch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RecursiveFileSearchTest {

    @TempDir
    Path tempDir;

    @Test
    void testFileInRootDirectory() throws Exception {
        Files.createFile(tempDir.resolve("notes.txt"));

        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList("notes.txt"), true);

        assertEquals(1, result.get("notes.txt").size());
    }

    @Test
    void testFileInSubdirectory() throws Exception {
        Path folder = Files.createDirectory(tempDir.resolve("Projects"));
        Files.createFile(folder.resolve("report.pdf"));

        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList("report.pdf"), true);

        assertEquals(1, result.get("report.pdf").size());
    }

    @Test
    void testMultipleOccurrences() throws Exception {
        Files.createFile(tempDir.resolve("notes.txt"));

        Path folder = Files.createDirectory(tempDir.resolve("Folder"));
        Files.createFile(folder.resolve("notes.txt"));

        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList("notes.txt"), true);

        assertEquals(2, result.get("notes.txt").size());
    }

    @Test
    void testCaseInsensitiveSearch() throws Exception {
        Files.createFile(tempDir.resolve("NOTES.TXT"));

        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList("notes.txt"), false);

        assertEquals(1, result.get("notes.txt").size());
    }

    @Test
    void testCaseSensitiveSearch() throws Exception {
        Files.createFile(tempDir.resolve("NOTES.TXT"));

        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList("notes.txt"), true);

        assertEquals(0, result.get("notes.txt").size());
    }

    @Test
    void testFileNotFound() throws Exception {
        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList("missing.txt"), true);

        assertTrue(result.get("missing.txt").isEmpty());
    }

    @Test
    void testInvalidDirectory() {
        Path invalidPath = tempDir.resolve("nonexistent");

        assertThrows(IllegalArgumentException.class, () ->
                RecursiveFileSearch.searchFiles(
                        invalidPath, Arrays.asList("notes.txt"), true));
    }

    @Test
    void testEmptyFilename() {
        assertThrows(IllegalArgumentException.class, () ->
                RecursiveFileSearch.searchFiles(
                        tempDir, Arrays.asList(""), true));
    }

    @Test
    void testMultipleFiles() throws Exception {
        Files.createFile(tempDir.resolve("notes.txt"));
        Files.createFile(tempDir.resolve("report.pdf"));

        Map<String, List<Path>> result =
                RecursiveFileSearch.searchFiles(
                        tempDir,
                        Arrays.asList("notes.txt", "report.pdf"),
                        true);

        assertEquals(1, result.get("notes.txt").size());
        assertEquals(1, result.get("report.pdf").size());
    }
}
