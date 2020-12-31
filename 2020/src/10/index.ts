/**
 * From a distribution of adapter values, determine how many "joltage" jumps are made,
 * assuming that you can chain adapters from 0 "jolts" to your device, which takes the highest
 * adapter value + 3. Only jumps up to 3 jolts are allowed.
 *
 * Perform some arbitrary calculations with these values to get final output number:
 * "What is the number of 1-jolt differences multiplied by the number of 3-jolt differences?"
 */

import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("10.txt");
if (data) {
  // Based on the problem statement, we need to insert the differences in joltage
  // between the outlet (0 jolts) and the 1st adapter, and that b/n the last adapter and our machine (+3)
  const arr = data.split("\n").map((x) => parseInt(x));
  arr.unshift(0);
  arr.push(Math.max(...arr) + 3);
  // Sort the data, then tally up the differences in jolts between each adapter
  const seq = new Uint16Array(arr).sort();
  const tallies = Array(4).fill(0);
  for (let i = 1; i < seq.length; i++) {
    const diff = seq[i] - seq[i - 1];
    tallies[diff] += 1;
  }
  const answer = tallies[1] * tallies[3];
  console.log(`${tallies} => ${answer}`);
}
