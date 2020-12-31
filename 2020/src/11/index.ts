/**
 * Look at seating chart and determine how seats will fill simultaneously with the following rules:
 * - If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
 * - If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
 * - Otherwise, the seat's state does not change.
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
  let done = false;
  let seatChanges = -1; //Curious to how long to stabilize for given input
  while (!done) {
    // Note we cannot use something like spread operator here, as the multidemtional array passes the
    // 2nd level of arrays by reference and we will mutate the originals
    newSeats = deepCopy(seats);
    for (let i = 0; i < seats.length; i++) {
      for (let j = 0; j < seats[i].length; j++) {
        const count = adjacentOccupied(seats, i, j);
        // 1. Rule to occupy
        if (seats[i] && seats[i][j].match(openRegex) && count === 0) {
          newSeats[i][j] = "#";
        }
        // 2. Rule to empty seat
        if (seats[i] && seats[i][j].match(occupiedRegex) && count >= 4) {
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
  }

  const count = newSeats
    .map((s) => s.join(""))
    .join("")
    .match(occupiedRegex)?.length;
  console.log(`Total occupied seats @ equilibrium: ${count}; After ${seatChanges} seat changes.`);
}

function adjacentOccupied(data: string[][], row: number, col: number): number {
  let count = 0;
  for (let i = -1; i <= 1; i++) {
    for (let j = -1; j <= 1; j++) {
      if (!(i === 0 && j === 0)) {
        //skip iterating over current node
        if (data[row + i]) {
          const p = data[row + i][col + j];
          if (p && p.match(occupiedRegex)) {
            count++;
          }
        }
      }
    }
  }
  return count;
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
