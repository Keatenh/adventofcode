use std::collections::VecDeque;

use solver::read_lines;

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/1/example.txt")?;
    let lines = read_lines("../input/1/input.txt")?;

    //Keep track of dial position
    let mut position: i32 = 50;
    let mut zero_counter: i32 = 0;
    // let mut movement = 0;

    for line in lines {
        // Debug variable
        // movement += 1;
        // println!("Move # {}", movement);

        let text = line?; // unwrap
        // println!("{}", text);

        //Using a type of Vector where I can unshift I guess lol
        let mut chars: VecDeque<char> = text.chars().collect();
        let lr: char = chars.pop_front().unwrap();
        let num_str: String = chars.into_iter().collect();
        let num: i32 = num_str.parse().unwrap();
        let mut sign = 1;
        if lr == 'L' {
            sign = -1;
        }

        //Check crossing 0
        for _n in 0..num.abs() {
            position += sign;
            if position == 0 {
                zero_counter += 1;
                // println!("Crossed ZERO!");
            } else if position == 100 {
                zero_counter += 1;
                // println!("Crossed ZERO @ 100!");
                position = 0;
            } else if position < 0 {
                position += 100;
            } else if position > 99 {
                position -= 100;
            }
        }
       
        // println!("{}, {}, {}", num*sign, position, zero_counter);
    }
    //6977 is too high
    //6360 is too high
    //5739 is too low
    //Your puzzle answer was 5933
    println!("The day 1 pt 2 password is {}", zero_counter);


    Ok(())
}