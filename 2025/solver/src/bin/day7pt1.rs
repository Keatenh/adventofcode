// use regex::Regex;

use solver::read_lines;

/**
 * Based on the wording and how tachyons break some expectations of physics,
 * I'm anticipating either the direction of the beam travel changing
 * or empty space (.) being occupied with some obstructions in part 2
 */

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/7/example.txt")?;
    let lines = read_lines("../input/7/input.txt")?;

    let lines_vec: Vec<String> = lines.map(|e| e.unwrap()).collect();
    let dup_lines = lines_vec.clone();

    let mut splits: i64 = 0;

    // let regx = Regex::new(r"S|\^"); //Having issues importing regex - it might not be a native lib

    //Note: it seems like we are discarding all remnants of the line as we process the lines variable
    // -> Might need to iterate to push into a standard vector? and then iterate

    //We are assuming all odd lines are filled with '.' characters -> step by 2
    for (i, line) in lines_vec.into_iter().enumerate().step_by(2) {
        let chars = line.char_indices();
        // println!("chars: {:?}", chars);

        if i == 0 {
            continue;
        }

        for (j, c) in chars {
            //We are keeping track of previous splitter line for comparisons, since the beam only travels downwards
            //Storing the dup_lines characters as separate variables mutates them when we call nth(),
            //so I did these ugly operations all in one line to find the target characters
            if c == '^' {


                //Handle the S term separately to reduce complexity in next section
                if i == 2 {
                    let mut center: char = '?';
                    if dup_lines.get(i - 2).unwrap().chars().nth(j).is_some() {
                        center = dup_lines.get(i - 2).unwrap().chars().nth(j).unwrap();
                    }
                    println!("center: {}", center);
                    if center == 'S' {
                        splits += 1;
                        break;
                    }
                }


                //Iterate, with x subtracting back to start 
                //(or line 2 since the S should always be split)
                for x in 1..i/2 { 
                    let mut left: char = '?';
                    if dup_lines.get(i - 2*x).unwrap().chars().nth(j - 1).is_some() {
                        left = dup_lines.get(i - 2*x).unwrap().chars().nth(j - 1).unwrap();
                    }
                    let mut right: char = '?';
                    if dup_lines.get(i - 2*x).unwrap().chars().nth(j + 1).is_some() {
                        right = dup_lines.get(i - 2*x).unwrap().chars().nth(j + 1).unwrap();
                    }
                    let mut center: char = '?';
                    if dup_lines.get(i - 2*x).unwrap().chars().nth(j).is_some() {
                        center = dup_lines.get(i - 2*x).unwrap().chars().nth(j).unwrap();
                    }

                    // If we find a splitter directly above us, before we are able to find 
                    // a splitter above and to the left or right, we are having he source beam blocked,
                    // and therefor are not able to split anything. Essentially in a beam shadow.
                    if center == '^' {
                        break;
                    }
                    // Otherwise, we are splitting a tachyon beam
                    if (j > 0 && left == '^') || (j < line.len() && right == '^') {
                        splits += 1;
                        break;
                    }
                }
            }
        }
        // println!("Splits after line {}: {}", i, splits);
    }

    println!("Number of tachyon beam splits: {}", splits);
    //1488 is too low -> Missing the cases where the beam continues multiple free lines (i)
    //1667 is too high -> Did not account for splitters blocking one another after adding above ^
    // Your puzzle answer was 1546.
    Ok(())
}
