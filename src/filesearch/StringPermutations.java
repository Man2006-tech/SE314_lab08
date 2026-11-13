
package filesearch;

import java.util.*;

public class StringPermutations {

    public static List<String> generatePermutations(
            String input, boolean includeDuplicates) {

        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(
                    "Input string cannot be empty or null.");
        }

        List<String> results = new ArrayList<>();

        generate("", input, results, includeDuplicates);

        return results;
    }

    private static void generate(
            String prefix, String remaining,
            List<String> results, boolean includeDuplicates) {

        if (remaining.isEmpty()) {
            results.add(prefix);
            return;
        }

        Set<Character> used = new HashSet<>();

        for (int i = 0; i < remaining.length(); i++) {

            char current = remaining.charAt(i);

            if (!includeDuplicates && !used.add(current)) {
                continue;
            }

            String newPrefix = prefix + current;
            String newRemaining =
                    remaining.substring(0, i)
                    + remaining.substring(i + 1);

            generate(newPrefix, newRemaining,
                    results, includeDuplicates);
        }
    }

public static List<String> generatePermutationsIterative(
        String input, boolean includeDuplicates) {

    if (input == null || input.isEmpty()) {
        throw new IllegalArgumentException("Input cannot be empty.");
    }

    List<String> permutations = new ArrayList<>();
    permutations.add("");

    for (char current : input.toCharArray()) {

        List<String> newPermutations = new ArrayList<>();

        for (String existing : permutations) {

            for (int i = 0; i <= existing.length(); i++) {

                String newValue =
                        existing.substring(0, i)
                        + current
                        + existing.substring(i);

                newPermutations.add(newValue);
            }
        }

        if (!includeDuplicates) {
            permutations = new ArrayList<>(
                    new LinkedHashSet<>(newPermutations));
        } else {
            permutations = newPermutations;
        }
    }

    return permutations;
}


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String input = scanner.nextLine();

        System.out.print("Include duplicate permutations? (yes/no): ");
        boolean includeDuplicates =
                scanner.nextLine().equalsIgnoreCase("yes");

        try {
            List<String> permutations =
                    generatePermutations(input, includeDuplicates);

            System.out.println("Total permutations: " + permutations.size());

            for (String permutation : permutations) {
                System.out.println(permutation);

long startRecursive = System.nanoTime();

generatePermutations(input, includeDuplicates);

long recursiveTime = System.nanoTime() - startRecursive;

long startIterative = System.nanoTime();

generatePermutationsIterative(input, includeDuplicates);

long iterativeTime = System.nanoTime() - startIterative;

System.out.println("\nPerformance Comparison:");
System.out.println("Recursive time: " + recursiveTime + " ns");
System.out.println("Iterative time: " + iterativeTime + " ns");

            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
