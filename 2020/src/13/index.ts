/**
 * Multiply the earliest bus ID by the number of minutes you'd need to wait
 */

// const arr = [7, 13, 59, 31, 19];
// const target = 939;
import { gimmeInput } from "../read";
const data = gimmeInput("13.txt");
if (data) {
  const instructions: string[] = data.split("\n");
  const target = parseInt(instructions.shift() || "-1");
  const arr: number[] = instructions[0]
    .split(",")
    .filter((s) => s !== "x")
    .map((e) => parseInt(e));
  let id = -1,
    d = target; //make this a high number to start
  // Find the id with the closest cycle to the target
  arr.forEach((n) => {
    const delta = Math.ceil(target / n) * n - target;
    if (delta < d) {
      id = n;
      d = delta;
    }
  });
  const ans = id * d;
  console.log(`id: ${id} answer: ${ans}`);
}
