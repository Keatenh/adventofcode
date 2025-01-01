import AOC.Day12;

public class Main {
    public static void main(String[] args) {
        Day12 solution = new Day12();
        solution.solvePart1("input/Day12/example.txt");
        solution.solvePart1("input/Day12/example2.txt");
        //TODO: C in this example includes a value it shouldn't in 1 group intead of 2
        // 15x32, instead of 14x28 + 1x4
        // -> checks for down links on same line does not have to be connected to current node
//        solution.solvePart1("input/Day12/example3.txt");
//        solution.solvePart1("input/Day12/input.txt");
//        solution.solvePart2("input/Day12/example.txt");
//        solution.solvePart2("input/Day12/input.txt");
    }
}