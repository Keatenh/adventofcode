/**
 * Boot instructions that lead to infinite loop
 * Find the value of the accumulator at the time it
 * starts repeating instrucutions.
 * acc = Increase or decrease accumulator
 * jmp = Jump to line instruction relative to self
 * nop = No Operation
 */

//Get input and store data in array with metadata that
// indicates if the node has been visited.

import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("8.txt");
// Create array of data objects with only the info we need about color and what the bag contains
if (data) {
  const instructions: Instruction[] = data.split("\n").map((x) => {
    const line = x.split(" ");
    return {
      cmd: line[0],
      value: parseInt(line[1]),
      visited: false,
    };
  });
//   console.log(JSON.stringify(instructions));
  let accumulator = 0;
  let i = 0;
  while (i < instructions.length) {
    if (instructions[i].visited) {
      break;
    }
    if (instructions[i].cmd === "acc") {
      accumulator += instructions[i].value;
    } else if (instructions[i].cmd === "jmp") {
      i = i + instructions[i].value - 1; //compensating for the i++
    }
    instructions[i].visited = true;
    i++;
  }
  console.log(`Before entering loop, last Accumulator value is: ${accumulator}`);
}
interface Instruction {
  cmd: string;
  value: number;
  visited: boolean;
}
