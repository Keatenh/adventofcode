package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.AbstractMap;
import java.util.Map;

public class Day5 {
    public List<List<List<String>>> fileContents(String path) throws IOException {
        List<List<List<String>>> rulesAndUpdates = new ArrayList<>();
        List<List<String>> rules = new ArrayList<>();
        List<List<String>> updates = new ArrayList<>();
        rulesAndUpdates.add(rules);
        rulesAndUpdates.add(updates);
        AtomicInteger index = new AtomicInteger();
        Files.lines(Path.of(path)).forEach(
                (line) -> {
                    if (line.isEmpty()) {
                        index.getAndIncrement();
                    } else {
                        if (index.get() == 0) {
                            rulesAndUpdates.get(index.get()).add(Arrays.stream(line.split("\\|")).collect(Collectors.toList()));
                        } else { //index == 1
                            rulesAndUpdates.get(index.get()).add(Arrays.stream(line.split(",")).collect(Collectors.toList()));
                        }
                    }
                });
        return rulesAndUpdates;
    }

    public static void addToMap(HashMap<String, List<String>> map, String key, String value) {
        // If the key doesn't exist, create a new list for it
        map.putIfAbsent(key, new ArrayList<>());

        // Add the item to the list for that key
        map.get(key).add(value);
    }

    //This is gross, but I am using getKey() for passing true/false
    //and getValue() for tracking the index for failure case
    //this is to avoid building a class to reuse in part 2
    public Map.Entry<Boolean, Map.Entry<Integer,Integer>> passesRules(HashMap<String, List<String>> rules, List<String> updates) {
//        System.out.println("Evaluating rules for: " + updates);
        boolean passing = true;
        int failedIndex = -1;
        String failedEntry = "";
        int firstConflictIndex = -1;
        int numberOfUpdates = updates.size();

        for (int i = 0; i < numberOfUpdates; i++) {
            //Compare what SHOULD come after the page (rules) to what DOES come after (rest of update)
            //ASSUMPTION 1: There are no uncovered rules -> EDIT: this was shown to be false in example with #13 -> adding null check
            // and that we just need to check that all the updates after the current page are contained in the rules we compiled

            List<String> currentPageRules = rules.get(updates.get(i));
            //This branch is unused -> Was tested to work in case ASSUMPTION 1 was wrong
            if (i == numberOfUpdates-1) {
                //We don't require rules for the last entry - So only process what we can
//                       if (currentPageRules != null) {
//                           //Check that the last update's rule does NOT contain any of the previous updates' page numbers
//                           //Not quite sure if this branch of checks can be equated to failing for null rulesets before the last update...
//                           List<String> previousPages = updates.subList(0, i);
//                           for (String previousPage : previousPages) {
//                               if (currentPageRules.contains(previousPage)) {
//                                   passing = false;
//                                   failedIndex = i;
//                                   break;
//                               }
//                           }
//                       }
            } else {
                //Note that I tried to create this without a new ArrayList and caused a bug where
                //altering remainingPages would affect the original updates List
                List<String> remainingPages = new ArrayList<>(updates.subList(i+1,numberOfUpdates));

                // Simple check that works with ASSUMPTION 1:
                if (currentPageRules == null ) {
                    passing = false;
                    failedIndex = i;
//                    failedEntry = updates.get(i); //for debugging
                    firstConflictIndex = i+1;
                    break;
                }
                if (currentPageRules != null && !currentPageRules.containsAll(remainingPages)) {
                    passing = false;
                    failedIndex = i;
                    remainingPages.removeAll(currentPageRules);
                    failedEntry = remainingPages.get(0);
                    firstConflictIndex = updates.indexOf(failedEntry);
                    break;
                };
            }
        }

//        System.out.println(failedIndex);
//        System.out.println(failedEntry);
//        System.out.println(firstConflictIndex);
        return new AbstractMap.SimpleEntry<>(passing, new AbstractMap.SimpleEntry<>(failedIndex,firstConflictIndex));
    }

    public int findMiddleValue(List<String> updates) {
        //ASSUMPTION 2: There are only odd-length sets of updates with a true middle page
        int middlePageIndex = Math.floorDiv(updates.size(), 2);
        return Integer.parseInt(updates.get(middlePageIndex));
    }

    public void swap(List <String> list, int n, int o) {
        String s = list.get(n);
        list.set(n, list.get(o));
        list.set(o, s);
    }

    public void solvePart1(String path) {
        try {
            List<List<List<String>>> rulesAndUpdates = fileContents(path);

            //Variable for adding middle page numbers together
           AtomicInteger total = new AtomicInteger();

            //Create Page order rules
            HashMap<String, List<String>> rules = new HashMap<String, List<String>>();
            rulesAndUpdates.get(0).forEach(rule -> {
                addToMap(rules,rule.get(0), rule.get(1));
            });

            //Apply rules to sets of updates
            rulesAndUpdates.get(1).forEach(updateSet -> {

                if (passesRules(rules, updateSet).getKey()) {
                    //Find middle page number within each printable update
                    total.addAndGet(findMiddleValue(updateSet));
                }
            });

            System.out.println("The sum of the middle page values is: "+total);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        try {
            List<List<List<String>>> rulesAndUpdates = fileContents(path);
            List<List<String>> modifiedUpdates = new ArrayList<>();

            //Variable for adding middle page numbers together
            AtomicInteger total = new AtomicInteger();

            //Create Page order rules
            HashMap<String, List<String>> rules = new HashMap<String, List<String>>();
            rulesAndUpdates.get(0).forEach(rule -> {
                addToMap(rules,rule.get(0), rule.get(1));
            });

            //Apply rules to sets of updates
            rulesAndUpdates.get(1).forEach(updateSet -> {
                if (!passesRules(rules, updateSet).getKey()) {
                    modifiedUpdates.add(updateSet);
                }
            });
            //Apply rules to modified sets of updates
            modifiedUpdates.forEach(update -> {
                while(true) {  //Keep repeating steps until we hit a success criteria for the update set
                    Map.Entry<Boolean, Map.Entry<Integer, Integer>> u = passesRules(rules, update);

                    if (u.getKey()) {
                        //Find middle page number within each printable update
                        total.addAndGet(findMiddleValue(update));
                        break;
                    }

                    swap(update, u.getValue().getKey(), u.getValue().getValue());
                }
            });

//            System.out.println("final failed updates: "+modifiedUpdates);
            System.out.println("The sum of the middle page values is: "+total);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
