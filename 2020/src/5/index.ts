/**
 * Finding seating via binary search instructions:
 * Find each row and column, calcuate ID based on row * 8 + column
 * Determine hihghest ID in input
 */

import { gimmeInput } from "../read";

const data = gimmeInput("5.txt");
if (data) {
  const arr = data.split("\n");
  // const arr = ["FBFBBFFRLR", "BFFFBBFRRR", "FFFBBBFRRR", "BBFFBBFRLL"];

  const rowMin = 0;
  const rowMax = 127;
  const colMin = 0;
  const colMax = 7;

  let highestID = 0;
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
    if (id > highestID) {
      highestID = id;
    }
      console.log(`${rowM} x ${colM} -> ID: ${id}`);
  });

  console.log(`Highest ID found: ${highestID}`);
}
