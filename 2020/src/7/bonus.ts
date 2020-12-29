/**
 * Luggage Rules
 * Find the total number of bags that the target contains.
 * Target color: "shiny gold"
 */

import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("7.txt");
// Create array of data objects with only the info we need about color and what the bag contains
if (data) {
  const arr = data.split("\n");
  const objs: BagRule[] = arr.map((rule) => {
    const parts: string[] = rule.split("bags contain").map((x) => x.trim());
    const type = parts[0];
    const contents: Item[] = parts[1]
      .split(",")
      .filter((x) => x.match(/\d+/))
      .map(
        (x: string): Item => {
          return {
            name: x
              .replace(/[0-9]/, "")
              .replace(/bag.*$/, "")
              .trim(),
            qty: parseInt(x.replace(/\D/g, "") || "0"),
          };
        }
      );
    return {
      type,
      contents,
    };
  });
  // console.log(JSON.stringify(objs, null, 4));
  // Iterate through the references to determine how many different primary bags the target contains
  const answer = countContents(objs, ["shiny gold"], 0);
  console.log(answer);
}

interface BagRule {
  type: string;
  contents: Item[];
}

interface Item {
  name: string;
  qty: number;
}

function countContents(
  rules: BagRule[],
  targets: string[],
  counter: number
): number {
  // Access the bag(s) we want to check the contents of:
  const lastCount = counter; //set for later comparison
  let nextTargets: string[] = [];
  // Find every target we are interested in:
  targets.forEach((target) => {
    const rule = rules.find((rule) => rule.type === target);
    // If there are contents, add them to the tally
    if (rule && rule.contents) {
      counter += rule.contents.reduce((acc, cur) => acc + cur.qty, 0);
      // When adding the bag types to check on next recurstion, add additional array entries 
      // for every count in the qty for the current rule
      rule.contents.forEach((x) => {
        nextTargets = nextTargets.concat(Array(x.qty).fill(x.name));
      });
    }
  });
  if (counter === lastCount) { //If we are no longer adding bags, we are done
    return counter;
  } else {
    return countContents(rules, nextTargets, counter);
  }
}
