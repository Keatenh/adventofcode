use solver::read_last_line;
/**
 * Given the fact that we only have to interpret addition and multiplication, my guess is part 2 will include subtraction and division.
 * The difference between the 2 is that the order of the elements in our columns then matters, so I will try to be mindful of that in my solution.
 *
 * We get the operators on the very last line, so we can read all the lines in and
 * A. push the numbers into individual column vectors / arrays (maybe within a root vector / array)
 *     & then run chosen the operation on each one, based on the matching character in the last index
 *     & then add the result to the final sum
 * or
 * B. run all the possible operations simultaneously, storing the results in a 2D array / tuple
 *    & then choose the element of the array / tuple, based on the matching character in the last index
 *     & then add the result to the final sum
 *
 * (Reading the file in reverse would work for just + and *, since they are commutative operations)
 * 
 * UPDATE: I ended up 
 * C. traversing the file 2x, once to get the operators, and then again to directly update the sums/products based on matching index
 *
 */
use solver::read_lines;

fn main() -> std::io::Result<()> {
    // let file = "../input/6/example.txt";
    let file = "../input/6/input.txt";

    let lines = read_lines(file)?;
    //Read the last line of file
    //Since I am learning rust, I was so out of the loop on how file io works that the function I'm importing here is NOT anything I wrote
    let operations_text: String = read_last_line(file)?.unwrap();
    let ops: Vec<&str> = operations_text.trim().split_ascii_whitespace().collect();

    //Tried to create an array, but this is not allowed at runtime
    let mut answers: Vec<i64> = vec![];

    for (i, line) in lines.into_iter().enumerate() {
        let text = line?;
        //Don't process line of operators
        //I don't think the inside of the loop can know about the size of the thing it is iterating on.
        //So I just check for the expected characters
        if text.starts_with("+") || text.starts_with("*") {
            break;
        }
        let row: Vec<&str> = text.trim().split_ascii_whitespace().collect();
        for (j,column) in row.into_iter().enumerate() {
            let num: i64 = column.parse().unwrap();
            // println!("num: {}", num);
            if i == 0 {
                answers.push(num);
            } else if ops[j] == "+" {
                answers[j] += num
            } else {
                //ops[j] == "*"
                answers[j] *= num
            }
            // println!("answers: {:?}", answers);
        }
    }
    // println!("final answers: {:?}", answers);

    let answer = answers.iter().fold(0, |acc: i64, x| acc + x);

    println!("Cephalopod's math homework answer: {}", answer);
    // Your puzzle answer was 6605396225322.
    Ok(())
}
