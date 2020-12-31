/**
 * For given n, start checking a finite sequence of numbers (starting at index n)
 * to find the member that fails a check that the previous n members contain at least
 * 1 pair of summands for the current value.
 *
 * e.g,
 * n = 5
 * sequence: 35,20,15,25,47,40,62,55,65,95,102,117,150,182,127,219
 *                           ^ Starting value               ^ 1st Failing value
 * ...,[ 95,102,117,150,182 ],<127>, ... => None of these sum to 127
 *
 */
import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("9.txt");
if (data) {
  const sequence: number[] = data.split("\n").map((x) => parseInt(x));
  const n = 25; // Size of "preamble" and of relative range where we can find summands
  // Check over each section of size n to see if there exists 2 numbers
  // that sum to the current value; Exit when there are none.
  for (let i = n; i < sequence.length; i++) {
    const target = sequence[i];
    const section = sequence.slice(i - n, i);
    const summands = twoSum(section, target);
    if (summands.length === 0) {
      console.log(`Value ${sequence[i]} breaks summation rule for input`);
      break;
    }
  }
}

/**
 *
 * @param arr sequence to look for summmands
 * @param target sum we are looking for
 * @returns pair of summands in array, if they exist
 */
function twoSum(arr: number[], target: number): number[] {
  // Create hashtable to store all values of original array
  let indices: number[] = [];
  const table: { [key: number]: number } = {};
  for (let i = 0; i < arr.length; i++) {
    table[arr[i]] = i;
  }
  // Check over same range to find complement to current value as a key in table
  for (let i = 0; i < arr.length; i++) {
    const complement = target - arr[i];
    // 2nd condition ensures summands are 2 unique members of sequence
    if (table.hasOwnProperty(complement) && table[complement] !== i) {
      indices = [i, table[complement]];
    }
  }
  return indices;
}
