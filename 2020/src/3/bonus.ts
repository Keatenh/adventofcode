/**
 * Traverse additional slopes and take the product of all the tree counts
 */

import { gimmeInput } from "../read";

const data = gimmeInput("3.txt");
if (data) {
  const arr = data.split("\n");
  /**
   * Note i will represent y-axis, j as x-axis
   */
  const treeRegex = new RegExp("#", "g");
  // Looks like we can assume our input will have consistent width:
  const width = arr[0].length;

  const slopes = [
    [1, 1],
    [3, 1],
    [5, 1],
    [7, 1],
    [1, 2],
  ];

  let result = 1;
  slopes.forEach((slope) => {
    let treeCount = 0;
    const iDelta = slope[1];
    // Get the relative slope compared to i
    const jFactor = slope[0] / iDelta;
    for (let i = 0; i < arr.length; i += iDelta) {
      let j = jFactor * i;
      if (j >= width) {
        //Need to continue down the slope, reflecting on sides
        j = j % width;
      }
      if (arr[i][j].match(treeRegex)) {
    //     console.log(`YES: ${arr[i][j]} is a tree`);
        treeCount++;
      } else {
    //     console.log(`NO: ${arr[i][j]} is NOT a tree`);
      }
    }
    console.log(`Encountered ${treeCount} trees on the way down the slope`);
    result *= treeCount;
  });
  console.log(`Product of trees from each slope run: ${result}`);
}
