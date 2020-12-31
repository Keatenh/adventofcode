// const arr = [16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4];
import { gimmeInput } from "../read";
import { memoize } from "../memo";

// Read file
const data = gimmeInput("10.txt");
if (data) {
  const arr = data.split("\n").map((x) => parseInt(x));
  arr.unshift(0);
  arr.push(Math.max(...arr) + 3);
  const seq = [...new Uint16Array(arr).sort()];

  let count = 0;
  const countJolts = (arr: number[], start: number): number => {
    for (let i = start; i <= arr.length; i++) {
      if (i === arr.length) {
        return count;
      } else {
        if (arr[i + 2] - arr[i] <= 3) {
          //   console.log(arr[i + 1]);
          count++;
          if (count % 1000000 === 0) {
            console.clear();
            console.log(`count: ${count / 1000000} million...`);
          }

          countJolts([...arr.slice(0, i + 1), ...arr.slice(i + 2)], i);
        }
      }
    }
    return -1;
  };

  const c = memoize(countJolts);
  const answer = c(seq, 0) + 1; //Always have the initial case of NO removals
  console.log(
    `Found a total of ${answer} adapter combinations.`
  );
}
/**
 * Time for method:
 * 10-test.txt
 * ===============
 * real	0m1.795s
 * user	0m2.581s
 * sys	0m0.200s
 *
 * 10.text
 * ===============
 * After getting answer from DP solution, 
 * It appears this method would take > 20 days to complete
 * (vs 1.3 seconds from other method)
 */
