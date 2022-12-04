advent_of_code_macro::solve_problem!(
    day 4,
    Input Vec<Vec<i32>>,
    parse |input: &str| {
        input.lines()
            .map(|line| {
                let (a, rest) = line.split_once('-').unwrap();
                let (b, rest) = rest.split_once(',').unwrap();
                let (c, d) = rest.split_once('-').unwrap();
                [a, b, c, d].into_iter()
                    .map(str::parse)
                    .map(Result::unwrap)
                    .collect()
            }).collect()
    },
    part one |data: Input| {
        data.into_iter()
            .fold(0, |acc, curr|
                if (curr[0] <= curr[2] && curr[1] >= curr[3])
                    || (curr[2] <= curr[0] && curr[3] >= curr[1]) {
                    acc + 1
                } else {
                    acc
                }
            )
    },
    part two |data: Input| {
        data.into_iter()
            .fold(0, |acc, curr|
                if (curr[0] >= curr[2] && curr[0] <= curr[3])
                    || (curr[1] >= curr[2] && curr[1] <= curr[3])
                    || (curr[2] >= curr[0] && curr[2] <= curr[1])
                    || (curr[3] >= curr[0] && curr[3] <= curr[1]) {
                    acc + 1
                } else {
                    acc
                }
            )
    },
    sample tests [2, 4],
    star tests [450, 837]
);
