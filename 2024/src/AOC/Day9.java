package AOC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day9 {

    public String readFileToString(String path) throws IOException {
         return Files.lines(Path.of(path)).toList().get(0);
    }

    public List<Integer> idBlocks(String diskmap) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < diskmap.length(); i++) {
            int times = Integer.parseInt(Character.toString(diskmap.charAt(i)));
            //Start @0 with even numbers being files and odd being empty space
            if (i % 2 == 0 ) {
                for (int j = 0; j < times; j++) {
                    //Adding the ID = counting only files and ignoring spaces (odd indices)
                    result.add(i/2);
                }
            } else {
                for (int j = 0; j < times; j++) {
                    result.add(null);
                }
            }
        }
        return result;
    }

    private int countNulls(List<Integer> list) {
        return list.stream().reduce(0, (count, element) -> element == null ? count + 1 : count);
    }

    public boolean isSorted(List<Integer> list) {
        int count = countNulls(list);
        return list.subList(list.size()-count,list.size()).stream().allMatch(Objects::isNull);
    }

    public void swap(List <Integer> list, int n, int o) {
        Integer s = list.get(n);
        list.set(n, list.get(o));
        list.set(o, s);
    }

    /**
     *
     * moves IDs in list from end into available null spaces one at a time
     */
    public void moveEndIDToBeginningSpace(List <Integer> list) {
        //Find the earliest null and latest ID
        int earlyNullIndex = -1;
        int lateIDIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            //stop once found
            if (list.get(i) == null) {
                earlyNullIndex = i;
                break;
            }
        }
        for (int i = list.size()-1; i >= 0; i--) {
            //stop once found
            if (list.get(i) != null) {
                lateIDIndex = i;
                break;
            }
        }
        if (earlyNullIndex == -1 || lateIDIndex == -1) {
            throw new Error("Something unexpected happened!");
        }
        swap(list,earlyNullIndex,lateIDIndex);
    }
    /**
     *
     * Iterates over entire list to move groups of IDs from end into
     * available null spaces that have enough sequential nulls to fit
     *
     */
    public void moveEndBlockToBeginningSpace(List <Integer> list) {
        //Starting from end of list...
        for (int x = list.size()-1; x > 0; x--) {
            if (list.get(x) != null) {
                //Find the earliest null and latest ID
                int lateIDIndex = -1;
                int lateIDSize = 0;
                List<Integer[]> nulls = new ArrayList<>();
                //does size -2 cover the upper limit?
                for (int i = 0; i < list.size()-2; i++) {
                    int earlyNullIndex = -1;
                    int earlyNullSize = 0;
                    if (list.get(i) == null) {
                        earlyNullIndex = i;
                        int n = i + 1;
                        earlyNullSize = 1;
                        while (n < list.size() && list.get(n) == null) {
                            earlyNullSize += 1;
                            n++;
                            i++;
                        }
                        nulls.add(new Integer[]{earlyNullIndex, earlyNullSize});
                    }
                }
                for (int i = x; i > 1; i--) {
                    //stop once found
                    if (list.get(i) != null) {
                        lateIDIndex = i;
                        int n = i - 1;
                        lateIDSize = 1;
                        while (Objects.equals(list.get(n), list.get(i))) {
                            lateIDSize += 1;
                            n--;
                        }
                        break;
                    }
                }
                int finalLateIDSize = lateIDSize;
                if (lateIDIndex == -1) {
                    System.out.println("Something unexpected happened!");
                    finalLateIDSize = 2;
                } else {
                    int finalLateIDIndex = lateIDIndex;
//                    System.out.println(lateIDIndex);
//                    System.out.println(lateIDSize);
                    for (Integer[] knull : nulls) {
                        if (finalLateIDSize <= knull[1] && knull[0] < finalLateIDIndex-finalLateIDSize-1) {
                            int c1 = finalLateIDSize - 1;
                            int c2 = 0;
                            while (c1 >= 0) {
//                                System.out.println("index A: "+(knull[0] + c2));
//                                System.out.println("index B: "+(finalLateIDIndex - c1));
                                swap(list, knull[0] + c2, finalLateIDIndex - c1);
                                c1--;
                                c2++;
                            }
//                            System.out.println(list);
                            break;
                        }
                    }
                }
                //move backward to not reprocess same block
                x -= (finalLateIDSize-1);
            }
        }
    }


    public long computeChecksum(List<Integer> list) {
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                sum += i * list.get(i);
            }
        }
        return sum;
    }

    public void solvePart1(String path) {
        try {
            String dm = readFileToString(path);
            List<Integer> ids = idBlocks(dm);
//            System.out.println(ids);
            while (!isSorted(ids)) {
                moveEndIDToBeginningSpace(ids);
            }
//            System.out.println(ids);
            long chsm = computeChecksum(ids);
            System.out.println("The filesystem checksum is: "+chsm);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void solvePart2(String path) {
        try {
            String dm = readFileToString(path);
            List<Integer> ids = idBlocks(dm);
//            System.out.println(ids);
            moveEndBlockToBeginningSpace(ids);
//            System.out.println(ids);
            long chsm = computeChecksum(ids);
            System.out.println("The filesystem checksum is: "+chsm);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
