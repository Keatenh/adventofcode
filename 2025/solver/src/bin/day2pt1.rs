use solver::read_lines;









fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/2/example.txt")?;
    let lines = read_lines("../input/2/input.txt")?;
    
    let mut sum = 0;
    
    // 1. parse input as ranges of numbers
    for line in lines {
        let text = line?; // unwrap
        let range_points: Vec<i64> = text
            .split(',')
            .flat_map(|range| {
                range
                    .split('-')
                    .map(|s| s.parse::<i64>().unwrap())
            })
            .collect();
        //  println!("{:?}", range_points);

        // 2. iterate over each range (inclusively)
        for i in (0..range_points.len()).step_by(2) {
            // println!("{}, {}",range_points[i], range_points[i+1]);
            for num in range_points[i]..=range_points[i+1] {
                // println!("EVALUATING: {} ...", num);
                // 3. for each value in range, create string representation
                let str = num.to_string();
                // 4. ignore num strings with odd digits
                if str.len() % 2 != 0 {
                    continue;
                }
                // 5. ignore num strings that have leading zeroes (per problem statement)
                if str.chars().nth(0).unwrap() == '0' {
                    continue;
                }
                // 6. otherwise, split the string representation in half
                let (str1, str2) = str.split_at(str.len()/2);
                // println!("str1 is {}, str2 is {}", str1, str2);
                // 7. if the 2 sections of the num string are the same, add the original number itself to a sum
                if str1 == str2 {
                    // println!("invalid ID found: {}", num);
                    sum += num;
                }
            }
        }
    }
    // 8. return the sum
    println!("The day 1 sum is {}", sum);
    // Your puzzle answer was 31000881061.
    Ok(())
}