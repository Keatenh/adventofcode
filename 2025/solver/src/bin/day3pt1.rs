use solver::read_lines;


fn find_char_indices(s: &str, target: char) -> Vec<usize> {
    return s.char_indices()
    .filter(|(_,c)| *c == target)
    .map(|(i, _)| i)
    .collect();
}

fn main() -> std::io::Result<()> {
    //1. read input lines as battery banks
    // let lines = read_lines("../input/3/example.txt")?;
     let lines = read_lines("../input/3/input.txt")?;
    
    let mut sum: i64 = 0;

    //2. for each bank, check if all but the last character is "0"
    for line in lines {
        let bank = line?; // unwrap
        // println!("bank: {}",bank);
        let working_bank = &bank[..bank.len()-1];
        // println!("working bank: {}",working_bank);

        //2a. if so, add the last character as a digit to the result sum
        // let mut chars: std::str::Chars<'_> = working_bank.chars(); <- Did NOT use this, bc this iterator changes as we move across it with .all()
        if working_bank.chars().all(|c| c == '0') {
            sum += &bank[bank.len()-1..].parse().unwrap();
            continue;
        }

        //3. If not, iterate over (all but the last) the bank characters to find the highest number -> digit1
        let mut digit1: i64 = 0; //This should never be zero due to above logic, so could help debug
        for num_str in working_bank.chars() {
            let num = num_str.to_string().parse().unwrap();
            if num > digit1 {
                digit1 = num;
            }
        }

        //4. iterate over (all but the last) the bank characters to find all indices matching this highest number
        let locations: Vec<usize> = find_char_indices(working_bank, digit1.to_string().chars().next().unwrap());
        // println!("Locations of highest digit1: {}", digit1);
        // println!("{:?}",locations);

        //5. For each index, i, check the values of i+1-len-1 and find the highest one -> num2
        let bank_vector: Vec<char> = bank.chars().collect();
        let mut digit2: i64 = 0;
        for index in locations {
            for n in index..bank.len()-1 {
                let num = bank_vector[n+1].to_string().parse().unwrap();
                if num > digit2 {
                    digit2 = num
                }
            }
        }
        //6. concatenate num1+num2, convert to integer, and add to the result sum
        let digits: String = format!("{}{}", digit1, digit2);
        println!("The highest 2-digit number is: {}", digits);
        sum += digits.parse::<i64>().unwrap();

    }
    
    //7. return result sum
    println!("The day 3 part 1 sum is {}", sum);
    // Your puzzle answer was 16927.
    Ok(())
}