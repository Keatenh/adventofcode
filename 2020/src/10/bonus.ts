/**
 * Find the combinations of adapters that can be used (Up to +3 joltage gaps)
 *
 * Following are forum explanations for others' individual solutions:
 *
 * All items in the list start with 0 paths leading to them
 * The first item in the list starts with 1 path leading to it
 * Loop through the items and evey item adds its number of paths to all the items it can reach
 *
 * For any given point in the (sorted) list, there at most 3 possible places to originate from.
 * For each of those places, add the number of steps needed to reach that place,
 *
 * Order the entire list into ascending order, then break it up into groups.
 * A group definition is that it can only have one "entrance" and one "exit",
 * meaning groups placed next to each other can only jump to 1 adapter in the next or previous group.
 * E.g, [0,1,2,3], [6, 7, 8], [11, 12, 13] (since the jolt difference is 3)
 * Then, calculate how many unique ways a group can be arranged. Multiply these numbers together.
 */
// const input = [0, 1, 3, 4];
// const input = [16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4];
import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("10.txt");
if (data) {
  const input = data.split("\n").map((x) => parseInt(x));
  input.unshift(0);
  input.push(Math.max(...input) + 3);
  const seq = [...new Uint16Array(input).sort()];

  // Create segments of islands that cannot be rearraged (>3 jolts gap)
  const segments: number[][] = [];
  let marker = 0;
  for (let i = 0; i < seq.length; i++) {
    // Everytime a +3 gap is found, we define a segment to optimize
    if (seq[i + 1] - seq[i] >= 3) {
      segments.push(seq.slice(marker, i + 1));
      marker = i + 1;
    }
  }
  // console.log(segments);
  let answer = 1;
  /**
   * Loop over segments, taking the product of the combinations
   * that can be made with each given stretch of numbers.
   */
  for (let i = 0; i < segments.length; i++) {
    const n = segments[i].length;
    answer *= maxDiff3Combination(n);
  }
  console.log(`${answer} combinations found.`);
}

/**
 * Created a pattern to how many combinations one can have with sorted,
 * nonrepeating numbers with a max delta of +3 from one another
 * (see explicitAdaptorCombination below).
 * Then searched online for a general form that could be applied for n > 0,
 * This way, the program should be able to handle any stretch of sequencial numbers.
 * @param n
 */
function maxDiff3Combination(n: number): number {
  if (n > 0) {
    return 1 + ((n - 2) * (n - 1)) / 2;
  }
  return 1;
}

// function explicitAdaptorCombination(x: number): number {
//   return [1, 1, 1, 2, 4, 7][x];
// }
// [0, 1, 2, 3, 4, 5, 6].forEach((x) => {
//   console.log(`${x} => ${explicitAdaptorCombination(x)}`);
//   console.log(`${x} => ${maxDiff3Combination(x)}`);
// });
