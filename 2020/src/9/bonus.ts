/**
 * Find contiguous set of numbers in input array that add to target (from part 1)
 * Then, add together the smallest and largest numbers in the range to produce a single number.
 */

import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("9.txt");
if (data) {
  const sequence: number[] = data.split("\n").map((x) => parseInt(x));
  const target = 1309761972;
  let sum = 0;
  let range: number[] = [];
  // Brute force check sections of original sequence until the sum is >= the target.
  // If at that point we are == to target, we found the winning series to sum.
  for (let i = 0; i < sequence.length; i++) {
    let final = -1;
    for (let f = i; f < sequence.length; f++) {
      sum = sequence.slice(i, f).reduce((acc, cur) => acc + cur, 0);
      if (sum >= target) {
        // console.log(i, f);
        final = f;
        break;
      }
    }
    if (sum === target) {
      range = [i, final]; //final is index after last used value (as used in slice input)
      break;
    }
  }
  const summandSection = sequence.slice(...range);
  const code = Math.min(...summandSection) + Math.max(...summandSection);
  console.log(`Found sum ${sum} in range: ${JSON.stringify(range)}`);
  console.log(`Encryption code is ${code}`);
}
