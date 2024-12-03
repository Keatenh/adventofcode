package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day2 {

    public ArrayList<List<Integer>> fileContents(String path) throws IOException {
        ArrayList<List<Integer>> reports = new ArrayList<>();
        Files.lines(Path.of(path)).forEach(
                (line) -> {
                    //Note: I added the empty check because I targeted the wrong text file at first :P
                    List<Integer> report = Arrays.stream(line.split(" ")).filter(c -> !c.isEmpty()).map(c -> Integer.parseInt(c)).collect(Collectors.toList());
//                    System.out.println(report);
                    reports.add(report);
                });
        return reports;
    }

    public Boolean checkReportSafety(List<Integer> report) {
        Boolean safe = true;
        for (int i=0; i<report.size()-2 ; i++) {
            int cur = report.get(i);
            int next = report.get(i+1);
            int afterNext = report.get(i+2);
            // Condition 1: The levels are either all increasing or all decreasing.
         if ((cur>next && next<afterNext)||((cur<next && next>afterNext))){
             safe = false;
             break;
             // Condition 2: Any two adjacent levels differ by at least one and at most three.
             // Note: Included 'afterNext' here to handle case at end of report, accessing report.size()-1
         } else if (Math.abs(next-cur) < 1 ||
                    Math.abs(next-cur) > 3 ||
                    Math.abs(afterNext-next) < 1 ||
                    Math.abs(afterNext-next) > 3 ) {
             safe = false;
             break;
         }
        }
//        System.out.println(safe);
        return safe;
    }

    //Using "phase" to denote that we often have multiple possible removal choices per "unsafe" condition
    //Only using values of 0 or 1 for phase
//    public Boolean checkReportSafetyWithProblemDampener(List<Integer> report, int phase) {
//        Boolean safe = true;
//        Boolean dampened = false;
//        int inversePhase = phase == 1 ? 0 : 1;
//        for (int i=0; i<report.size()-2 ; i++) {
//            int cur = report.get(i);
//            int next = report.get(i+1);
//            int afterNext = report.get(i+2);
//            if ((cur>next && next<afterNext)||((cur<next && next>afterNext))){
//                if (dampened) {
//                    safe = false;
//                    break;
//                } else {
//                    dampened = true;
//                    report.remove(i+phase);
//                    i-=inversePhase;
//                }
//            } else if (Math.abs(next-cur) < 1 ||
//                    Math.abs(next-cur) > 3 ||
//                    Math.abs(afterNext-next) < 1 ||
//                    Math.abs(afterNext-next) > 3 ) {
//                if (dampened) {
//                    safe = false;
//                    break;
//                } else {
//                    dampened = true;
//                    report.remove(i+phase);
//                    i-=inversePhase;
//                }
//            }
//        }
//        return safe;
//    }

    public ArrayList<List<Integer>> versionReports(List<Integer> report) {
        ArrayList<List<Integer>> versions = new ArrayList<>();
        for (int i = 0; i < report.size(); i++) {
            List<Integer> tempReport = new ArrayList<>(report);
            tempReport.remove(i);
            versions.add(tempReport);
        }
        return versions;
    }

    public void solvePart1(String path) {
        try {
            int totalSafe = fileContents(path).stream()
                    .map(report -> checkReportSafety(report))
                    .filter(r -> r == true)
                    .collect(Collectors.toList())
                    .size();
            System.out.println(totalSafe+" reports are safe.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        try {
            AtomicInteger totalSafe = new AtomicInteger();
            List<ArrayList<List<Integer>>> versions = fileContents(path).stream()
                    .map(report -> versionReports(report))
                    .collect(Collectors.toList());
//            System.out.println(versions);
            versions.forEach(report -> {
                Boolean safe = report.stream().map(version -> checkReportSafety(version)).anyMatch(v -> v == true);
                if (safe == true) {
                    totalSafe.getAndIncrement();
                }
            });

            System.out.println(totalSafe+" reports are safe.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
