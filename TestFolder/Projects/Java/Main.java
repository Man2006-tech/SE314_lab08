
package filesearch;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class RecursiveFileSearch {

    public static void main(String[] args) {

        // Check command-line arguments
        if (args.length < 2) {
            System.out.println("Usage: java RecursiveFileSearch <directory> <filename1> [filename2...] [--ignore-case]");
            return;
        }

        try {
            Path directory = Paths.get(args[0]);

            // Check search mode
            boolean caseSensitive = true;
            int endIndex = args.length;

            if (args[args.length - 1].equalsIgnoreCase("--ignore-case")) {
                caseSensitive = false;
                endIndex--;
            }

            // Store multiple filenames
            List<String> fileNames = new ArrayList<>();

            for (int i = 1; i < endIndex; i++) {
                fileNames.add(args[i]);
            }

            if (fileNames.isEmpty()) {
                System.out.println("Please provide at least one filename.");
                return;
            }

            // Start searching
            Map<String, List<Path>> results =
                    searchFiles(directory, fileNames, caseSensitive);

            // Display search results
            for (String fileName : results.keySet()) {

                List<Path> matches = results.get(fileName);

                System.out.println("\nSearching for: " + fileName);

                if (matches.isEmpty()) {
                    System.out.println("File not found.");
                } else {

                    for (Path path : matches) {
                        System.out.println("Found: " + path);
                    }

                    System.out.println("Total occurrences: " + matches.size());
                }
            }

        } catch (InvalidPathException e) {
            System.out.println("Error: Invalid directory path.");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Error while searching: " + e.getMessage());

        } catch (SecurityException e) {
            System.out.println("Error: Permission denied.");
        }
    }

    // Initialize the search
    public static Map<String, List<Path>> searchFiles(
            Path root,
            List<String> fileNames,
            boolean caseSensitive) throws IOException {

        if (root == null || !Files.isDirectory(root)) {
            throw new IllegalArgumentException(
                    "The specified directory does not exist or is invalid."
            );
        }

        if (fileNames == null || fileNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one filename must be provided."
            );
        }

        Map<String, List<Path>> results = new LinkedHashMap<>();

        for (String name : fileNames) {

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Filename cannot be empty."
                );
            }

            results.put(name, new ArrayList<>());
        }

        // Call recursive method
        searchDirectory(root, results, caseSensitive);

        return results;
    }

    // Recursive method to search directories
    private static void searchDirectory(
            Path directory,
            Map<String, List<Path>> results,
            boolean caseSensitive) throws IOException {

        // Read all entries in the current directory
        try (DirectoryStream<Path> entries =
                     Files.newDirectoryStream(directory)) {

            for (Path entry : entries) {

                // If entry is a directory, search recursively
                if (Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS)) {

                    searchDirectory(entry, results, caseSensitive);

                }

                // If entry is a file, compare its name
                else if (Files.isRegularFile(
                        entry, LinkOption.NOFOLLOW_LINKS)) {

                    String actualName = entry.getFileName().toString();

                    for (String requestedName : results.keySet()) {

                        boolean matches;

                        if (caseSensitive) {
                            matches = actualName.equals(requestedName);
                        } else {
                            matches = actualName.equalsIgnoreCase(
                                    requestedName
                            );
                        }

                        if (matches) {
                            results.get(requestedName).add(
                                    entry.toAbsolutePath()
                            );
                        }
                    }
                }
            }
        }
    }
}
