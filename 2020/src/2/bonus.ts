/**
 * Read passwords with policies and get total number of Valid pws
 * Example: 1-3 a => Means index 0 OR 2 must be "a" to be valid (NOT both)
 */

import { gimmeInput } from "../read";

const data = gimmeInput("2.txt");
let totalValid = 0;
if (data) {
  const arr = data.split("\n");
  arr.forEach((e) => {
    const items = e.split(" ");
    const range = items[0].split("-");
    const letter = items[1].substring(0, 1); //must remove colon
    const pw = items[2];
    const pos1 = parseInt(range[0]);
    const pos2 = parseInt(range[1]);
    const index1 = pos1 - 1; //The positions are indexed at 1 instead of 0
    const index2 = pos2 - 1;
    if (
      (pw[index1] === letter && pw[index2] !== letter) ||
      (pw[index1] !== letter && pw[index2] === letter)
    ) {
      totalValid++;
    }
  });
  console.log(
    `There are ${totalValid} valid passwords (Out of ${arr.length}).`
  );
}
