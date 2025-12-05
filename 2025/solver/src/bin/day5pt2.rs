use solver::read_lines;

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/5/example.txt")?;
    let lines = read_lines("../input/5/input.txt")?;
    let mut freshness: Vec<(i64, i64)> = Vec::new();

    // 2. Parse lines into a vector of ranges and a vector of ingredient IDs, respectively
    // Switching from one to the other once we find an empty line
    for line in lines {
        let text = line?; // unwrap
        if text == "" {
            break;
        }
        //process "freshness" ranges
        let r: Vec<&str> = text.split("-").collect();
        let fresh_range: (i64, i64) = (r[0].parse().unwrap(), r[1].parse().unwrap());
        freshness.push(fresh_range);
    }
    //3. Optimize the ranges such that they no longer overlap, so that we don't have to compare the IDs against every item in the range vector
    freshness.sort_by_key(|&(min, _max)| min);

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
                freshness.remove(i);
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
    // 4. For Part 2, we simply need to accumulate the delta of the ranges, +1 to be inclusive of ALL values:
    let fresh_ingredient_count = freshness.iter().fold(0,|acc: i64, x| {
        acc+(x.1-x.0)+1
    });

    println!(
        "Number of fresh ingredients found: {}",
        fresh_ingredient_count
    );
    // Your puzzle answer was 344486348901788.
    Ok(())
}
