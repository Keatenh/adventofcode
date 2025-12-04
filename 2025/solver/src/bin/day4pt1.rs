use std::fmt::format;

/**
 * Solution A:
 * 1. Create 2-D vector, matching on character '@' read from lines to enter 1, otherwise 0
 * 2. iterate over indices, i, j, and for each combo, check combinations:
 *   i-1, j-1
 *   i-1, j
 *   i-1, j+1
 *   i, j-1
 *   i, j+1
 *   i+1, j-1
 *   i+1, j
 *   i+1, j+1
 * adding +1 to curr_sum for every 1 value found
 * 3. if curr_sum <4 ("fewer than 4"), there is 5 or more (out of 8) spaces available 
 *              -> add +1 to free_rolls counter
 * 4. return free_rolls
 *
 * Solution B:
 * Alternatively, we could try to record the roll data as sets of numbers x,y and store those in a vector
 * Then search the vector for the coordinates we specified
 * I think I like this ^ approach for a simpler type to deal with in rust, and string comparisons
 * It might also require less boundary checking than A.) for the sides/corners 
 */
use solver::read_lines;

fn main() -> std::io::Result<()> {
    //1. read input lines as battery banks
    // let lines = read_lines("../input/4/example.txt")?;
    let lines = read_lines("../input/4/input.txt")?;

    let mut accessible_rolls: i64 = 0;

    //Construct vector of location strings
    let mut locs = Vec::<String>::new();
    for (i, line) in lines.into_iter().enumerate() {
        let text = line?;
        for (j, c) in text.chars().enumerate() {
            if c == '@' {
                locs.push(format!("{},{}",i,j));
            }
        }
    }
    // println!("locations: {:?}",locs);
    for loc in &locs {
        //We are ultimately looking for how much free space we have, which we can get by counting blocked spaces
        let mut blocked_count: i64 = 0;
        //parse actual coords so we can add/subtract from them
        let coords: Vec<i64> = loc.split(',').map(|s| s.parse::<i64>().unwrap()).collect();
        //check surroundings
        for i in coords[0]-1..=coords[0]+1 {
            for j in coords[1]-1..=coords[1]+1 {
                //ignore trivial case of finding one's self
                if i == coords[0] && j == coords[1] {
                    continue;
                }
                let target = format!("{},{}", i, j);
                if locs.contains(&target) {
                    // println!("Found blockage for {} at: {}",loc, target);
                    blocked_count +=1;
                }
            }
        }
        if blocked_count < 4 {
            accessible_rolls +=1;
        }
    }

    println!("Number of rolls accessible by forklift {}", accessible_rolls);
    // Your puzzle answer was 1356.
    Ok(())
}