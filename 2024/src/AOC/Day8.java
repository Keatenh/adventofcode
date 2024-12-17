package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day8 {


    public boolean isAlphaNumeric(String s){
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    public <T> void printArray(T[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    public <T> void printArrayList(List<T[]> list) {
        System.out.println(list.stream().map(Arrays::toString).toList());
    }

    public void printLocations(HashMap<Character, List<Integer[]>> locations) {
        locations.forEach((k, v) -> {
            System.out.println(k);
            printArrayList(v);
        });
    }

    /**
     * Some methods that allow us to utilize Lists with unique primitive value pairs in arrays
     * (intead of using Set, for example)
     */
    public String createStringHash(Integer[] arr) {
        return String.format("%s,%s",arr[0],arr[1]);
    }

    public boolean checkUnique2DArray(List<Integer[]> list, Integer[] arr) {
        String hash = createStringHash(arr);
        List<String> checker = list.stream().map(this::createStringHash).toList();
        return !checker.contains(hash);
    }

    public class MapInfo {
        private Integer maxX;
        private Integer maxY;
        HashMap<Character, List<Integer[]>> antennaLocations;
        List<Integer[]> antinodeLocations;

        //constructor
        public MapInfo(String path) throws IOException {
            this.antennaLocations = new HashMap<Character, List<Integer[]>>();
            this.antinodeLocations = new ArrayList<>();
            List<String> lines = Files.lines(Path.of(path)).toList();
            this.maxX = lines.size() - 1;
            this.maxY = -1;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (this.maxY == -1) {
                    this.maxY = line.length() - 1;
                }
                for (int j = 0; j < line.length(); j++) {
                    char c = line.charAt(j);
                    //character is an antenna
                    if (isAlphaNumeric(Character.toString(c))) {
                        List<Integer[]> locations = antennaLocations.get(c);
                        if (locations == null) {
                            List<Integer[]> newLocations = new ArrayList<>();
                            newLocations.add(new Integer[] {i, j});
                            antennaLocations.putIfAbsent(c, newLocations);
                        }
                        else { //Add additional locations to our character map of antennae
                            locations.add(new Integer[] {i, j});
                        }
                    }

                }
//                printLocations(antennaLocations);
            }
        }

        //TODO: more looping to check against every combination of matching locations
        //e.g 0->1, 0->2, 0->3, 1->2, 1->3, 2->3
        public void visitAntennae() {
            this.antennaLocations.forEach((character,locations) -> {
                //Getting deltas between any 2 locations means we skip the 1st or last entry
                for (int j = 0; j < locations.size(); j++) {
                    for (int n = j+1; n < locations.size(); n++) {
                        Integer[] cur = locations.get(n);
                        Integer[] prev = locations.get(j);
                        //debug
//                        System.out.println("char: "+character);
//                        System.out.println("j: "+j);
//                        System.out.println("n: "+n);
//                        printArray(cur);
//                        printArray(prev);
                        Integer[] delta = new Integer[] {cur[0] - prev[0], cur[1] - prev[1]};
//                        printArray(delta);
                        //Create antinodes at distances that match the deltas to ensure they are
                        //2x as far from one node as the other
                        Integer[] low = new Integer[] {prev[0] - delta[0], prev[1] - delta[1]};
                        Integer[] high = new Integer[] {cur[0] + delta[0], cur[1] + delta[1]};
                        //Record the antinode locations if:
                        // 1. they are in bounds for the map of antennae
                        // 2. they are a unique location (not previously added by other antennae)
                        if (low[0] >= 0 &&
                                low[0] <= this.maxX &&
                                low[1] >= 0 &&
                                low[1] <= this.maxY &&
                                checkUnique2DArray(this.antinodeLocations,low)
                        ) {
                            this.antinodeLocations.add(low);
                        }
                        if (high[0] >= 0 &&
                                high[0] <= this.maxX &&
                                high[1] >= 0 &&
                                high[1] <= this.maxY &&
                                checkUnique2DArray(this.antinodeLocations,high)
                        ) {
                            this.antinodeLocations.add(high);
                        }
                    }
                }
            });
        }

        public void visitAntennaePart2() {
            this.antennaLocations.forEach((character,locations) -> {
                //Getting deltas between any 2 locations means we skip the 1st or last entry
                for (int j = 0; j < locations.size(); j++) {
                    for (int n = j+1; n < locations.size(); n++) {
                        Integer[] cur = locations.get(n);
                        Integer[] prev = locations.get(j);
                        Integer[] delta = new Integer[] {cur[0] - prev[0], cur[1] - prev[1]};
                        //Create antinodes at distances that match any multiple of the deltas in bounds
                        //Starting from zero, since the requirements now state that
                        // the antinodes can contain the antenna position itself
                        int w = 0;
                        while(prev[0] - delta[0]*w <= this.maxX &&
                                prev[1] - delta[1]*w <= this.maxY &&
                                prev[0] - delta[0]*w >= 0 &&
                                prev[1] - delta[1]*w >= 0
                        ) {
                            Integer[] low = new Integer[] {prev[0] - delta[0]*w, prev[1] - delta[1]*w};
                            if (checkUnique2DArray(this.antinodeLocations,low)) {
                                this.antinodeLocations.add(low);
                            }
                            w++;
                        }
                        w = 0;
                        while(cur[0] + delta[0]*w <= this.maxX &&
                                cur[1] + delta[1]*w <= this.maxY &&
                                cur[0] + delta[0]*w >= 0 &&
                                cur[1] + delta[1]*w >= 0) {
                            Integer[] high = new Integer[] {cur[0] + delta[0]*w, cur[1] + delta[1]*w};
                            if (checkUnique2DArray(this.antinodeLocations,high)) {
                                this.antinodeLocations.add(high);
                            }
                            w++;
                        }
                    }
                }
            });
        }

    }


    /**
     *
     * OBJECTIVE: Find all the unique "antinode" locations within the bounds of the map
     */
    public void solvePart1(String path) {
        try {
            MapInfo mi = new MapInfo(path);
//            printLocations(mi.antennaLocations);
//            System.out.println(mi.maxX);
//            System.out.println(mi.maxY);
            mi.visitAntennae();
            printArrayList(mi.antinodeLocations);
            System.out.println("Found "+mi.antinodeLocations.size()+ " unique antinode locations in bounds.");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Same goal as part 1, with newly allowed forms of antinodes
     */
    public void solvePart2(String path) {
        try {
            MapInfo mi = new MapInfo(path);
            mi.visitAntennaePart2();
            //Sort to compare result with example answer
            mi.antinodeLocations.sort((a, b) -> {
                if (a[0] == b[0]) {
                    return Integer.compare(a[1], b[1]);
                }
                return Integer.compare(a[0], b[0]);
            });
            printArrayList(mi.antinodeLocations);
            System.out.println("Found "+mi.antinodeLocations.size()+ " unique antinode locations in bounds.");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
