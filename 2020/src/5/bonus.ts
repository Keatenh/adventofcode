/**
 * Finding seating via binary search instructions:
 * Find each row and column, calcuate ID based on row * 8 + column
 * Find missing ID in list (Ignoring any missing from ends)
 */

import { gimmeInput } from "../read";
import { quickSort } from "../sort";

const data = gimmeInput("5.txt");
if (data) {
  const arr = data.split("\n");

  const rowMin = 0;
  const rowMax = 127;
  const colMin = 0;
  const colMax = 7;

  const ids: number[] = [];
  arr.forEach((seat) => {
    let rowS = rowMin;
    let rowE = rowMax;
    let rowM = Math.floor((rowS + rowE) / 2);

    let colS = colMin;
    let colE = colMax;
    let colM = Math.floor((colS + colE) / 2);
    for (let i = 0; i < 10; i++) {
      if (i < 7) {
        if (seat[i] === "F") {
          // rowS stays the same
          rowE = rowM;
        } else {
          // B or "upper half"
          rowS = rowM + 1;
          // rowE stays the same
        }
        rowM = Math.floor((rowS + rowE) / 2);
      } else {
        if (seat[i] === "L") {
          colE = colM;
        } else {
          // R or "upper half"
          colS = colM + 1;
        }
        colM = Math.floor((colS + colE) / 2);
      }
    }
    const id = rowM * 8 + colM;
    // console.log(`${rowM} x ${colM} -> ID: ${id}`);
    ids.push(id);
  });
  const sortedIds = quickSort(ids);
  const possibleIDs = findMissingNumber(sortedIds);
  // console.log(possibleIDs);
  console.log(`My seat is ID: ${possibleIDs[0]}`);
}

function findMissingNumber(arr: number[]): number[] {
  const min = Math.min(...arr);
  const max = Math.max(...arr);
  // Create array with all numbers:
  const fullArr = Array.from(Array(max - min), (v, i) => {
    return i + min;
  });
  // compare the full array with the provided array that is missing entries/numbers
  return fullArr.filter((i) => {
    return !arr.includes(i);
  });
};
