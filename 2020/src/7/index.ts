/**
 * Luggage Rules
 * Find the number of bags that can ultimately contain target.
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
    return {
      type: parts[0],
      contents: parts[1]
        .split(",")
        .filter((x) => x.match(/\d+/))
        .map((x) =>
          x
            .replace(/[0-9]/, "")
            .replace(/bag.*$/, "")
            .trim()
        ),
    };
  });
  // console.log(JSON.stringify(objs, null, 4));
  // Iterate through the references to determine how many different primary bags (types) ultimately contain the target
  const bagCount = countBags(objs, ["shiny gold"], []);
  console.log(bagCount); //326 for reference in case of refactor...
}

interface BagRule {
  type: string;
  contents: string[];
}

function countBags(
  rules: BagRule[],
  targets: string[],
  aggregateList: string[]
): number {
  const filtered = rules
    .filter((x) => {
      return x.contents.some((a) => targets.includes(a));
    })
    .map((obj) => obj.type);
  if (filtered.length > 0) {
    // counter += filtered.length;
    aggregateList = aggregateList.concat(filtered);
    return countBags(rules, filtered, aggregateList);
  } else {
    //Dedupe when we are done finding all of the valid paths to the target bag(s)
    return new Set(aggregateList).size;
  }
  // counter += rules.reduce(
  //   (acc, cur) => acc + (cur.contents.includes(target) ? 1 : 0),
  //   0
  // );
}
