use solver::read_lines;

/**
 * Problem: Find largest 12 digit numbers created from given strings of digits and sum them
 * Assumption: ALL strings in input will be > 12 digits, and also all be of the same length, l.
 *
 * Solution:
 *
 * LOWER BOUND
 * Finding the delta, d, between the len of the lines and 12 should tell us how many characters we can give up to construct the largest number
 * This means our 1st digit has to come from index, i = 0..=l-12
 * (d = l-12) -> i = 0..=d
 * our 2nd digit can come from i = 1..=d+1
 * our 3rd digit can come from i = 2..=d+2
 * ...
 * our nth digit can come from i = n-1..=d+n
 * -> If we shift this to 0-indexing, we can more simply say
 * our nth digit can come from n..=d+n
 *
 * The example, 987654321111111 has 15 characters -> d = 3 (# of characters omitted in number to sum)
 * our 1st digit, index 0, can come from 0..=3
 *
 *
 *
 * UPPER BOUND
 * Something has to inform how high value / low index we can pick the subsequent digits, based on the index of the last digit we chose
 * If we store the last chosen digit's index as m, our nth digit range becomes:
 *                     m+1..=m+1+d
 * But how many terms we skip also should inform how far ahead we can look, so m needs to be in the upper index term with something related to (d-m)
 * Need to calculate "skips remaining" to give proper end of search range
 * skips taken, k, could be counted as we find the largest digit in each (if we assign new max value, assign current iteration #)
 *                      m+1..=m+1+d-k
 * This would amount to how much the current m changed from the original low end of range (m0+1)
 *                      k = m-(m0+1)
 * Taken at each step, we would need to sum this as we go
 *                      total_k += k
 *
 * We can initialize m = -1, k = 0, total_k = 0 for the n = 0 term: 0..=d
 *
 * The example, 234234234234278 has 15 characters -> d = 3 (# of characters omitted in number to sum)
 * our 1st digit, index 0, can come from m+1..=m+1+d-k -> 0..=d -> 0..=3
 * 2342...
 * The highest value here is 4, at index m = 2
 * k = m-(m0+1) = 2-(-1+1) = 2 -> K = 2
 * our 2nd digit, index 1 can then come from m+1..=m+1+d-k -> 3..=4
 * ...23...
 * The highest value here is 3 at index m = 4
 * k = m-(m0+1) = 4-(2+1) = 1 -> K = 3 = d
 * our 3rd digit, index 2 can then come from m+1..=m+1+d-k -> 5..=5
 * ...4...
 * The highest value here is 4 at index m = 5
 * k = m-(m0+1) = 5-(4+1) = 0 -> K = 3 = d
 * our 4th digit, index 3 can then come from m+1..=m+1+d-k-> 6..=6
 * ...2...
 * The only value that satisfies this is the index 6 digit
 * So after we hit that point where the terms are the same, we can just add the rest of the digits to the end of the string
 * Our 12 digit number then becomes -> 434234234278
 *
 * We can iterate over these ranges, finding the highest number each time?
 * and when we do this 12 times, we have our number to add to the sum
 *
 * As for the algorithm to determine highest value index,
 * we only add if >, not ==, that way it never prefers later equivalent values at a lower base10 location
 * */

/**
 * My implementation of the above had several issues with boundaries while attempting to iterate over an inner loop with
 * for x in m+1..=m+1+d-k
 *
 * I enlisted a genAI chat to refactor what I had, while retaining the outer structure
 * I did this after the day 3 submittal window, as to not affect my points on the private leaderboard, but still attempt to learn something
 * */

fn main() -> std::io::Result<()> {
    // let lines = read_lines("../input/3/example.txt")?;
    let lines = read_lines("../input/3/input.txt")?;

    const ALLOWED_DIGITS: usize = 12;
    let mut l: usize = 0;
    let mut sum: i64 = 0;

    for line in lines {
        let bank = line?;
        // println!("bank: {}", bank);

        // Determine d the first time through
        if l == 0 {
            l = bank.len();
        }

        let chars: Vec<char> = bank.chars().collect();
        let total_len = chars.len();

        let mut selected = String::new();
        let mut m: isize = -1;

        while selected.len() < ALLOWED_DIGITS {
            let remaining_needed = ALLOWED_DIGITS as isize - selected.len() as isize;
            let remaining_digits = total_len as isize - (m + 1);

            // signed math so we can detect negative
            let mut max_skips_allowed = remaining_digits - remaining_needed;

            // if negative, clamp to 0
            if max_skips_allowed < 0 {
                max_skips_allowed = 0;
            }

            let start = (m + 1) as usize;
            let end = (start as isize + max_skips_allowed) as usize;

            let mut best_digit = -1;
            let mut best_index = m;

            for i in start..=end {
                let d = (chars[i] as u8 - b'0') as i64;
                if d > best_digit {
                    best_digit = d;
                    best_index = i as isize;
                }
            }

            selected.push(chars[best_index as usize]);
            m = best_index;
        }

        // println!("selected_digits: {}", selected);
        sum += selected.parse::<i64>().unwrap();
    }

    println!("The day 3 part 2 sum is {}", sum);
    // Your puzzle answer was 167384358365132.
    Ok(())
}
