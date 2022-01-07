/**
 * File: day09.rs
 * Author: Adam Jeniski; @Ajetski
 */

use std::collections::{HashSet, LinkedList};
type Position = (usize, usize);

fn is_local_min(data: &Vec<Vec<u64>>, (row, col): Position) -> bool {
    if row > 0 && data[row][col] >= data[row - 1][col] {
        return false;
    } else if row < data.len() - 1 && data[row][col] >= data[row + 1][col] {
        return false;
    } else if col > 0 && data[row][col] >= data[row][col - 1] {
        return false;
    } else if col < data[row].len() - 1 && data[row][col] >= data[row][col + 1] {
        return false;
    }
    true
}

fn get_local_mins(data: &Vec<Vec<u64>>) -> Vec<Position> {
    let mut mins = vec![];
    for row in 0..data.len() {
        for col in 0..data[row].len() {
            if is_local_min(data, (row, col)) {
                mins.push((row, col));
            }
        }
    }
    mins
}

/** use a BFS to count all of the nodes in the basin */ 
fn get_basin_size_from_min(data: &Vec<Vec<u64>>, start: Position) -> u64 {
    /* queue: pop from front; push to back */
    let mut search_queue = LinkedList::from([start]);
    let mut visited = HashSet::from([start]);
    let mut count = 1;

    while !search_queue.is_empty() {
        let (row, col) = search_queue.pop_front().unwrap();
        let mut directions = vec![];

        if row > 0 { directions.push((row - 1, col)) }
        if col > 0 { directions.push((row, col - 1)) }
        if row < data.len() - 1 { directions.push((row + 1, col)) }
        if col < data[row].len() - 1 { directions.push((row, col + 1)) }

        for (next_row, next_col) in directions {
            if data[next_row][next_col] != 9
                && !visited.contains(&(next_row, next_col)) {
                search_queue.push_back((next_row, next_col));
                count += 1;
            }
            visited.insert((next_row, next_col));
        }
    }

    count
}

fn parse_input(input: &str) -> Vec<Vec<u64>> {
    input
        .split_ascii_whitespace()
        .map(|a| a.chars().map(|c| c.to_string().parse().unwrap()).collect())
        .collect()
}

fn part_1_solution(input: &str) -> u64 {
    let data = parse_input(input);
    let mins = get_local_mins(&data);
    mins.len() as u64
        + mins.iter().fold(0, |acc, (row, col)| {
            acc + data[row.to_owned()][col.to_owned()]
        })
}

fn part_2_solution(input: &str) -> u64 {
    let data = parse_input(input);
    let mins = get_local_mins(&data);
    let mut basin_sizes: Vec<u64> = mins
        .iter()
        .map(|k| get_basin_size_from_min(&data, k.to_owned()))
        .collect();
    basin_sizes.sort();
    basin_sizes.reverse();
    basin_sizes.iter().take(3).fold(1, |prod, curr| prod * curr)
}

#[cfg(test)]
mod part1 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/9_test.txt");
        assert_eq!(part_1_solution(input), 15);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/9.txt");
        assert_eq!(part_1_solution(input), 502);
    }
}

#[cfg(test)]
mod part2 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/9_test.txt");
        assert_eq!(part_2_solution(input), 1134);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/9.txt");
        assert_eq!(part_2_solution(input), 1330560);
    }
}
