import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("6.txt");

let sum = 0;
if (data) {
  const arr = data.split("\n\n").map((str) => str.split("\n"));
//   console.log(JSON.stringify(arr));
  arr.forEach((group) => {
    const uniqueAns = new Set();
    group.forEach((answer) => {
      for (let i = 0; i < answer.length; i++) {
        uniqueAns.add(answer[i]);
      }
    });
    // console.log(`${uniqueAns}, ${uniqueAns.size}`);
    sum += uniqueAns.size;
  });
  console.log(`Total unique answers from all groups: ${sum}`);
}
