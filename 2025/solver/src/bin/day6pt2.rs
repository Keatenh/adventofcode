use std::str::Chars;

use solver::read_last_line;
use solver::read_last_line_preserve_whitespace;
use solver::read_lines_preserve_whitespace;

/**
 * With the new problem statement, we can no longer strip the whitespace, because we have to read the numbers vertically in the file.
 * My instinct is to try to rearrange the text, and then process the numbers the same way,
 * but I might play around with the outputs I see printed in the existing loop and try to get the number parsing to match expected.
 *
 * Since all the operations are still commutative, we can start wherever we like if we parse the numbers correctly.
 */

//Using the operators to tell us when to move to next problem -> meaningful whitespace
fn rotate_and_group_vector(input: Vec<String>, ops: Chars) -> Vec<Vec<String>> {
    let mut groups: Vec<Vec<String>> = vec![vec![]];
    let o: Vec<char> = ops.collect();
    for (i, txt) in input.into_iter().enumerate() {
        let row: Chars<'_> = txt.chars();
        //Reset group index on each new line of the original vector
        let mut group_index = 0;

        //Gonna set a new character index that will work with our groups
        let mut k = 0;
        for (j, c) in row.into_iter().enumerate() {

            //ignore the 1st operator, since we pushed an initial empty vector
            if j > 0 && j < o.len() && (o[j] == '+' || o[j] == '*') {
                if i == 0 {
                    //remove meaningful space from last group (problem separator)
                    groups[group_index].pop();
                    groups.push(vec![]);
                }
                group_index += 1;
                k = 0; //reset
            }
           
            if i == 0 {
                groups[group_index].push(format!("{}", c));
            } else if c != ' ' {
                groups[group_index][k] = format!("{}{}", groups[group_index][k], c);
            }

            //update our proxy index
            k += 1;
        }
    }
    return groups;
}

fn main() -> std::io::Result<()> {
    // let file = "../input/6/example.txt";
    let file = "../input/6/input.txt";

    let mut answer: i64 = 0;

    //Once again bested by file io :'(
    let operations_text: String = read_last_line(file)?.unwrap();
    let ops: Vec<&str> = operations_text.trim().split_ascii_whitespace().collect();

    let operations_text_raw: String = read_last_line_preserve_whitespace(file)?.unwrap();
    let ops_raw: Chars<'_> = operations_text_raw.chars();

    // I'm anticipating rotating / transforming the 2D representation of the text characters like a matrix
    let mut lines: Vec<String> = read_lines_preserve_whitespace(file)?;
    lines.pop();

    let rotated_input: Vec<Vec<String>> = rotate_and_group_vector(lines, ops_raw);
    // println!("{:?}", rotated_input);

    //Parse the new grouped text as numbers and take sum or product
    for n in 0..ops.len() {
        let problem_answer: i64;
        if ops[n] == "+" {
            problem_answer = rotated_input[n].iter().fold(0, |acc: i64, x| acc + x.trim().parse::<i64>().unwrap());
        } else { // == "*"
            problem_answer = rotated_input[n].iter().fold(1, |acc: i64, x| acc * x.trim().parse::<i64>().unwrap());
        }
        // println!("answer #{}: {}",n,problem_answer);
        answer += problem_answer;
    }

    println!(
        "Cephalopod's math homework answer (correct parsing): {}",
        answer
    );
    // Your puzzle answer was 11052310600986.
    Ok(())
}
