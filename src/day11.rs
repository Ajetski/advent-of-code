/**
 * File: day11.rs
 * Author: Adam Jeniski; @Ajetski
 */

fn part_1_solution(mut input: Vec<Vec<i32>>) -> u64 {
    fn flash(data: &mut Vec<Vec<i32>>, row: usize, col: usize, count: &mut u64) {
        *count += 1;
        //println!("flash {row} {col}");
        data[row][col] = 0;

        for (i, j) in (-1i32..=1).flat_map(|i| (-1i32..=1).map(move |j| (i, j))) {
            // compute coords as i32 so we can do bounds checking
            let x = i + row as i32;
            let y = j + col as i32;
            //print!("{row}, {col}; {x}, {y}. ");

            // skip out of bounds
            if x < 0 || y < 0 || x >= data.len() as i32 || y >= data[x as usize].len() as i32 {
                //println!("exiting");
                continue;
            }

            // convert coords to usize for easy indexing
            let x = x as usize;
            let y = y as usize;

            if data[x][y] != 0 {
                data[x][y] += 1;
                //println!("updated data to {}", data[x][y]);
            }
            if data[x][y] > 9 {
                //println!("flushing");
                flash(data, x, y, count);
            }
        }
    }
    let mut count = 0;
    for _ in 0..100 {
        for row in &mut input {
            for cell in row {
                *cell += 1;
            }
        }
        for i in 0..input.len() {
            for j in 0..input[i].len() {
                if input[i][j] > 9 {
                    flash(&mut input, i, j, &mut count);
                }
            }
        }
    }
    count
}

fn part_2_solution(mut input: Vec<Vec<i32>>) -> u64 {
    fn flash(data: &mut Vec<Vec<i32>>, row: usize, col: usize) {
        //println!("flash {row} {col}");
        data[row][col] = 0;

        for (i, j) in (-1i32..=1).flat_map(|i| (-1i32..=1).map(move |j| (i, j))) {
            // compute coords as i32 so we can do bounds checking
            let x = i + row as i32;
            let y = j + col as i32;
            //print!("{row}, {col}; {x}, {y}. ");

            // skip out of bounds
            if x < 0 || y < 0 || x >= data.len() as i32 || y >= data[x as usize].len() as i32 {
                //println!("exiting");
                continue;
            }

            // convert coords to usize for easy indexing
            let x = x as usize;
            let y = y as usize;

            if data[x][y] != 0 {
                data[x][y] += 1;
                //println!("updated data to {}", data[x][y]);
            }
            if data[x][y] > 9 {
                //println!("flushing");
                flash(data, x, y);
            }
        }
    }
    for count in 1..100000 {
        for row in &mut input {
            for cell in row {
                *cell += 1;
            }
        }
        for i in 0..input.len() {
            for j in 0..input[i].len() {
                if input[i][j] > 9 {
                    flash(&mut input, i, j);
                }
            }
        }
        if input.iter().all(|row| row.iter().all(|cell| cell == &0)) {
            return count;
        }
    }
    todo!()
}

#[cfg(test)]
mod part1 {
    use super::*;
    fn parse_input(input: &str) -> Vec<Vec<i32>> {
        input
            .split_ascii_whitespace()
            .map(|s| s.chars().map(|c| c.to_string().parse().unwrap()).collect())
            .collect()
    }

    #[test]
    fn day_11_sample_part_one() {
        let input = include_str!("../inputs/11_test.txt");
        let sol = part_1_solution(parse_input(input));
        assert_eq!(sol, 1656);
    }

    #[test]
    fn day_11_test_part_one() {
        let input = include_str!("../inputs/11.txt");
        assert_eq!(part_1_solution(parse_input(input)), 1725);
    }

    #[test]
    fn day_11_sample_part_two() {
        let input = include_str!("../inputs/11_test.txt");
        assert_eq!(part_2_solution(parse_input(input)), 195);
    }

    #[test]
    fn day_11_test_part_two() {
        let input = include_str!("../inputs/11.txt");
        assert_eq!(part_2_solution(parse_input(input)), 308);
    }
}
