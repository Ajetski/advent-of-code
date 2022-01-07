#![allow(dead_code)]

/**
 * File: day11.rs
 * Author: Adam Jeniski; @Ajetski
 */

type Data = Vec<Vec<u8>>;

fn parse_input(input: &str) -> Data {
    input
        .split_ascii_whitespace()
        .map(|s| s.chars().map(|c| c.to_string().parse().unwrap()).collect())
        .collect()
}

fn flash(data: &mut Data, row: usize, col: usize) {
    data[row][col] += 1;
    if row > 0 && col > 0 {
        data[row - 1][col - 1] += 1;
    }
    if row > 0 {
        data[row - 1][col] += 1;
    }
    if row > 0 && col < data[row].len() - 1 {
        data[row - 1][col + 1] += 1;
    }
    if col < data[row].len() - 1 {
        data[row][col + 1] += 1;
    }
    if row < data.len() - 1 && col < data[row].len() - 1 {
        data[row + 1][col + 1] += 1;
    }
    if row < data.len() - 1 {
        data[row + 1][col] += 1;
    }
    if row < data.len() - 1 && col > 0 {
        data[row + 1][col - 1] += 1;
    }
    if col > 0 {
        data[row][col - 1] += 1;
    }
}

fn part_1_solution(input: &str) -> u64 {
    let mut data = parse_input(input);
    let mut count = 0;

    for _ in 0..100 {
        // increase energy
        for row in 0..data.len() {
            for col in 0..data[row].len() {
                data[row][col] += 1;
            }
        }

        // while any flashable cells exits; flash them
        while data.iter().any(|row| row.iter().any(|cell| *cell == 10)) {
            for row in 0..data.len() {
                for col in 0..data[row].len() {
                    if data[row][col] == 10 {
                        flash(&mut data, row, col);
                    }
                }
            }
        }

        // count all the flashed cells and reset them to zero
        for row in 0..data.len() {
            for col in 0..data[row].len() {
                if data[row][col] > 9 {
                    data[row][col] = 0;
                    count += 1;
                }
            }
        }
    }
    count
}

#[cfg(test)]
mod part1 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/11_test.txt");
        let sol = part_1_solution(input);
        assert_eq!(sol, 1656);
    }

    // #[test]
    // fn run() {
    //     let input = include_str!("../inputs/11.txt");
    //     assert_eq!(part_1_solution(input), 339477);
    // }
}

// #[cfg(test)]
// mod part2 {
//     use super::*;

//     #[test]
//     fn run_sample() {
//         let input = include_str!("../inputs/11_test.txt");
//         assert_eq!(part_2_solution(input), 288957);
//     }

//     #[test]
//     fn run() {
//         let input = include_str!("../inputs/11.txt");
//         assert_eq!(part_2_solution(input), 3049320156);
//     }
// }
