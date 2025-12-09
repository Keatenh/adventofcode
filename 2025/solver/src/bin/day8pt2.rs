/**
 * This time, we keep connecting until the groups are all the same #
 * Then take the last 
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
    let lines = read_lines("../input/8/input.txt")?;

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

    let mut n = 0;
    let mut i_final = 0;
    let mut j_final = 0;

    loop {
        //Exit the loop if we connect all groups (none are default '0')
        if !groups.contains(&0) {
            break;
        }

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
        i_final = i;
        j_final = j;
        n+=1;
    }
    // println!("groups: {:?}", groups);
    // println!("i final: {}", i_final);
    // println!("i final coords: {:?}", coords_vec[i_final]);
    // println!("j final: {}", j_final);
    // println!("j final coords: {:?}", coords_vec[j_final]);
    
    let last_junction_x_product = coords_vec[i_final][0] * coords_vec[j_final][0];

    println!(
        "The product of X coordinates of the last two junction boxes: {}",
        last_junction_x_product
    );
    // Your puzzle answer was 9259958565.
    Ok(())
}
