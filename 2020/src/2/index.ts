/**
 * Read passwords with policies and get total number of Valid pws
 * Example: 1-3 a => Means "one to three" instances of "a" to be valid
 */

import { gimmeInput } from "../read";

const data = gimmeInput("2.txt");
let totalValid = 0;
if (data) {
  const arr = data.split("\n");
  arr.forEach((e) => {
    const items = e.split(" ");
    const range = items[0].split("-");
    const letter = items[1].substring(0,1); //must remove colon
    const pw = items[2];
    const min = parseInt(range[0]);
    const max = parseInt(range[1]);
    const regex = new RegExp(letter, "g");
    const count = (pw.match(regex) || []).length;
    if (min <= count && count <= max) {
        totalValid++;
    } 
    else {
        console.log(`${count} ${letter}'s in ${pw}`);
        console.log(`This is outside the range: ${min} - ${max} \n`);
    }
  });
  console.log(`There are ${totalValid} valid passwords (Out of ${arr.length}).`);
}
