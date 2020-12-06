/*
 * 3SUM Problem
 */

import { gimmeInput } from "../read";
import { quickSort } from "../sort";
import { binarySearch } from "../search";

const data = gimmeInput("1.txt");
const sum = 2020;
let firstNumber = -1;
if (data) {
  const unsortedarr = data.split("\n").map((e) => parseInt(e));
  const arr = quickSort(unsortedarr);

  for (let i = 0; i < arr.length - 2; i++) {
    const a = arr[i];
    let start = i + 1;
    let end = arr.length - 1;
    while (start < end) {
      const b = arr[start];
      const c = arr[end];
      if (a + b + c === sum) {
        console.log(`${a} + ${b} + ${c} = ${a + b + c}`);
        console.log(`${a} x ${b} x ${c} = ${a * b * c}`);
        // Ignoring next comments, we are going to assume 1 correct answer in the data set.
        // Break this while and the outside for loop:
        i = arr.length;
        break;
        // Continue search for all triplet combinations summing to zero.
        // We need to update both end and start together since the array values are distinct.
        // start = start + 1;
        // end = end - 1;
      } else if (a + b + c > sum) {
        end = end - 1;
      } else {
          start = start + 1;
      }
    }
  }
}
