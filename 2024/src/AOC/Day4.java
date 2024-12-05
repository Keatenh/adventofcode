package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 {

    public List<List<Character>> fileContents(String path) throws IOException {
        List<List<Character>> crossword = new ArrayList<>();
        Files.lines(Path.of(path)).forEach(
                (line) -> {
                    List<Character> row = line.chars().mapToObj(e->(char)e).collect(Collectors.toList());;
//                    System.out.println(report);
                    crossword.add(row);
                });
//        System.out.println(crossword);
        return crossword;
    }

    public static <T> T safeGet(List<T> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }

        // Return a default value based on type
        return getDefaultValue(list);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getDefaultValue(List<T> list) {
        // If the list is empty, we can't infer a type, so return null
        if (list.isEmpty()) {
            return (T) (Character) '~';
        }

        // Determine the class type of the first element
        Class<?> type = list.get(0).getClass();

        // Handle common cases for default values
        if (List.class.isAssignableFrom(type)) {
        return (T) new ArrayList<>(); // Return new empty ArrayList for List types
    }

        // For all other types, return null
        return (T) (Character) '~';
    }

    public void solvePart1(String path) {
        try {
            List<List<Character>> puzzle = fileContents(path);
            int count = 0;
            for (int i = 0; i < puzzle.size(); i++) {
                for (int j = 0; j < puzzle.get(i).size(); j++) {
                    if (puzzle.get(i).get(j) == 'X') {
//                        System.out.println("Found X at: "+i+" , "+j);
                        //Could I make this LESS UGLY by coupling the characters to the distance, n?
                        if (safeGet(safeGet(puzzle, i-1), j-1) == 'M' &&
                                        safeGet(safeGet(puzzle, i-2), j-2) == 'A' &&
                                        safeGet(safeGet(puzzle, i-3), j-3) == 'S'
                            ) { count++; }
                        if (safeGet(safeGet(puzzle, i-1), j) == 'M' &&
                                        safeGet(safeGet(puzzle, i-2), j) == 'A' &&
                                        safeGet(safeGet(puzzle, i-3), j) == 'S'
                            ) { count++; }
                        if (safeGet(safeGet(puzzle, i-1), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i-2), j+2) == 'A' &&
                                safeGet(safeGet(puzzle, i-3), j+3) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i), j-1) == 'M' &&
                                safeGet(safeGet(puzzle, i), j-2) == 'A' &&
                                safeGet(safeGet(puzzle, i), j-3) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i), j+2) == 'A' &&
                                safeGet(safeGet(puzzle, i), j+3) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i+1), j-1) == 'M' &&
                                safeGet(safeGet(puzzle, i+2), j-2) == 'A' &&
                                safeGet(safeGet(puzzle, i+3), j-3) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i+1), j) == 'M' &&
                                safeGet(safeGet(puzzle, i+2), j) == 'A' &&
                                safeGet(safeGet(puzzle, i+3), j) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i+1), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i+2), j+2) == 'A' &&
                                safeGet(safeGet(puzzle, i+3), j+3) == 'S'
                        ) { count++; }
                    }
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        try {
            List<List<Character>> puzzle = fileContents(path);
            int count = 0;
            for (int i = 0; i < puzzle.size(); i++) {
                for (int j = 0; j < puzzle.get(i).size(); j++) {
                    if (puzzle.get(i).get(j) == 'A') {
                        if (safeGet(safeGet(puzzle, i-1), j-1) == 'M' &&
                                safeGet(safeGet(puzzle, i+1), j+1) == 'S' &&
                                safeGet(safeGet(puzzle, i-1), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i+1), j-1) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i+1), j-1) == 'M' &&
                                safeGet(safeGet(puzzle, i-1), j+1) == 'S' &&
                                safeGet(safeGet(puzzle, i+1), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i-1), j-1) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i-1), j-1) == 'M' &&
                                safeGet(safeGet(puzzle, i+1), j+1) == 'S' &&
                                safeGet(safeGet(puzzle, i+1), j-1) == 'M' &&
                                safeGet(safeGet(puzzle, i-1), j+1) == 'S'
                        ) { count++; }
                        if (safeGet(safeGet(puzzle, i-1), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i+1), j-1) == 'S' &&
                                safeGet(safeGet(puzzle, i+1), j+1) == 'M' &&
                                safeGet(safeGet(puzzle, i-1), j-1) == 'S'
                        ) { count++; }
                    }
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
