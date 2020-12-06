/**
 * Read map from textfile and determine how many trees you encounter,
 * traversing a path at angle right 3 and down 1, from top left
 */

import { gimmeInput } from "../read";

const data = gimmeInput("3.txt");
let treeCount = 0;
if (data) {
  const arr = data.split("\n");
  /**
   * Note i will represent y-axis, j as x-axis
   */
  const treeRegex = new RegExp("#", "g");
  // Looks like we can assume our input will have consistent width:
  const width = arr[0].length;

  for (let i = 0; i < arr.length; i++) {
    let j = 3 * i;
    if (j >= width) { //Need to continue down the slope, reflecting on sides
      j = j % width;
    }
    console.log(j);
    if (arr[i][j].match(treeRegex)) {
      console.log(`YES: ${arr[i][j]} is a tree`);
      treeCount++;
    } else {
      console.log(`NO: ${arr[i][j]} is NOT a tree`);
    }
  }
  console.log(`Encountered ${treeCount} trees on the way down the slope`);
}
