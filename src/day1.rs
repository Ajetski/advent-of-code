#![allow(dead_code)]

fn parse_input(input: &str) -> Vec<u32> {
    input.split_ascii_whitespace().map(|x| x.parse().unwrap() ).collect()
}

fn part_1_solution(input: &str) -> u64 {
    parse_input(input)
        .as_slice()
        .windows(2)
        .fold(0, |acc, w| if w[0] < w[1] { acc + 1 } else { acc })
}

fn part_2_solution(input: &str) -> u64 {
    parse_input(input)
        .as_slice()
        .windows(4)
        .fold(0, |acc, w| if w[0] < w[3] { acc + 1 } else { acc })
}

#[cfg(test)]
mod part1 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/1_test.txt");
        assert_eq!(part_1_solution(input), 7);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/1.txt");
        assert_eq!(part_1_solution(input), 1139);
    }
}

#[cfg(test)]
mod part2 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/1_test.txt");
        assert_eq!(part_2_solution(input), 5);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/1.txt");
        assert_eq!(part_2_solution(input), 1103);
    }
}
