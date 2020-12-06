/**
 * Two Sum Problem
 */

import { gimmeInput } from "../read";
import { quickSort } from "../sort";
import { binarySearch } from "../search";

const data = gimmeInput("1.txt");
const sum = 2020;
let firstNumber = -1;
if (data) {
  const arr = data.split("\n").map((e) => parseInt(e));
  const sortedArr = quickSort(arr);
  sortedArr.some((e) => {
    const siblingIndex = binarySearch(sortedArr, sum - e);
    if (siblingIndex >= 0) {
      firstNumber = e;
      return true;
    }
  });
  // Making an assumption we don't get negative numbers so I can set the default to -1
  if (firstNumber >= 0) {
    const product = firstNumber * (sum - firstNumber);
    console.log(`${firstNumber}x${sum - firstNumber}=${product}`);
  } else {
    console.log(`Could not find a sum of ${sum} in the input provided`);
  }
}
