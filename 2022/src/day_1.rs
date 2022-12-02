advent_of_code_macro::solve_problem!(
    day 1,
    Input Vec<Vec<i32>>,
    parse |input: &str| {
        input.split("\n\n").map(|lines| lines.lines().map(|line| line.parse().unwrap()).collect()).collect()
    },
    part one |data: Input| {
        data.iter().map(|cals| cals.iter().sum()).max().unwrap()
    },
    part two |data: Input| {
        let mut cals: std::collections::BinaryHeap<i32>  = data.iter().map(|cals| cals.iter().sum()).collect();
        cals.pop().unwrap() + cals.pop().unwrap() + cals.pop().unwrap()
    },
    sample tests [24_000, 45_000],
    star tests [68_923, 200_044]
);
