package AOC;

import com.sun.jdi.event.EventIterator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Day12 {

    private class Garden {
        private Character id;
        Integer total;
        Integer area;
        Integer perimeter;
        Garden(Character c) {
            this.id = c;
            this.total = 0;
            this.area = 0;
            this.perimeter = 0;
        }

        void sumPlot() {
            this.total += this.area * this.perimeter;
            this.area = 0;
            this.perimeter = 0;
//            this.locations.clear();
        }
    }

    private void printPlots(HashMap<String,Garden> plots) {
        plots.forEach((k,v) -> {
            System.out.println("Key: "+k);
            System.out.println("id: "+v.id);
            System.out.println("area: "+v.area);
            System.out.println("perimeter: "+v.perimeter);
            System.out.println("total: "+v.total);
        });
    }

    private void printPlot(Garden p) {
        System.out.println("id: "+p.id);
        System.out.println("area: "+p.area);
        System.out.println("perimeter: "+p.perimeter);
        System.out.println("total: "+p.total);
    }

    // Tell if xi,yi is connected to xf, yf
    //TODO: I think we have to keep track of ALL previously visited nodes to not enter inf loops for square configurations
    public boolean isConnected(List<String> lines, char c, int xi, int yi, int xf, int yf, String lastDirection, List<String> visited) {
        boolean result = xi == xf && yi == yf;
        int x;
        int y;
        //Up Check
        x = xi-1;
        y = yi;
        if (x >= 0 && lines.get(x).charAt(y) == c && lastDirection != "down") {
            if (!visited.contains(x+","+y)) {
                visited.add(x+","+y);
                return isConnected(lines,c,x,y,xf,yf,"up", visited);
            }
        }
        //Left Check
        x = xi;
        y = yi-1;
        if (y >= 0 && lines.get(x).charAt(y) == c && lastDirection != "right") {
            if (!visited.contains(x+","+y)) {
                visited.add(x+","+y);
                return isConnected(lines, c, x, y, xf, yf, "left", visited);
            }
        }
        //Down Check
        x = xi+1;
        y = yi;
        if (x < lines.size() && lines.get(x).charAt(y) == c && lastDirection != "up") {
            if (!visited.contains(x+","+y)) {
                visited.add(x+","+y);
                return isConnected(lines, c, x, y, xf, yf, "down", visited);
            }
        }
        //Right Check
        x = xi;
        y = yi+1;
        if ( y < lines.get(x).length() && lines.get(x).charAt(y) == c && lastDirection != "left") {
            if (!visited.contains(x+","+y)) {
                visited.add(x + "," + y);
                return isConnected(lines, c, x, y, xf, yf, "right", visited);
            }
        }

        return result;
    }

    public void solvePart1(String path) {
        try {
            AtomicReference<Integer> answer = new AtomicReference<>(0);

            //Build lists of plots:
            HashMap<String,Garden> plots = new HashMap<>();
            List<String> lines = Files.lines(Path.of(path)).toList();

            //See how plots interact with one another to build perimeters
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                for (int j = 0; j < line.length(); j++) {
                    char c = line.charAt(j);
                    String cs = Character.toString(c);
                    //
                    if (!plots.containsKey(cs)) {
                        plots.put(cs, new Garden(c));
                    }
                    //
                    Garden plot = plots.get(cs);
                    Integer walls = 4; //Inverse of connections to more parts of same plot
                    int x;
                    int y;

                    //Use to tell when we are done with a given plot, due to direction of traversal
                    Boolean rightOrDown = false;
                    Boolean plotDone = false;

                    //Up Check
                    x = i-1;
                    y = j;
                    if (x >= 0 && lines.get(x).charAt(y) == plot.id) {
//                        System.out.println("FOUND: "+lines.get(x).charAt(y)+" Above");
                        walls -= 1;
                    }
                    //Left Check
                    x = i;
                    y = j-1;
                    if (y >= 0 && lines.get(x).charAt(y) == plot.id) {
//                        System.out.println("FOUND: "+lines.get(x).charAt(y)+" Left");
                        walls -= 1;
                    }
                    //Down Check
                    x = i+1;
                    y = j;
                    if (x < lines.size() && lines.get(x).charAt(y) == plot.id) {
//                        System.out.println("FOUND: "+lines.get(x).charAt(y)+" Down");
                        walls -= 1;
                        rightOrDown = true;
                    }
                    //Right Check
                    x = i;
                    y = j+1;
                    if ( y < line.length() && lines.get(x).charAt(y) == plot.id) {
//                        System.out.println("FOUND: "+lines.get(x).charAt(y)+" Right");
                        walls -= 1;
                        rightOrDown = true;
                    }

                    plot.area +=1;
                    plot.perimeter+=walls;

//                    if (c == 'C') {
//                        printPlot(plot);
//                    }

                    if (!rightOrDown) {
                        // Check the rest of the current line for other down instances
                        // If we don't find the down check anywhere in the row, we are done
                        plotDone = true;

                        for (int j2 = 0; j2 < line.length(); j2++) {
                            char c2 = line.charAt(j);
                            String cs2 = Character.toString(c2);
                            Garden plot2 = plots.get(cs2);
                            //Down Check
                            x = i+1;
                            y = j2;
                            if (x < lines.size()
                                    && lines.get(x).charAt(y) == plot2.id
                            && isConnected(lines,plot2.id,i,j2,x,y, "default", new ArrayList<>())) {
//                              System.out.println("FOUND: "+lines.get(x).charAt(y)+" Down");
                                plotDone = false;
                                break;
                            }
                        }
                    }

                    if (plotDone) {
                        plot.sumPlot();
                    }
                }
            }
//            printPlots(plots);

            //Add up all the perimeters to create a price
            plots.forEach((k,v) -> {
                answer.updateAndGet(v1 -> v1 + v.total);
            });
            System.out.println("The total price of all the plots is: "+answer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
