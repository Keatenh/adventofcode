/**
 * Parse the lines as 2D coordinates
 * Find the area by taking the product of the deltas between the x and y portions of the coordinates
 * (plus 1 in each dimention to be inclusive of the starting row, columns)
 * Return the greatest product
 */
use solver::read_lines;

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/9/example.txt")?;
    let lines = read_lines("../input/9/input.txt")?;
    let lines_vec: Vec<String> = lines.map(|e| e.unwrap()).collect();
    let mut coords_vec: Vec<[i64; 2]> = Vec::new();

    for line in lines_vec {
        let c_strings: Vec<&str> = line.split(",").collect();
        let mut coords: [i64; 2] = [0; 2];
        for i in 0..2 {
            coords[i] = c_strings[i]
                .parse::<i64>()
                .expect("Invalid number in input");
        }
        coords_vec.push(coords);
    }

    let mut max_area = 0;

    let num_of_coords = coords_vec.len();
    println!("num of coords: {}", num_of_coords);

    for i in 0..num_of_coords {
        for j in i + 1..num_of_coords {
            let area = ((coords_vec[j][0] - coords_vec[i][0]).abs() + 1)
                * ((coords_vec[j][1] - coords_vec[i][1]).abs() + 1);
            // println!("i: {}, j: {}", i, j);
            // println!("area: {}", area);
            if area > max_area {
                max_area = area;
            }
        }
    }

    println!("The largest red square corner area is: {}", max_area);
    // Your puzzle answer was 4750092396.
    Ok(())
}
