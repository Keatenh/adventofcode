/**
 * Same as before, with new rules:
 * - If there are any occupied seats in the 8 directions, they count as occupied for that node
 * - It now takes 5 occupied seats to make someone leave a node
 */

import { gimmeInput } from "../read";
import { deepCopy } from "../utils";

const data = gimmeInput("11.txt");
const openRegex = new RegExp("L", "g");
const occupiedRegex = new RegExp("#", "g");
if (data) {
  const rows: string[] = data.split("\n");
  let seats: string[][] = rows.map((row) => row.split(""));
  let newSeats: string[][] = [];

  let done = false; //Set to true to test below with corresponding file
  // console.log(anyOccupied(seats, 3, 3));//for test-2
  // console.log(anyOccupied(seats, 0, 3)); //for test-3

  let seatChanges = -1; //Curious to how long to stabilize for given input
  while (!done) {
    // Note we cannot use something like spread operator here, as the multidemtional array passes the
    // 2nd level of arrays by reference and we will mutate the originals
    newSeats = deepCopy(seats);
    for (let i = 0; i < seats.length; i++) {
      for (let j = 0; j < seats[i].length; j++) {
        const count = anyOccupied(seats, i, j);
        // 1. Rule to occupy
        if (seats[i] && seats[i][j].match(openRegex) && count === 0) {
          newSeats[i][j] = "#";
        }
        // 2. Rule to empty seat
        if (seats[i] && seats[i][j].match(occupiedRegex) && count >= 5) {
          newSeats[i][j] = "L";
        }
      }
    }
    // Debug output to display seating map at each step:
    // console.log(newSeats.map((s) => s.join("")).join("\n"));
    // console.log("\n");

    // Exit when we have not been able to change seats
    done = compare2DArray(seats, newSeats);
    // Set starting matrix to last computed values
    seats = deepCopy(newSeats);
    // Count seat change
    seatChanges++;

    // Debug output to display attempt count in real time:
    console.clear();
    console.log(`Executed ${seatChanges} seat changes...`);
  }

  const count = newSeats
    .map((s) => s.join(""))
    .join("")
    .match(occupiedRegex)?.length;
  console.log(
    `Total occupied seats @ equilibrium: ${count}; After ${seatChanges} seat changes.`
  );
}

/**
 * Looks for any unobscured open or occupied seats in 8 directions, from row, col
 * @param data
 * @param row
 * @param col
 * @returns The total number of occupied seats as seen from row, col
 */
function anyOccupied(data: string[][], row: number, col: number): number {
  const dirTally = [0, 0, 0, 0, 0, 0, 0, 0];
  const dirDelta = Array(dirTally.length).fill(1000);

  // Checks regex of char at coordinates
  const count = (i: number, j: number, regex: RegExp): number => {
    if (data[i]) {
      const p = data[i][j];
      if (p && p.match(regex)) {
        return 1;
      }
    }
    return 0;
  };

  // Loop over entire input
  for (let i = 0; i <= data.length; i++) {
    for (let j = 0; j <= data[0].length; j++) {
      // Check that the seat we are looking for lies on the same row or column,
      // or lies on a diagonal line from the current node (equidistant in each dimension)
      const deltaI = Math.abs(row - i);
      const deltaJ = Math.abs(col - j);
      const diagonal = deltaI === deltaJ;

      // Mark 0 for open or 1 for occupied an seat, if it is the closest one in a given direction, n
      const dist = Math.sqrt(deltaI * deltaI + deltaJ * deltaJ);
      const open = count(i, j, openRegex);
      const occupied = count(i, j, occupiedRegex);
      const tally = (n: number): void => {
        if (open && dist < dirDelta[n]) {
          dirTally[n] = 0;
          dirDelta[n] = dist;
        }
        if (occupied && dist < dirDelta[n]) {
          dirTally[n] = 1;
          dirDelta[n] = dist;
        }
      };
      // Provide direction based on coordinates relative to current row, col
      let n = -1; //Tally dimension
      if (i === row && j < col) {
        n = 0;
      } else if (i === row && j > col) {
        n = 1;
      } else if (j === col && i < row) {
        n = 2;
      } else if (j === col && i > row) {
        n = 3;
      } else if (i < row && j < col && diagonal) {
        n = 4;
      } else if (i < row && j > col && diagonal) {
        n = 5;
      } else if (i > row && j < col && diagonal) {
        n = 6;
      } else if (i > row && j > col && diagonal) {
        n = 7;
      }
      if (n !== -1) {
        tally(n);
      }
    }
  }
  // Return the total number of "blocked" directions
  return dirTally.reduce((a, c) => a + c);
}

/**
 * Compares 2D string arrays for exact likeness
 * @param arr1
 * @param arr2
 */
function compare2DArray(arr1: string[][], arr2: string[][]): boolean {
  const str = (arr: string[][]) => arr.map((s) => s.join("")).join("");
  if (str(arr1) === str(arr2)) {
    return true;
  }
  return false;
}
