use solver::read_lines;

//Create function that returns Vector of substrings of length == n, from input string s
fn substrings_of_len(s: String, n: usize) -> Vec<String> {
    let mut v = Vec::new();
    for i in (0..s.len()).step_by(n) {
        let slice = s.get(i..i + n).unwrap();
        v.push(slice.to_string());
    }
    // println!("checkout vector: {:?}", v);
    return v;
}

// Define a function named 'get_first_char' that borrows a string slice and returns its first character
fn get_first_char(s: &str) -> Option<char> {
    s.chars().next() // Return an Option containing the first character of the string slice, or None if the string is empty
}

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/2/example.txt")?;
    let lines = read_lines("../input/2/input.txt")?;

    let mut sum = 0;

    // 1. parse input as ranges of numbers
    for line in lines {
        let text = line?; // unwrap
        let range_points: Vec<i64> = text
            .split(',')
            .flat_map(|range| range.split('-').map(|s| s.parse::<i64>().unwrap()))
            .collect();
        //  println!("{:?}", range_points);

        // 2. iterate over each range (inclusively)
        for i in (0..range_points.len()).step_by(2) {
            // println!("{}, {}",range_points[i], range_points[i+1]);
            for num in range_points[i]..=range_points[i + 1] {
                // println!("EVALUATING: {} ...", num);
                // 3. for each value in range, create string representation
                let str = num.to_string();

                // 4. split the string representation in half, thirds, forths... until the number of divisions == the length of digits in num
                for i in 2..=str.len() {
                    let factor: f64 = str.len() as f64 / i as f64;
                    //only use true factors for divisions -> cast to int
                    if factor.fract() != 0.0 {
                        continue;
                    }
                    // day2pt2.rs(49, 64): consider cloning the value if the performance cost is acceptable: `.clone()`
                    let clone_string = str.clone();
                    let strs: Vec<String> = substrings_of_len(clone_string, factor as usize);
                    //5. If ALL substrings are matching, we want to add the original number to a set of invalid IDs...
                    //OR, simply add the number, and stop processing it, so that we do not count it multiple times.

                    let val = strs.get(0).unwrap();
                    // println!("initial value: {}", val);
                    let c = get_first_char(val).unwrap();
                    if c != '0' && strs.iter().all(|s | s == val) {
                        // println!("FOUND MATCHING VALUES: {}",val);
                        sum += num;
                        break;
                    }
                }
            }
        }
    }
    // 8. return the sum
    println!("The day 1 sum is {}", sum);
    // Your puzzle answer was 46769308485.
    Ok(())
}
