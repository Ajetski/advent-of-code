use std::{
    cmp::Ordering,
    collections::{BinaryHeap, HashMap},
    vec,
};

#[derive(Debug, Copy, Clone, Eq, PartialEq)]
struct Path {
    x: usize,
    y: usize,
    length: u32,
}
impl From<&Path> for u32 {
    fn from(val: &Path) -> Self {
        val.length
    }
}
impl Ord for Path {
    fn cmp(&self, other: &Self) -> Ordering {
        u32::from(other).cmp(&u32::from(self))
    }
}
impl PartialOrd for Path {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

fn part_one(input: Vec<Vec<u32>>) -> Result<u32, &'static str> {
    let mut queue = BinaryHeap::new();
    let mut dp = HashMap::new();
    let num_rows = input.len() as i32;
    let num_cols = input[0].len() as i32;
    let target_x = num_rows as usize - 1;
    let target_y = num_cols as usize - 1;

    queue.push(Path {
        x: 0,
        y: 0,
        length: 0,
    });
    dp.insert((0, 0), 0);

    'outer: while let Some(path) = queue.pop() {
        if path.x == target_x && path.y == target_y {
            return Ok(path.length);
        }

        // skip this if we've seen better
        if let Some(length) = dp.get(&(path.x, path.y)) {
            if *length < path.length {
                continue 'outer;
            }
        }

        'inner: for (x_offset, y_offset) in [(-1, 0), (1, 0), (0, -1), (0, 1)] {
            let x = x_offset + path.x as i32;
            let y = y_offset + path.y as i32;

            if x < 0 || x >= num_rows || y < 0 || y >= num_cols {
                continue 'inner;
            }

            let x = x as usize;
            let y = y as usize;
            let length = path.length + input[x][y];

            let entry = dp.entry((x, y)).or_insert(u32::MAX);
            if length < *entry {
                *entry = length;
                queue.push(Path { x, y, length });
            }
        }
    }

    Err("couldn't find an answer")
}

fn part_two(mut input: Vec<Vec<u32>>) -> Result<u32, &'static str> {
    let num_rows = input.len();
    let num_cols = input[0].len();

    for offset in 1..5 {
        for row in input.iter_mut().take(num_rows) {
            for idx in 0..num_cols {
                let mut val = row[idx] + offset;
                while val > 9 {
                    val -= 9;
                }
                if val == 0 {
                    val = 1;
                }
                row.push(val);
            }
        }
    }
    for offset in 1..5 {
        for row_idx in 0..num_rows {
            input.push(vec![]);
            let last_row_idx = input.len();
            for col_idx in 0..input[row_idx].len() {
                let mut val = input[row_idx][col_idx] + offset;
                while val > 9 {
                    val -= 9;
                }
                if val == 0 {
                    val = 1;
                }
                input[last_row_idx - 1].push(val);
            }
        }
    }

    part_one(input)
}

#[cfg(test)]
mod tests {

    use super::*;

    fn parse_input(input: &str) -> Vec<Vec<u32>> {
        input
            .split_ascii_whitespace()
            .map(|s| s.chars().map(|c| c.to_digit(10).unwrap()).collect())
            .collect()
    }

    #[test]
    fn part_one_sample_test() {
        let input = parse_input(include_str!("../inputs/15_test.txt"));
        assert!(part_one(input) == Ok(40));
    }

    #[test]
    fn part_one_test() {
        let input = parse_input(include_str!("../inputs/15.txt"));
        assert!(part_one(input) == Ok(581));
    }

    #[test]
    fn part_two_sample_test() {
        let input = parse_input(include_str!("../inputs/15_test.txt"));
        assert!(part_two(input) == Ok(315));
    }

    #[test]
    fn part_two_test() {
        let input = parse_input(include_str!("../inputs/15.txt"));
        assert!(part_two(input) == Ok(2916));
    }
}
