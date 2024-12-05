package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 {

    // Returns substring of string that matches given regex
    public String filterStringOnRegex(String input, String regx) {
        StringBuilder contents = new StringBuilder();
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(input);
        while (m.find()) {
            contents.append(m.group());
        }
        return contents.toString();
    }

    // Returns 2 integers to multiply from a string with specific format; e.g. abc(24,83)
    public List<Integer> factorsFromInstruction(String instruction) {
        return Arrays.stream(instruction.split(",")).map(s -> Integer.parseInt(filterStringOnRegex(s,"\\d*"))).collect(Collectors.toList());
    }

    // Core logic
    public int commonSolver(String path, int part) throws IOException {
        AtomicInteger total = new AtomicInteger();
        // zero or more times (*), but non-greedily (?), so it stops at the first closing parenthesis.
        String regx = "mul\\(\\d*?,\\d*?\\)";
        //Record of if we are enabling mul() operations; Persistent between lines
        AtomicReference<Boolean> factoring = new AtomicReference<>(true);
        /**
         * Note: This turned out to be the wrong approach, since I initially misread the problem,
         * but I find value in the functionality, so I am recording it here:
         *
         * Assuming we needed to capture 'don't' independent of 'do', we use a negative lookahead assertion
         * This ensures that when we match "do" it is not followed by "'nt"
         *
         * Pattern p = Pattern.compile("mul\\(\\d*?,\\d*?\\)|do(?!n't)|don't");
         */
        if (part == 2) {
            regx = "mul\\(\\d*?,\\d*?\\)|do\\(\\)|don't\\(\\)";
        }

        Pattern p = Pattern.compile(regx);
        Files.lines(Path.of(path)).forEach(line -> {
            AtomicInteger lineSum = new AtomicInteger();
            List<String> matches = new ArrayList<>();
            Matcher m = p.matcher(line);
            while (m.find()) {
                matches.add(m.group());
            }
            List<List<Integer>> factors = matches.stream().map(instruction -> {
                if (part == 2) {
                    if (Objects.equals(instruction, "do()")) {
                        factoring.set(true);
                    }
                    else if (Objects.equals(instruction, "don't()")) {
                        factoring.set(false);
                    } else {
                        if (factoring.get()) {
                            return factorsFromInstruction(instruction);
                        }
                    }
                    return new ArrayList<Integer>();
                } else {
                    //Part 1
                    return factorsFromInstruction(instruction);
                }
            }).collect(Collectors.toList());
            factors.stream().filter(set -> set.size()>0).forEach(pair -> {
                lineSum.addAndGet(pair.get(0) * pair.get(1));
            });
            total.addAndGet(lineSum.get());
        });
        return total.get();
    }

    public void solvePart1(String path) {
        try {
            int result = commonSolver(path, 1);
            System.out.println("The sum of multiplications is: "+result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        try {
            int result = commonSolver(path, 2);
            System.out.println("The sum of multiplications is: "+result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
