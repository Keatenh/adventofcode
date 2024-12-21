package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {

    public boolean isAlphaNumeric(String s){
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    public ArrayList<List<Integer>> fileContents(String path) throws IOException {
        ArrayList<List<Integer>> rows = new ArrayList<>();
        List<String> lines = Files.lines(Path.of(path)).collect(Collectors.toList());
        Files.lines(Path.of(path)).forEach(
                (line) -> {
                    List<Integer> row = Arrays.stream(line.split("")).map(c -> isAlphaNumeric(c) ? Integer.parseInt(c) : null).collect(Collectors.toList());
                    rows.add(row);
                });
        return rows;
    }

    class Answer {
        public Integer total;
        private Set<List<Integer>> summits;
        public Answer() {
            this.total = 0;
            this.summits = new HashSet<>();
        }
        public void increment() {
            this.total++;
        }

        public void addSummit(List<Integer> summit) {
            this.summits.add(summit);
        }

    }

    public void climb(List<List<Integer>> map, int height, int x, int y, Answer ans, int xi, int yi) {
//        System.out.println("Working at "+x+" , "+y+" , height: "+height);
        //Exit recursion if we have reached a summit
        if (height == 9) {
//            System.out.println("Incrementing at "+x+" , "+y+" , height: "+height);
            ans.increment();
            //Using the original x and y for uniqueness, passed through as xi, yi
            ans.addSummit(Arrays.asList(xi,yi,x,y));
            return;
        }
        if (x > 0) {
            Integer left = map.get(x-1).get(y);
            if (left != null && left == height+1) {
                climb(map,height+1,x-1, y, ans, xi, yi);
            }
        }
        if (x < map.size()-1) {
            Integer right = map.get(x+1).get(y);
            if (right != null && right == height+1) {
                climb(map,height+1,x+1, y, ans, xi, yi);
            }
        }
        if (y > 0) {
            Integer up = map.get(x).get(y-1);
            if (up != null && up == height+1) {
                climb(map,height+1,x, y-1, ans, xi, yi);
            }
        }
        if (y < map.get(x).size() - 1) {
            Integer down = map.get(x).get(y+1);
            if (down != null && down == height+1) {
                climb(map,height+1,x, y+1, ans, xi, yi);
            }
        }
        //did not make it to any condition -> dead end
    }

    public void solveParts(String path) {
        try {
            List<List<Integer>> map = fileContents(path);
//            System.out.println(map);
            Answer ans = new Answer();
            for (int i = 0; i < map.size(); i++) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    if (map.get(i) != null && map.get(i).get(j) != null) {
                        if (map.get(i).get(j) == 0) {
//                            System.out.println("Starting at "+i+" , "+j);
                            climb(map,0,i,j,ans,i,j);
                        }
                    }
                }
            }
            System.out.println("The achievable summits (per path) are: " + ans.summits);
            System.out.println("The sum of the scores of all trailheads is: " + ans.summits.size());
            System.out.println("The total paths of all trailheads is: " + ans.total);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
