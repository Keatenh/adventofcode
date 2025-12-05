use solver::read_lines;

fn main() -> std::io::Result<()> {
    let mut fresh_ingredient_count: i64 = 0;

    // 1. Read in input lines
    // let lines = read_lines("../input/5/example.txt")?;
    let lines = read_lines("../input/5/input.txt")?;
    let mut ingredients: Vec<i64> = Vec::new();
    let mut freshness: Vec<(i64, i64)> = Vec::new();

    // 2. Parse lines into a vector of ranges and a vector of ingredient IDs, respectively
    // Switching from one to the other once we find an empty line
    let mut ingredient_flag = false;
    for line in lines {
        let text = line?; // unwrap
        if text == "" {
            ingredient_flag = true;
            continue;
        }
        if ingredient_flag {
            //process ingredients
            ingredients.push(text.parse().unwrap());
        } else {
            //process "freshness" ranges
            let r: Vec<&str> = text.split("-").collect();
            let fresh_range: (i64, i64) = (r[0].parse().unwrap(), r[1].parse().unwrap());
            freshness.push(fresh_range);
        }
    }

    // println!("freshness ranges: {:?}", freshness);
    // println!("ingredients: {:?}", ingredients);

    //3. Optimize the ranges such that they no longer overlap, so that we don't have to compare the IDs against every item in the range vector
    //   3-5
    //   10-14
    //   16-20
    //   12-18
    //   becomes:
    //   3-5
    //   10-20
    // My idea is to
    // 3a. sort by the min val in the range and iterate,
    // Check for: Partial Overlap
    // 3b. if max n >= min n+1 -> drop n and n+1 and insert term: min(n), max(n+1)
    // then repeat steps 3a and 3b on the mutated vector - could use a flag in between 3a. and 3b. so that if we never drop and insert / exit, then we know we are done
    // for Total Overlap, I suppose we just drop the vector with a range already contained in the other?
    freshness.sort_by_key(|&(min, _max)| min);
    // println!("freshness ranges (after sort): {:?}", freshness);

    //Note: I tried for i in 0..freshness.len()-1, but we need to manipulate i from the outer loop
    loop {
        let mut updated = false;
        let mut i = 0;

        while i < freshness.len() - 1 {
            let outer_min: i64 = freshness[i].0;
            let inner_max: i64 = freshness[i].1;
            let inner_min: i64 = freshness[i + 1].0;
            let outer_max: i64 = freshness[i + 1].1;
            if inner_max >= inner_min {
                freshness.remove(i);
                freshness.remove(i); //I'm assuming i+1 element will be i after 1st remove()...
                // Maybe if I use insert I won't have to continuously sort...
                //I originally just used the outer max, but if the latter term is contained within the former, we will basically just recreate the i-term with:
                freshness.insert(i, (outer_min, std::cmp::max(outer_max, inner_max))); 
                updated = true;
                break;
            }
            i += 1;
        }
        if !updated {
            break;
        }
    }
    // println!("freshness ranges (after dedupe): {:?}", freshness);

    // 4. Iterate over the ingredient vector, and search the values of the freshness vector from the start until the max value is <= the ingredient val
    // If no match is found, continue
    // If a matching range is found, and the min value is <= the ingredient val, increment the fresh ingredient count
    for ingredient in ingredients {
        for r in &freshness {
            // println!("i: {}", ingredient);
            // println!("r: {:?}", r);
            if ingredient <= r.1 {
                if ingredient >= r.0 {
                    fresh_ingredient_count+=1;
                }
                //After we make it this far, we don't have to check the rest of the ranges
                break;
            }
        }
    }

    println!(
        "Number of fresh ingredients found: {}",
        fresh_ingredient_count
    );
    // 710 is too low -> Edge Case: I didn't account for ranges completely contained within their previous range, after sorting on min value
    // Your puzzle answer was 739.
    Ok(())
}
