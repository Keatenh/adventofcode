import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("6.txt");

let sum = 0;
if (data) {
  const arr = data.split("\n\n").map((str) => str.split("\n"));
  arr.forEach((group) => {
    let commonAns: Set<string> = new Set();
    for (let j = 0; j < group.length; j++) {
      const answer = group[j];
      const localSet = new Set(answer.split(""));
      if (j === 0) {
        commonAns = localSet;
      } else {
        commonAns = new Set([...commonAns].filter((x) => localSet.has(x)));
      }
    }
    // console.log(commonAns.size);
    sum += commonAns.size;
  });
  console.log(`Total common answers from all groups: ${sum}`);
}
