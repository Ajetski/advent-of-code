use std::collections::HashSet;

type Data = Vec<Vec<i32>>;
type Output = i32;
fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|line| {
            line.chars()
                .map(|c| c.to_digit(10).unwrap() as i32)
                .collect()
        })
        .collect()
}
fn part_one(data: Data) -> Output {
    let mut visible = HashSet::<(usize, usize)>::new();
    (0..data.len()).for_each(|i| {
        let mut max = -1;
        for j in 0..data[i].len() {
            if data[i][j] > max {
                max = data[i][j];
                visible.insert((i, j));
            }
        }

        let mut max = -1;
        for j in (0..data[i].len()).rev() {
            if data[i][j] > max {
                max = data[i][j];
                visible.insert((i, j));
            }
        }
    });
    for j in 0..data.len() {
        let mut max = -1;
        (0..data.len()).for_each(|i| {
            if data[i][j] > max {
                max = data[i][j];
                visible.insert((i, j));
            }
        });

        let mut max = -1;
        for i in (0..data.len()).rev() {
            let i = i as usize;
            if data[i][j] > max {
                max = data[i][j];
                visible.insert((i, j));
            }
        }
    }
    visible.len() as i32
}
fn part_two(data: Data) -> Output {
    let mut max_count = -1;
    for (i, row) in data.iter().enumerate() {
        for (j, height) in row.iter().enumerate() {
            let mut down = 0;
            for idx in i + 1..row.len() {
                down += 1;
                if data[idx][j] >= *height {
                    break;
                }
            }

            let mut up = 0;
            for idx in (0..i).rev() {
                up += 1;
                if data[idx][j] >= *height {
                    break;
                }
            }

            let mut right = 0;
            for j in j + 1..row.len() {
                right += 1;
                if data[i][j] >= *height {
                    break;
                }
            }

            let mut left = 0;
            for j in (0..j).rev() {
                left += 1;
                if data[i][j] >= *height {
                    break;
                }
            }

            max_count = std::cmp::max(up * left * right * down, max_count);
        }
    }
    max_count
}

advent_of_code_macro::generate_tests!(
    day 8,
    parse,
    part_one,
    part_two,
    sample tests [21, 8],
    star tests [1_719, 590_824]
);
