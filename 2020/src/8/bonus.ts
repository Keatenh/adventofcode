/**
 * Boot instructions that lead to infinite loop
 * Change one cmd (jmp or nop) such that program
 * terminates by proceeding past the last line instruction
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

  const instructionSet: Instruction[][] = [];
  instructionSet.push(instructions);
  /**
   * Declare every potential set of instructions and try them 1 by 1
   * until one set does not terminate early
   */
  const indices = instructions
    .map((ins, i) => (ins.cmd === "jmp" || ins.cmd === "nop" ? i : -1))
    .filter((index) => index !== -1);
  indices.forEach((targetIndex) => {
    instructionSet.push(
      instructions.map((ins, i) =>
        i === targetIndex ? { ...ins, cmd: cmdSwap(ins.cmd) } : ins
      )
    );
  });
  // console.log(JSON.stringify(instructionSet, null, 4));
  for (let j = 0; j < instructionSet.length; j++) {
    instructionSet[j].forEach((ins) => (ins.visited = false));
    // console.log(instructionSet[j]);
    let accumulator = 0;
    let i = 0;
    let complete = false;
    while (i <= instructionSet[j].length) {
      if (i === instructionSet[j].length) {
        complete = true;
        console.log(`Instruction set ${j} completed all commands (line ${i} is last line)`);
        break;
      }
      if (instructionSet[j][i].visited) {
        // console.log(`Instruction set ${j} repeated at line ${i}`); //debug
        break;
      }
      if (instructionSet[j][i].cmd === "acc") {
        accumulator += instructionSet[j][i].value;
      } else if (instructionSet[j][i].cmd === "jmp") {
        i = i + instructionSet[j][i].value - 1; //compensating for the i++
      }
      if (i < 0) {
        //If we would go outside of file, set to zero
        i = 0;
      }
      instructionSet[j][i].visited = true;
      i++;
    }
    if (complete) {
      console.log(`Last Accumulator value is: ${accumulator}`);
      break;
    }
  }
}

interface Instruction {
  cmd: string;
  value: number;
  visited: boolean;
}

function cmdSwap(input: string): string {
  if (input === "jmp") {
    return "nop";
  }
  return "jmp";
}
