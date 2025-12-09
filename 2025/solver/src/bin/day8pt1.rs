/**
 * Read lines in as x,y,z coords
 * For each coordinate, ci, compare it to the others, taking 3D distance:
 *      d = √(Δx²+Δy²+Δz²)
 * When we find the lowest d, we need to group its coords, cmin with ci
 * Maybe we can do this by simply giving it an incremental group #, and store
 * all this data in a vector of tuple (coords, group)
 *
 * We could store the calculated d between each position ahead of time,
 * and then take the min when we are in the main loop
 * Then sort the position data by d, and make the connections in order, assigning the group #
 */
use solver::read_lines;

fn distance(coords_i: [i64; 3], coords_f: [i64; 3]) -> f64 {
    let delta_x: i64 = coords_f[0] - coords_i[0];
    let delta_y: i64 = coords_f[1] - coords_i[1];
    let delta_z: i64 = coords_f[2] - coords_i[2];
    return ((delta_x * delta_x + delta_y * delta_y + delta_z * delta_z) as f64).sqrt();
}

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/8/example.txt")?;
    // let connections_allowed = 10;

    let lines = read_lines("../input/8/input.txt")?;
    let connections_allowed = 1000;

    let lines_vec: Vec<String> = lines.map(|e| e.unwrap()).collect();
    let mut coords_vec: Vec<[i64; 3]> = Vec::new();
    //The groups will represent unique circuits (aside from default ID '0')
    let mut groups: Vec<i64> = Vec::new();

    //push read lines into our custom obj
    //I got tired of fighting the compiler with .map() statements lol
    for line in lines_vec {
        let c_strings: Vec<&str> = line.split(",").collect();
        let mut coords: [i64; 3] = [0; 3];
        for i in 0..=2 {
            coords[i] = c_strings[i]
                .parse::<i64>()
                .expect("Invalid number in input");
        }
        coords_vec.push(coords);
        groups.push(0);
    }

    // println!("coords_vec: {:?}", coords_vec);
    let num_of_coords = coords_vec.len();

    //Gonna try a double for loop without repeating pair combinations, since the distance from A to B = distance B to A
    let mut distances: Vec<([usize; 2], i64)> = Vec::new();
    for i in 0..num_of_coords {
        for j in i + 1..num_of_coords {
            let d = distance(coords_vec[i], coords_vec[j]).floor() as i64;
            distances.push(([i, j], d));
        }
    }
    distances.sort_by_key(|e| e.1);

    // println!("distances: {:?}", distances);

    let mut new_group = 0;

    //Update number of skips based on number of merges of groups we perform...
    //This is bc we chose to go over the groups in order, but we can remove them, 
    //so we might have to skip over some iterations before we are truly done processing the counts
    let mut skips = 0;

    for n in 0..connections_allowed {
        let i = distances[n].0[0];
        let j = distances[n].0[1];

        let mut existing_group = 0;
        //If we have 2 existing groups, merge them
        if groups[i] != 0 && groups[j] != 0 {
            existing_group = groups[i].min(groups[j]);
            let to_replace = groups[i].max(groups[j]);
            for group in &mut groups {
                if *group == to_replace {
                    *group = existing_group;
                    skips += 1;
                }
            }
        } else if groups[i] != 0 || groups[j] != 0 {
            //If we have 1 existing group, use it
            existing_group = groups[i].max(groups[j]);
        }
        //If they are both ungrouped, create a new one
        if existing_group == 0 {
            new_group += 1;
            groups[i] = new_group;
            groups[j] = new_group;
        } else {
            //Make sure any ungrouped or merged groups get reassigned a group
            groups[i] = existing_group;
            groups[j] = existing_group;
        }
    }
    // println!("groups: {:?}", groups);

    let mut group_id_counts: Vec<usize> = Vec::new();
    let mut n = 1;

    loop {
        //ignore group 0, as they have no connections...
        let count = groups.iter().filter(|&&x| x == n).count();
        //If we stopped finding groups, we are done
        if count <= 0 {
            if skips <= 0 {
                break;
            } else {
                skips -= 1;
            }
        }
        group_id_counts.push(count);
        n += 1;
    }
    // println!("group_id_counts: {:?}", group_id_counts);
    //This is apparently how you can reverse sort in rust (largest to smallest)
    group_id_counts.sort_by(|a, b| a.cmp(b).reverse());
    //Get the product of the 3 largest circuits
    let circuit_product = group_id_counts[0..3]
        .iter()
        .fold(1, |acc: i64, x| acc * *x as i64);

    println!(
        "The product of the 3 largest circuits is: {}",
        circuit_product
    );
    // Your puzzle answer was 123234.
    Ok(())
}
