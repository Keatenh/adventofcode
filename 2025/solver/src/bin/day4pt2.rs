/**
 * For part 2, we might want to iterate on top the loop we created in part 1.
 * We could remove the coordinates from the vector as we find each one that fits the criteria,
 *      adding the count to a new sum each time
 * The stopping condition could be when the length of the vector no longer changes between
 *      iterations of the original loop.
 * 
 * Notes: 
 * - Figuring out the &mut notation and that it was needed was challenging and I needed help figuring out how to make the compiler happy
 * - My solution is not very cheap, it took a long time to run so there should be a more optimized algorithm
 */

use std::collections::HashSet;
use solver::read_lines;

 fn remove_vec_from_vec(o: &mut Vec<String>,r: Vec<String>) {
     // Convert the 'to_remove_vec' into a HashSet for efficient lookup
     let rset: HashSet<_> = r.into_iter().collect();
     // Use retain to keep only elements not present in 'rset'
     o.retain(|element| !rset.contains(element));
 }

 fn main() -> std::io::Result<()> {
     //1. read input lines as battery banks
    //  let lines = read_lines("../input/4/example.txt")?;
     let lines = read_lines("../input/4/input.txt")?;
 
     let mut removed_rolls: i64 = 0; 
    
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

     loop {
        let mut accessible_rolls = 0; //reset for this iteration
        let mut locs_to_remove: Vec<String> = Vec::new();
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
                locs_to_remove.push(loc.clone());
            }
        }
        // println!("Number of rolls accessible by forklift {}", accessible_rolls);
        removed_rolls += accessible_rolls;

        if accessible_rolls <= 0 {
            break;
        }
        remove_vec_from_vec(&mut locs, locs_to_remove);
     }
     
     println!("Number of rolls removed by forklift {}", removed_rolls);
     // Your puzzle answer was 8713.
     Ok(())
    }