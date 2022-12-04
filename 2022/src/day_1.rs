type Input = Vec<Vec<i32>>;

fn parse(input: &str) -> Input {
    input
        .split("\n\n")
        .map(|lines| lines.lines().map(|line| line.parse().unwrap()).collect())
        .collect()
}

fn part_one(data: Input) -> i32 {
    data.iter().map(|cals| cals.iter().sum()).max().unwrap()
}

fn part_two(data: Input) -> i32 {
    let mut cals: std::collections::BinaryHeap<i32> =
        data.iter().map(|cals| cals.iter().sum()).collect();
    cals.pop().unwrap() + cals.pop().unwrap() + cals.pop().unwrap()
}

advent_of_code_macro::generate_tests!(
    day 1,
    parse,
    part_one,
    part_two,
    sample tests [24_000, 45_000],
    star tests [68_923, 200_044]
);
