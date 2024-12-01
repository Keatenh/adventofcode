package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.lang.Math;
import java.util.stream.Stream;

// Part 1: Find the sum of total differences between each element of 2 (low-> high sorted) lists of integers
// Part 2: Take each element in Left list, multiply it by the number of times each appears in the right list, and take the sum.
public class Day1 {

    public ArrayList<ArrayList<Integer>> fileContents(String path) throws IOException {
        ArrayList<Integer> columnA = new ArrayList<>();
        ArrayList<Integer> columnB = new ArrayList<>();
        ArrayList<ArrayList<Integer>> result = new ArrayList();
        result.add(columnA);
        result.add(columnB);
        Files.lines(Path.of(path)).forEach(
                (line) -> {
//                System.out.println(line);
                String[] fields = line.split("   ");
                columnA.add(Integer.parseInt(fields[0]));
                columnB.add(Integer.parseInt(fields[1]));
                });
        return result;
    }

    public void solvePart1(String path) {
        int total = 0;
        try {
            ArrayList<ArrayList<Integer>> lines = fileContents(path);
            lines.get(0).sort(null);
            lines.get(1).sort(null);
//            System.out.println(lines);
            for(int i=0; i < lines.get(0).size(); i++) {
                int delta = Math.abs(lines.get(1).get(i) - lines.get(0).get(i));
                total+=delta;
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        int sum = 0;
        try {
            ArrayList<ArrayList<Integer>> lines = fileContents(path);
            for(int i=0; i < lines.get(0).size(); i++) {
                int base = lines.get(0).get(i);
                int frequency = lines.get(1).stream().filter(n -> n == base).toList().size();
                sum+=base*frequency;
            }
            System.out.println(sum);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
