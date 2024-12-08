package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day6 {

    public class MapInfo {
        //Note: I have infected the whole class with Integer types instead of int for apparent ease of use of logging helper functions
        private List<Integer[]> obstaclePositions;
        private Integer[] mapDimensions;
        private Integer[] guardPosition;
        private Integer[] guardDirection;
        private Integer[] startPosition;
        private Integer[] startDirection;
        private boolean done = false;
        private Set<List<Integer>> visitedNodes;
        private List<Integer[]> freePositions;
        private int loopingVersions;
        private Integer[] addedObstacle;
        private Set<String> visitedNewObstacleNodes;
        private int stepCount;
        private int maxLeaveSteps; //The most steps any iteration of step() took to leave map successfully

        //constructor
        public MapInfo(String path) throws IOException {
            this.obstaclePositions = new ArrayList<>();
            AtomicInteger width = new AtomicInteger();
            AtomicInteger height = new AtomicInteger();
            this.freePositions = new ArrayList<>();
            this.addedObstacle = new Integer[]{};
            this.stepCount = 0;
            this.maxLeaveSteps = 0;

            List<String> lines = Files.lines(Path.of(path)).collect(Collectors.toList());
            for (int i = 0; i < lines.size(); i++) {
                    if (width.get() == 0) {
                        width.set(lines.get(i).length());
                    }
                    char[] positions = lines.get(i).toCharArray();
                    for (int j = 0; j < positions.length; j++) {
                        char currentChar = positions[j];
                        if (currentChar == '#') {
                            this.obstaclePositions.add(new Integer[] {i, j});
                        } else if (currentChar == '.') {
                            this.freePositions.add(new Integer[]{i,j});
                        }
                        else {
                            this.guardPosition = new Integer[]{i,j};
                            this.guardDirection = interpretDirection(currentChar);
                        }
                    }
                    height.getAndIncrement();
            }
            this.mapDimensions = new Integer[]{width.get(), height.get()};
            this.visitedNodes = new HashSet<>();
            this.startPosition = new Integer[]{this.guardPosition[0],this.guardPosition[1]};
            this.startDirection = new Integer[]{this.guardDirection[0],this.guardDirection[1]};
            this.loopingVersions = 0;
            this.visitedNewObstacleNodes = new HashSet<>();
            /**
             * Note: the set of visited Nodes needs LIST type entries to properly compare values and not references to memory (like Arrays)
             * Importantly, my Solution 1 implementation "worked" for the example problem WITHOUT deduping the repeated positions,
             * but this now works for all the other requested input ¯\_(ツ)_/¯
             */
            this.visitedNodes.add(Arrays.asList(this.guardPosition)); //Add initial position as visited
        }

        //Guard movement
        public void step() {
            Integer[] intention =  new Integer[]{this.guardPosition[0]+this.guardDirection[0],this.guardPosition[1]+this.guardDirection[1]};

            //collision with boundary
            if (intention[0] < 0 || intention[0] > this.mapDimensions[0] ||
                    intention[1] < 0 || intention[1] > this.mapDimensions[1]) {
//                System.out.println("LEFT THE LAB!");
                this.maxLeaveSteps = Math.max(this.stepCount, this.maxLeaveSteps);
                this.done = true;
            }
            //collision with obstacle
            else if  (listContainsArray(this.obstaclePositions,intention)) {
                this.guardDirection = rotateCW(this.guardDirection[0], this.guardDirection[1]);
            }
            //free movement
            else {
                this.guardPosition = intention;
                this.visitedNodes.add(Arrays.asList(intention));
            }
//            printArray(this.guardPosition);

            // Check if we didn't already collide with bounds to escape before checking for infinite loop conditions
            if(!this.done) {
                // Collision with previous position + direction (looping indicator)
                if (this.visitedNewObstacleNodes.contains(hashPosAndDir())){
//                    System.out.println("DETECTED INFINITE LOOP!");
//                    System.out.println(this.visitedNewObstacleNodes);
                    this.loopingVersions++;
                    this.done = true;
                }
                this.visitedNewObstacleNodes.add(hashPosAndDir());
            }
            this.stepCount++;
        }

        public void addObstacle(int x, int y) {
            Integer[] i = new Integer[] {x, y};
            this.obstaclePositions.add(i);
            this.addedObstacle = i;
        }

        public void resetMap() {
            //remove added obstacle
            this.obstaclePositions.removeLast();
            this.addedObstacle = new Integer[]{};
            this.visitedNodes.clear();
            this.visitedNewObstacleNodes.clear();
            this.guardPosition = this.startPosition;
            this.guardDirection = this.startDirection;
            this.stepCount = 0;
            this.done = false;
        }

        public String hashPosAndDir() {
            String hsh = createStringHash(this.guardPosition[0], this.guardPosition[1], this.guardDirection[0], this.guardDirection[1]);
            return hsh;
        }
    }

    //Rotates coordinates 90 degrees clockwise (R turn)
    public static Integer[] rotateCW(Integer x, Integer y) {
        return new Integer[]{y, -x};
    }

    public static Integer[] interpretDirection(char c) {
        Integer[] dir;
        switch(c) {
            case '^':
                // North
                dir = new Integer[]{-1, 0};
                break;
            case '>':
                // East
                dir = new Integer[]{0, 1};
                break;
            case 'v':
                // South
                dir = new Integer[]{1, 0};
                break;
            default:
                dir = new Integer[]{0, -1};
                // West ('<')
        }
        return dir;
    }

    // Note: This was an unnecessarily complicated hashing function which also did not offer uniqueness at the 130x130x4 input scale
    // Ended up using createStringHash() instead
    public static int computeHash(int x, int y, int dx, int dy) {
        // Combine x, y, dx, and dy into a single hash
        // Assuming x, y, dx, and dy are bounded to small values (e.g., -1000 to 1000)
        int prime = 31; // A prime number for better distribution
        int hash = x * prime + y;
        hash = hash * prime + dx;
        hash = hash * prime + dy;
        return hash;
    }

    public static String createStringHash(int x, int y, int dx, int dy) {
     return String.format("%s,%s,%s,%s",x,y,dx,dy);
    }

    public <T> void printArray(T[] arr) {
        System.out.println(Arrays.toString(arr));
    }
    public <T> void printArrayList(List<T[]> list) {
        System.out.println(list.stream().map(Arrays::toString).toList());
    }

    public static boolean listContainsArray(List<Integer[]> list, Integer[] target) {
        for (Integer[] array : list) {
            // Use Arrays.equals to compare the content of the arrays, NOT their references
            if (Arrays.equals(array, target)) {
                return true;
            }
        }
        return false;
    }

    public void solvePart1(String path) {
        try {
            MapInfo mi = new MapInfo(path);
            while(!mi.done) {
                mi.step();
            }
            System.out.println("Visited "+mi.visitedNodes.size()+" distinct positions.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * PART 2
     * Find all positions for a single obstacle that would keep the guard in a loop
     *     Boundary: Cannot place obstacle at start position
     *
     *     Plan: Add one obstacle at a time to all empty nodes, running through the steps until exit
     *     -OR-
     *     X Until the guard position AND direction match the starting values,
     *         (Showed with adding obstacle {9,7} that this is not the only criteria for loop)
     *     ✓ We visit a node while repeating the same position & direction as ANY previous step
     *     THEN, increment the number of infinite loop possibilities
     */

    public void solvePart2(String path) {
        try {
            MapInfo mi = new MapInfo(path);
            int iterations = 0;
            for (Integer[] freePos :  mi.freePositions) {

                mi.addObstacle(freePos[0], freePos[1]);
//                printArray(mi.addedObstacle);
                while (!mi.done) {
                    mi.step();
                }
                mi.resetMap();
                iterations++;
//                System.out.println(iterations+ " simulations of "+mi.freePositions.size()+" completed. "+mi.loopingVersions+" versions found so far...");
//                System.out.println("Maximum number of steps for a leaving configuration: "+mi.maxLeaveSteps);
            }

            System.out.println("Found "+mi.loopingVersions+" number of distinct looping configurations.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

