package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Day11 {

    public List<Double> readFileToList(String path) throws IOException {
        return Arrays.stream(Files.lines(Path.of(path)).toList().get(0).split(" ")).map(s -> Double.parseDouble(s)).toList();
    }

    public void blink(List<Double> list) {
        int i = 0;
        while (i < list.size()) {
//            System.out.println("i: "+ i);
            Double val = list.get(i);
            String stringVal = String.format ("%.0f", val);
//            System.out.println(stringVal);
            if (val == 0) {
                list.set(i, 1.0);
            } else if (stringVal.length() % 2 == 0) {
                String beg = stringVal.substring(0,stringVal.length()/2);
                String end = stringVal.substring(stringVal.length()/2);
                list.set(i,Double.parseDouble(beg));
                list.add(i+1,Double.parseDouble(end));
                i++; //don't immediately reprocess the new list element
            } else {
                list.set(i, val*2024);
            }
            i++;
        }
    }

    public ConcurrentHashMap<Double, Counter> mapFromList(List<Double> list) {
        ConcurrentHashMap<Double, Counter> map = new ConcurrentHashMap<>();
        list.forEach(item -> {
            map.put(item, new Counter(1.0, false));
        });
        return map;
    }

    Double handleNullMapGet(Map<Double, Double> map, Double key) {
       return map.get(key) != null ? map.get(key) : 0.0;
    }

    Double handleNullCounterMapGet(Map<Double, Counter> map, Double key) {
        return map.get(key) != null ? map.get(key).count : 0.0;
    }

    void incrementMapValue(HashMap<Double, Double> ogMap, Double originalK,ConcurrentHashMap<Double, Counter> map, Double k) {
//        Double v = handleNullMapGet(map,originalK);
//        Double vn = v == 0.0 ? v+1.0 : 2.0*v;
//        if (originalK == 2.0) {
//            System.out.println(originalK);
//            System.out.println(k);
//            System.out.println(handleNullMapGet(ogMap,originalK));
//        }
        map.put(k, new Counter(handleNullCounterMapGet(map,k)+handleNullMapGet(ogMap, originalK), true));
    }
    void decrementMapValue(HashMap<Double, Double> ogMap, Double originalK,ConcurrentHashMap<Double, Counter> map, Double k) {
//        Double v = handleNullMapGet(map,k);
//        Double vn = v == 1.0 ? 0.0 : v/2;
//        map.put(k,new Counter(Math.max(vn, 0.0),skips));
        map.put(originalK, new Counter(handleNullMapGet(ogMap,originalK)-handleNullCounterMapGet(map,k), true));
    }

    public static class Counter {
        private Double count;
        private Boolean processed;

        public Counter(Double count, Boolean processed) {
            this.count = count;
            this.processed = processed;
        }
    }

    public void printMap(ConcurrentHashMap<Double, Counter> map){
        for (HashMap.Entry<Double, Counter> entry : map.entrySet()) {
            if (entry.getValue().count > 0) {
                System.out.println("Key: " + entry.getKey() + ", Count: " + entry.getValue().count + ", Processed: " + entry.getValue().processed);
            }
        }
    }

    public void resetMap(ConcurrentHashMap<Double, Counter> map) {
        for (HashMap.Entry<Double, Counter> entry : map.entrySet()) {
            entry.getValue().processed = false;
        }
    }

    /**
     * Attempt to limit list size for memory / time constraints
     */
    public void blinkHashing(ConcurrentHashMap<Double, Counter> map) {
        int i = 0;
        //Take note of previous state of map before we start transforming it
        // Deep copy to a HashMap
        HashMap<Double, Double> originalMap = new HashMap<>();
        map.forEach((key, value) -> originalMap.put(key, value.count));

        for (HashMap.Entry<Double, Counter> entry : map.entrySet()) {
//            System.out.println("Index: " + i + ", Key: " + entry.getKey() + ", Count: " + entry.getValue().count);
            Double k = entry.getKey();
            String stringK = String.format ("%.0f", k);

            //previously: !entry.getValue().processed && entry.getValue().count > 0
            if (handleNullMapGet(originalMap,k) > 0) {//If some number of the entry exists
                Double newK;
                if (k == 0) {
                    incrementMapValue(originalMap, k, map,1.0);
                    decrementMapValue(originalMap, k, map, 1.0);
                } else if (stringK.length() % 2 == 0) {
                    String beg = stringK.substring(0,stringK.length()/2);
                    String end = stringK.substring(stringK.length()/2);
                    incrementMapValue(originalMap,k,map,Double.parseDouble(beg));
                    incrementMapValue(originalMap,k,map,Double.parseDouble(end));
                    decrementMapValue(originalMap, k, map, Double.parseDouble(beg));
                    decrementMapValue(originalMap, k, map, Double.parseDouble(end));
                } else {
                    if (k == 7.0) {
                        System.out.println(originalMap.get(k));
                        System.out.println(map.get(k).count);
                        System.out.println(map.get(k*2024));
                    }
                    incrementMapValue(originalMap,k,map,k*2024);
                    decrementMapValue(originalMap, k, map, k*2024);
                    if (k == 7.0) {
                        System.out.println(map.get(14168.0).count);
                        System.out.println(map.get(k).count);
                    }
                }
                //Remove our count of previous value
//                map.put(k, new Counter(0.0, false));
            }

            //don't reprocess new map element(s) this iteration
//            if (map.size() > currentMapSize) {
//                i+=(map.size()-currentMapSize);
//            }
            i++;
        }
    }

    public void solvePart1(String path) {
        try {
            Integer blinkCount = 25;
            List<Double> l = new ArrayList<>();
            List<Double> file = readFileToList(path);
            file.forEach(num -> l.add(num));
//            System.out.println(file);
            for (int i = 0; i < blinkCount; i++) {
                System.out.println(i+" of "+blinkCount+" ...");
                blink(l);
            }
//            System.out.println(l);
            System.out.println("The number of stones after "+blinkCount+" blinks is: "+l.size());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        try {
            Double result = (double) 0;
//            Integer blinkCount = 75;
            //TODO: check where things go awry - I think blink #6 is where it deviates
            Integer blinkCount = 6;
            List<Double> l = new ArrayList<>();
            List<Double> file = readFileToList(path);
            file.forEach(num -> l.add(num));
            ConcurrentHashMap<Double, Counter> map = mapFromList(l);
//            System.out.println(file);
            //Since I processed step 0, this number seems to be behind compared to part 1...
            //Adjusted upper limit of for loop.
            for (int i = 0; i < blinkCount; i++) {
                resetMap(map);
                blinkHashing(map);
            }
//            System.out.println(map);
            for (Counter value : map.values()) {
                result += value.count;
            }
            printMap(map);
            System.out.println("The number of stones after "+blinkCount+" blinks is: "+result);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
