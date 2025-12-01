use std::collections::VecDeque;

use solver::read_lines;

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/1/example.txt")?;
    let lines = read_lines("../input/1/input.txt")?;

    //Keep track of dial position
    let mut position: i32 = 50;
    let mut zero_counter: i32 = 0;

    for line in lines {
        let text = line?; // unwrap
        // println!("{}", text);

        //Using a type of Vector where I can unshift I guess lol
        let mut chars: VecDeque<char> = text.chars().collect();
        let lr: char = chars.pop_front().unwrap();
        let num_str: String = chars.into_iter().collect();
        let mut num: i32 = num_str.parse().unwrap();
        if lr == 'L' {
            num *= -1;
        }

        // let cycles = (num / 100).abs()+1;
        let num = num % 100;

        position += num;

        if position < 0 {
            position += 100;
            // println!("pos < 0");
        }
        if position > 99 {
            position -= 100;
            // println!("pos > 99");
        }

        if position == 0 {
            // println!("pos == 0");
            zero_counter += 1;
        }
        // println!("{}, {}, {}", num, position, zero_counter);
    }
    //Your puzzle answer was 1021
    println!("The day 1 pt 1 password is {}", zero_counter);


    Ok(())
}
