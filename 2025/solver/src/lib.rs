use std::fs::File;
use std::io::{self, BufRead, BufReader, Read};
use std::path::Path;

pub fn read_lines<P>(filename: P)
    -> io::Result<io::Lines<io::BufReader<File>>>
where
    P: AsRef<Path>,
{
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}

pub fn read_last_line(file_path: &str) -> io::Result<Option<String>> {
    let mut file = File::open(file_path)?;
    let mut contents = String::new();
    file.read_to_string(&mut contents)?;

    // Find the last line by splitting into lines and taking the last element
    let last_line = contents.lines().last().map(|s| s.to_string());
    Ok(last_line)
}

pub fn read_lines_preserve_whitespace<P>(filename: P) -> io::Result<Vec<String>>
where
    P: AsRef<Path>,
{
    let file = File::open(filename)?;
    let mut contents = String::new();
    std::io::Read::read_to_string(&mut BufReader::new(file), &mut contents)?;

    Ok(contents
        .split('\n')     // split without trimming
        .map(|s| s.to_string())
        .collect())
}

pub fn read_last_line_preserve_whitespace(file_path: &str) -> io::Result<Option<String>> {
    let mut file = File::open(file_path)?;
    let mut contents = String::new();
    file.read_to_string(&mut contents)?;

    if contents.is_empty() {
        return Ok(None);
    }

    // Keep the final line *exactly* as written (minus trailing '\n')
    // by manually splitting on '\n'
    let last = contents
        .trim_end_matches('\n')   // temporarily remove final newline
        .rsplit_once('\n')
        .map(|(_, line)| line.to_string())
        .or_else(|| Some(contents.trim_end_matches('\n').to_string()));

    Ok(last)
}
