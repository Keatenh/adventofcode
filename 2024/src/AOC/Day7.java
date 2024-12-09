package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {

    public class Calibration {
        private Double value;
        private List<Double> terms;

        public Calibration(String input) {
            String[] expressions = input.split(":");
            this.value = Double.parseDouble(expressions[0]);
            this.terms = Arrays.stream(expressions[1].trim().split(" ")).map(Double::parseDouble).toList();
        }
    }

    public List<Calibration> fileContents(String path) throws IOException {
        List<Calibration> calibrations = new ArrayList<>();
        Files.lines(Path.of(path)).forEach(
                (line) -> {
                    calibrations.add(new Calibration(line));
                });
        return calibrations;
    }

    public void buildExpressions(List<Double> in, boolean mul, int depth, Double total, List<Double> out) {
        if (depth >= 0) { //skip "1st" iteration at -1, so that we can start with 2 operations
            if (mul) {
                total *= in.get(depth);
            } else {
                total += in.get(depth);
            }
        }

        if (depth < in.size()-1) {
            buildExpressions(in,true,depth+1,total,out);
            buildExpressions(in,false,depth+1,total,out);
        } else {
            out.add(total);
        }
    }

    // New method for a 3rd possible operator which concatenates the characters of 2 numbers and parses it as a new one
    public void buildExpressionsFrom3Operators(List<Double> in, int depth, Double total, List<Double> out) {
        //Exit recursion if we have used all the input list elements
        if (depth == in.size()) {
            out.add(total);
            return;
        }
        Double cur = in.get(depth);
        buildExpressionsFrom3Operators(in,depth+1,total * cur, out);
        buildExpressionsFrom3Operators(in,depth+1,total + cur, out);
        buildExpressionsFrom3Operators(in,depth+1, Double.parseDouble(String.format ("%.0f",total)+ String.format ("%.0f",cur)), out);
    }

    /**
     *
     * OBJECTIVE: Find the sum of all the values of VALID equations using only * and + operators
     */
    public void solvePart1(String path) {
        try {
            final Double[] totalCalibrationResult = {(double) 0};
            List<Calibration> l = fileContents(path);
            l.forEach(cal -> {
//                System.out.println(cal.value);
//                System.out.println(cal.terms);
                List<Double> possibilities = new ArrayList<>();
                buildExpressions(cal.terms, true, -1, (double) 0,possibilities);
//                System.out.println(possibilities);
                if (possibilities.contains(cal.value)) {
                    totalCalibrationResult[0] += cal.value;
                }
            });
            System.out.printf( "Total Calibration Result is %.0f \n",totalCalibrationResult[0]);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * OBJECTIVE: Find the sum of all the values of VALID equations using only *, + and || operators
     * (See note on buildExpressionsFrom3Operators())
     */
    public void solvePart2(String path) {
        try {
            final Double[] totalCalibrationResult = {(double) 0};
            List<Calibration> l = fileContents(path);
            l.forEach(cal -> {
//                System.out.println(cal.value);
//                System.out.println(cal.terms);
                List<Double> possibilities = new ArrayList<>();
                buildExpressionsFrom3Operators(cal.terms,0, (double) 0, possibilities);
//                System.out.println(possibilities);
                if (possibilities.contains(cal.value)) {
                    totalCalibrationResult[0] += cal.value;
                }
            });
            System.out.printf( "Total Calibration Result is %.0f \n",totalCalibrationResult[0]);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
