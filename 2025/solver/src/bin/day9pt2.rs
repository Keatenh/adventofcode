/**
 * We have to create a boundary using the list of coordinates that we cannot leave.
 * Perhaps, starting at each of these boundary points,
 * we could check traverse diagonally,
 * until we are about to go out of bounds, and calculate the area at that location.
 *
 * We might have to traverse in at least 2 (if not 4) unique diagonal directions to
 * not miss a square. for example, traversing a path right might prematurely exit the bounds
 * sooner than traversing left from the same y coordinate, on the opposite x boundary,
 * depending on the shape we create with the input.
 *
 * We could also read ahead, to determine what type of corner we are creating with each new coord
 *
 * 7,1  -> next +x, =y | previous (last member, (7,3) of =x, -y
 * -> so this coner is from a line going up (since 0 is the top), and a line going right (0 is the left)
 *  So this is a top-left corner, and we should traverse right and down (+x, +y)
 * 11,1  -> next =x, +y | prev +x, =y -> top right corner, should traverse left and down (-x, +y)
 * ->
 * 11,7
 * 9,7
 * 9,5
 * 2,5
 * 2,3
 * 7,3
 *
 * The flipping of signs for different coordinates under different scenarios might be related to a rotation matrix...
 *
 * Perhaps we store all of the allowed points in the shape into a hash map,
 * Then we do the same calculations as before between every pair of points,
 * except this time, we also iterate over the path from p1 to p2
 * So, for (7,1), we decided to go +x & +y
 * We can check its border paths by looking along
 * x1..=x2, y1
 * x1..=x2, y2
 * x1, y1..=y2
 * x2, y1..=y2
 * and if any of these point is NOT in the hashmap, then the area is invalid
 *
 *
 * GENERATING THE HASHMAP OF VALID POINTS
 * In the example, it looks like we can use every other set of 3 points (2 with the middle skipped)
 * to generate the points that could go into the hash map,
 * but that might be a feature of its specific geometry...
 *
 *
 * So, my hypothesis is that the answer has to contain only the points found in
 * this ^ superset of valid rectangles.
 *
 * We could check if we are moving CW or CCW (Or assume one or the other).
 * We can make the superset by adding when the turns match the direction of the shape,
 * and subtracting those when the turns are opposite the direction of the shape
 *
 *
 * THE DIRECTION OF THE SHAPE
 * Perhaps the final link between points holds the answer to that:
 *
 * 7 -> 0 -> 1
 * 7,3 -> 7,1 -> 11,1 This is a corner going -y (up), +x (right) OH YEAH ORDER MATTERS FOR THOSE
 * Since the final join is CW, the whole thing is CW, and any CCW turns result in invalid rects?
 *
 * 7 -> 0 -> 1 - 1st corner, so rect 7,1 is always valid
 * ADD points from 7,1 rect
 *
 * ASSUMPTIONS:
 * (I'm gonnna choose to IGNORE the edge case of single steps
 *  and assume there is not a single line that out "areas" the > 1-D rectangles)
 *
 * I'm also gonna assume all the points are TURNS, and that they dont just put
 * 3 points along the same dimension in a row
 *
 * We know this 1st term is CW bc those operations are:
 * +x,+y
 * +y,-x
 * -x,-y
 * -y,+x
 *
 * Whereas CCW operations would be:
 * -x,+y
 * +y,+x
 * +x,-y
 * -y,-x
 *
 *
 * 0 -> 1 -> 2 - 2nd corner
 * 7,1 -> 11,1 -> 11,7 => +x,+y = CW (Matching)
 * ADD points from 0,2 rect
 *
 * 1 ->  2 -> 3 - 3rd corner
 * 11,1 -> 11,7 -> 9,7 => +y,-x = CW (Matching)
 * ADD points from 1,3 rect
 *
 * 2 -> 3 -> 4 - 4th corner
 * 11,7 -> 9,7 -> 9,5 => -x,-y = CW (Matching)
 * ADD points from 2,4 rect
 *
 * 3 -> 4 -> 5 - 5th corner
 * 9,7 -> 9,5 -> 2,5 => -y,-x = CCW (Opposite)
 * SUBTRACT points from 3,5 rect
 *
 * 4 -> 5 -> 6 - 6th corner
 * 9,5 -> 2,5 -> 2,3 -> => -x,-y = CW (Matching)
 * Add points from 4,6 rect
 *
 * 5 -> 6 -> 7 - 7th corner
 * 2,5 -> 2,3 -> 7,3 => -y,-x = CCW (Opposite)
 * SUBTRACT points from 5,7 rect
 *
 * * 6 -> 7 -> 8 (aka 0) - 8th (Final) corner
 * 2,3 -> 7,3 -> 7,1 => +x,-y = CCW (Opposite)
 * SUBTRACT points from 6,0 rect
 *
 * Also note that we will likely run out of RAM if we try to store ALL of the individual points into a set,
 * so we maybe we could:
 * A. Figure out how to calculate the needed areas only as we process potential target squares?
 * B. Store JUST the boundaries of the resultant shape into the set, since when we iterate,
 *  we will cross those points first upon the out-of-bounds scenarios and break/exit anyway
 *
 * For B ^, how do we subtract a rectangular boundary from another?
 * We could temporarily get all points of each crossing boundary, get the diff set, and then
 * generate the new bounadries?
 */
// Looking ahead at the input our 1st corner (len-1 -> 0 -> 1)is +x,+y, or right and down
// So using this, I will assume a clockwise shape, where every 2 entrys is a turn/corner,
// just like our example data
use std::{
    cmp::{max, min},
    collections::HashSet,
};

use solver::read_lines;

/**
 * Based on observations above, if we are turning CW:
 * - if we change x first, dx1 and dy2 have the SAME sign
 * - if we change y first, dy1 and dx2 have the OPPOSITE sign
 *
 * Conversely, if we are turning CCW:
 * - if we change x first, dx1 and dy2 have the OPPOSITE sign
 * - if we change y first, dy1 and dx2 have the SAME sign
 */
fn turning_cw(dx1: i64, dy1: i64, dx2: i64, dy2: i64) -> bool {
    if dy1 == 0 {
        //changed x 1st, dx2 == 0
        if dx1.signum() == dy2.signum() {
            return true;
        }
    } else {
        // changed y 1st, dx1 == 0, dy2 == 0
        if dy1.signum() != dx2.signum() {
            return true;
        }
    }
    return false;
}

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
    // println!("coords: {:?}", coords_vec);

    let mut points_in_bounds: HashSet<String> = HashSet::new();

    let mut max_area = 0;

    let num_of_coords = coords_vec.len();
    
    for n in 0..num_of_coords {
        println!("n: {}", n);
        //Handling index overflow, since I don't think rust vectors interpret n = -1
        let prev_x: i64;
        let prev_y: i64;
        let next_x: i64;
        let next_y: i64;
        if n == 0 {
            prev_x = coords_vec[num_of_coords - 1][0];
            prev_y = coords_vec[num_of_coords - 1][1];
        } else {
            prev_x = coords_vec[n - 1][0];
            prev_y = coords_vec[n - 1][1];
        }

        if n == num_of_coords - 1 {
            next_x = coords_vec[0][0];
            next_y = coords_vec[0][1];
        } else {
            next_x = coords_vec[n + 1][0];
            next_y = coords_vec[n + 1][1];
        }

        let delta_x_1 = coords_vec[n][0] - prev_x;
        let delta_y_1 = coords_vec[n][1] - prev_y;
        let delta_x_2 = next_x - coords_vec[n][0];
        let delta_y_2 = next_y - coords_vec[n][1];

        // println!("prev_x: {}", prev_x);
        // println!("prev_y: {}", prev_y);
        // println!("next_x: {}", next_x);
        // println!("next_y: {}", next_y);

        let cw = turning_cw(delta_x_1, delta_y_1, delta_x_2, delta_y_2);
        //The iterator here must be positive
        let mut ls_x = min(prev_x, next_x);
        let mut gr_x = max(prev_x, next_x);
        let mut ls_y = min(prev_y, next_y);
        let mut gr_y = max(prev_y, next_y);

        if cw {
            for x in ls_x..=gr_x {
                for y in ls_y..=gr_y {
                    let pt = format!("{},{}", x, y);
                    // println!("Adding pt {}", pt);
                    points_in_bounds.insert(pt);
                }
            }
        } else {
            //ccw
            //When we subtract for ccw, we do not want to remove the points along the border itself...
            //Thus we have a different, smaller range
            if delta_y_1 == 0 {
                //changed x 1st, dx2 == 0
                if delta_x_1 > 0 {
                    //moving right
                    if delta_y_2 > 0 {
                        //then down
                        // ignore the top, and the right
                        gr_x -= 1;
                        ls_y += 1;
                    } else {
                        //then up
                        // ignore the bottom, and the right
                        gr_x -= 1;
                        gr_y -= 1;
                    }
                } else {
                    //moving left
                    if delta_y_2 > 0 {
                        //then down
                        // ignore the top, and the left
                        ls_x += 1;
                        ls_y += 1;
                    } else {
                        //then up
                        //ignore the bottom, and the left
                        ls_x += 1;
                        gr_y -= 1;
                    }
                }
            } else {
                // changed y 1st, dx1 == 0, dy2 == 0
                if delta_y_1 > 0 {
                    //moving down
                    if delta_x_2 > 0 {
                        //then right
                        //ignore the left, and the bottom
                        ls_x += 1;
                        gr_y -= 1;
                    } else {
                        //then left
                        //ignore the right, and the bottom
                        gr_x -= 1;
                        gr_y -= 1;
                    }
                } else {
                    //moving up
                    if delta_x_2 > 0 {
                        //then right
                        //ignore the left, and the top
                        ls_x += 1;
                        ls_y += 1;
                    } else {
                        //then left
                        //ignore the right, and the top
                        gr_x -= 1;
                        ls_y += 1;
                    }
                }
            }
            for x in ls_x..=gr_x {
                for y in ls_y..=gr_y {
                    let pt = format!("{},{}", x, y);
                    // println!("Removing pt {}", pt);
                    points_in_bounds.remove(&pt);
                }
            }
        }
        println!("# of points in bounds: {:?}", points_in_bounds.len());
    }

    // println!("points in bounds: {:?}", points_in_bounds);
    println!("# of points in bounds: {:?}", points_in_bounds.len());

    //Now, check the original pairs of coords for rectangles
    for i in 0..num_of_coords {
        for j in i + 1..num_of_coords {
            println!("i: {}, j: {}", i,j);
            let start_x = coords_vec[i][0];
            let start_y = coords_vec[i][1];
            let end_x = coords_vec[j][0];
            let end_y = coords_vec[j][1];

            let dir_x = (end_x - start_x).signum();
            let dir_y = (end_y - start_y).signum();


            let mut k = start_x;
            let mut l = start_y;

            let mut allowed = true;

            //Traverse along the x, then y for one boundary
            loop {
                if k == end_x {
                    break;
                }
                //If either of the traversals include a point NOT included in the hashmap,
                //then that pair is NOT valid
                if !points_in_bounds.contains(&format!("{},{}", k, start_y)) {
                    allowed = false;
                    break;
                }
                k += dir_x;
            }
            if allowed {
                loop {
                    if l == end_y {
                        break;
                    }
                    if !points_in_bounds.contains(&format!("{},{}", end_x, l)) {
                        allowed = false;
                        break;
                    }
                    l += dir_y;
                }
            }
            if allowed {
                //Traverse along the y, then x for the other boundary
                k = start_x;
                l = start_y;
                loop {
                    if l == end_y {
                        break;
                    }
                    if !points_in_bounds.contains(&format!("{},{}", start_x, l)) {
                        allowed = false;
                        break;
                    }
                    l += dir_y;
                }
                if allowed {
                    loop {
                        if k == end_x {
                            break;
                        }
                        if !points_in_bounds.contains(&format!("{},{}", k, end_y)) {
                            allowed = false;
                            break;
                        }
                        k += dir_x;
                    }
                }
            }

            //If we continue past this  check, the pair IS valid.
            //So, take the area and compare that to our largest yet.
            if allowed {
                let area = ((coords_vec[j][0] - coords_vec[i][0]).abs() + 1)
                    * ((coords_vec[j][1] - coords_vec[i][1]).abs() + 1);
                // println!("i: {}, j: {}", i, j);
                // println!("area: {}", area);
                if area > max_area {
                    max_area = area;
                    println!("max_area: {}", max_area);
                }
            }
        }
    }

    println!("The largest red square corner area is: {}", max_area);
    //113596470 is too low :(
    // Your puzzle answer was ___.
    Ok(())
}
