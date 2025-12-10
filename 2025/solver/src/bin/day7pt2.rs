/**
 * I was avoiding it with understanding of the boundaries in part 1,
 * but I think part 2 could benefit from using recursion as we split.
 *
 * My idea is to continue calling the function that calculates
 * future rights and lefts until we run out of input
 *
 * Can we count the number of times we split, subtract the number of "dead ends" and multiply by 2
 * That was wrong...
 *
 * UPDATE:
 * After looking online, I found this visual representation of an algorithm:
 * https://www.reddit.com/r/adventofcode/comments/1pgnmou/2025_day_7_lets_visualize/#lightbox
 * It looks like we can find the total number of paths by:
 * Checking the number of times we visit each column AFTER a split,
 * pushing the carried visitation value and
 * subtracting from source col when we split, then sum all of the columns.
 * I'm not currently sure why this is, but it seems like the level of elegance that
 * I wanted to get out of the problem myself, so I'm gonna give it a try and
 * submit on day 8 so that I don't take points credit for it on the leaderboard.
 * My guess is the explanation is in the 'memory' of the previous values contained when we move
 * the visitation sum around.
 * 
 * UPDATE #2:
 * After talking through the problem with Ben, this ^ is right. Specifically, the
 * "visitation sum" can be conceptualized as the number of paths that led up to that
 * point/split.
 */
use solver::read_lines;

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/7/example.txt")?;
    let lines = read_lines("../input/7/input.txt")?;

    let lines_vec: Vec<String> = lines.map(|e| e.unwrap()).collect();
    let dup_lines = lines_vec.clone();

    let mut col_totals: Vec<i64> = vec![];

    // let regx = Regex::new(r"S|\^"); //Having issues importing regex - it might not be a native lib

    //Note: it seems like we are discarding all remnants of the line as we process the lines variable
    // -> Might need to iterate to push into a standard vector? and then iterate

    //We are assuming all odd lines are filled with '.' characters -> step by 2
    for (i, line) in lines_vec.into_iter().enumerate().step_by(2) {
        let chars = line.char_indices();
        // println!("chars: {:?}", chars);

        if i == 0 {
            for _c in chars {
                col_totals.push(0);
            }
            continue;
        }

        for (j, c) in chars {
            if c == '^' {
                //Handle the S term separately to reduce complexity in next section
                if i == 2 {
                    let mut center: char = '?';
                    if dup_lines.get(i - 2).unwrap().chars().nth(j).is_some() {
                        center = dup_lines.get(i - 2).unwrap().chars().nth(j).unwrap();
                    }
                    // println!("center: {}", center);
                    if center == 'S' {
                        col_totals[j - 1] += 1;
                        col_totals[j + 1] += 1;
                        break;
                    }
                }

                //Iterate, with x subtracting back to start
                //(or line 2 since the S should always be split)
                for x in 1..i / 2 {
                    let mut left: char = '?';
                    if dup_lines
                        .get(i - 2 * x)
                        .unwrap()
                        .chars()
                        .nth(j - 1)
                        .is_some()
                    {
                        left = dup_lines
                            .get(i - 2 * x)
                            .unwrap()
                            .chars()
                            .nth(j - 1)
                            .unwrap();
                    }
                    let mut right: char = '?';
                    if dup_lines
                        .get(i - 2 * x)
                        .unwrap()
                        .chars()
                        .nth(j + 1)
                        .is_some()
                    {
                        right = dup_lines
                            .get(i - 2 * x)
                            .unwrap()
                            .chars()
                            .nth(j + 1)
                            .unwrap();
                    }
                    let mut center: char = '?';
                    if dup_lines.get(i - 2 * x).unwrap().chars().nth(j).is_some() {
                        center = dup_lines.get(i - 2 * x).unwrap().chars().nth(j).unwrap();
                    }

                    // If we find a splitter directly above us, before we are able to find
                    // a splitter above and to the left or right, we are having he source beam blocked,
                    // and therefor are not able to split anything. Essentially in a beam shadow.
                    if center == '^' {
                        // dead_ends += 1;
                        // col_totals[j] += 1;
                        break;
                    }
                    // Otherwise, we are splitting a tachyon beam
                    let mut done = false;
                    let cur_val = col_totals[j];

                    if (j > 0 && left == '^') || (j < line.len() && right == '^') {
                        col_totals[j - 1] += cur_val;
                        col_totals[j + 1] += cur_val;
                        col_totals[j] = 0;
                        done = true;
                    }
                    if done {
                        break;
                    }
                }
            }
        }
        // println!("Splits after line {}: {:?}", i, col_totals);
        // println!("=======");
    }

    let timelines = col_totals.iter().fold(0, |acc: i64, x| acc + x);

    println!("Number of timelines: {}", timelines);
    // Your puzzle answer was 13883459503480.
    //3090 is too low [(1546-1)*2]
    //2848 is too low (1546-122)*2
    //3092 is too low (1546*2)
    //3214 is not right (1546*2+122)
    Ok(())
}
