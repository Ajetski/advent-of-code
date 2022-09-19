/**
 * File: day10.rs
 * Author: Adam Jeniski; @Ajetski
 */
use std::collections::HashMap;

type Stack = Vec<char>;

fn part_1_solution(input: &str) -> u64 {
    let point_table = HashMap::<char, u64>::from([(')', 3), (']', 57), ('}', 1197), ('>', 25137)]);

    let match_table = HashMap::<char, char>::from([(')', '('), (']', '['), ('}', '{'), ('>', '<')]);

    let mut count = 0;

    for line in input.split_ascii_whitespace() {
        let mut stack = Stack::new();
        for c in line.chars() {
            if "([{<".contains(c) {
                stack.push(c);
            } else {
                let start = stack.pop().unwrap();
                if start != *(match_table.get(&c).unwrap()) {
                    count += point_table.get(&c).unwrap();
                    break;
                }
            }
        }
    }

    count
}

fn part_2_solution(input: &str) -> u64 {
    let point_table = HashMap::<char, u64>::from([(')', 1), (']', 2), ('}', 3), ('>', 4)]);

    let match_table_from_start =
        HashMap::<char, char>::from([('(', ')'), ('[', ']'), ('{', '}'), ('<', '>')]);

    let match_table_from_end =
        HashMap::<char, char>::from([(')', '('), (']', '['), ('}', '{'), ('>', '<')]);

    let mut counts = vec![];

    'outer: for line in input.split_ascii_whitespace() {
        let mut stack = Stack::new();
        for c in line.chars() {
            if "([{<".contains(c) {
                stack.push(c);
            } else {
                let start = stack.pop().unwrap();
                println!("{} {}", start, c);
                if start != *(match_table_from_end.get(&c).unwrap()) {
                    continue 'outer;
                }
            }
        }
        let mut count = 0;
        while !stack.is_empty() {
            let c = stack.pop().unwrap();
            count *= 5;
            count += point_table
                .get(match_table_from_start.get(&c).unwrap())
                .unwrap();
        }
        counts.push(count);
    }

    counts.sort();
    counts[counts.len() / 2]
}

#[cfg(test)]
mod part1 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/10_test.txt");
        assert_eq!(part_1_solution(input), 26397);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/10.txt");
        assert_eq!(part_1_solution(input), 339477);
    }
}

#[cfg(test)]
mod part2 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/10_test.txt");
        assert_eq!(part_2_solution(input), 288957);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/10.txt");
        assert_eq!(part_2_solution(input), 3049320156);
    }
}
