use std::{cmp::Ordering, vec};

#[derive(Debug, Copy, Clone, Eq, PartialEq)]
struct Path {
    x: usize,
    y: usize,
    length: u32,
}
impl From<&Path> for i32 {
    fn from(val: &Path) -> Self {
        val.x as i32 + val.y as i32 - val.length as i32
    }
}
impl Ord for Path {
    fn cmp(&self, other: &Self) -> Ordering {
        i32::from(self).cmp(&i32::from(other))
    }
}
impl PartialOrd for Path {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

fn part_one(input: Vec<Vec<u8>>) -> Result<u32, &'static str> {
    let height = input.len();
    let width = input[0].len();
    let mut dp = vec![vec![0u32; width]; height];
    for row in 0..height {
        for col in 0..width {
            let mut min = u32::MAX;
            if row > 0 {
                min = std::cmp::min(min, dp[row - 1][col] as u32);
            }
            if col > 0 {
                min = std::cmp::min(min, dp[row][col - 1] as u32);
            }
            if min == u32::MAX {
                min = 0;
            }
            println!("{} {} {}", row, col, min);
            dp[row][col] += input[row][col] as u32 + min;
        }
    }
    for row in 0..height {
        for col in 0..width {
            let mut min = u32::MAX;
            if row + 1 < height {
                min = std::cmp::min(min, input[row + 1][col] as u32);
            }
            if col + 1 < width {
                min = std::cmp::min(min, input[row][col + 1] as u32);
            }
            if min == u32::MAX {
                min = 0;
            }
            dp[row][col] = std::cmp::min(dp[row][col], dp[row][col] + input[row][col] as u32 + min);
        }
    }
    Ok(dp[height - 1][width - 1] - 1)
}

#[cfg(test)]
mod tests {

    use super::*;

    fn parse_input(input: &str) -> Vec<Vec<u8>> {
        input
            .split_ascii_whitespace()
            .map(|s| s.chars().map(|c| c.to_digit(10).unwrap() as u8).collect())
            .collect()
    }

    #[test]
    fn part_one_sample_test() {
        let input = parse_input(include_str!("../inputs/15_test.txt"));
        let res = part_one(input);
        println!("{:?}", res);
        assert!(res == Ok(40));
    }

    // #[test]
    // fn part_one_test() {
    //     let input = parse_input(include_str!("../inputs/15.txt"));
    //     let res = part_one(input);
    //     println!("{:?}", res);
    //     assert!(res == Ok(40));
    // }
}
