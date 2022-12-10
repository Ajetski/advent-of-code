type Data = Vec<Vec<i32>>;

fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|line| {
            let (a, rest) = line.split_once('-').unwrap();
            let (b, rest) = rest.split_once(',').unwrap();
            let (c, d) = rest.split_once('-').unwrap();
            [a, b, c, d]
                .into_iter()
                .map(str::parse)
                .map(Result::unwrap)
                .collect()
        })
        .collect()
}
fn part_one(data: Data) -> i32 {
    data.into_iter().fold(0, |acc, curr| {
        if (curr[0] <= curr[2] && curr[1] >= curr[3]) || (curr[2] <= curr[0] && curr[3] >= curr[1])
        {
            acc + 1
        } else {
            acc
        }
    })
}
fn part_two(data: Data) -> i32 {
    data.into_iter().fold(0, |acc, curr| {
        if (curr[0] >= curr[2] && curr[0] <= curr[3])
            || (curr[1] >= curr[2] && curr[1] <= curr[3])
            || (curr[2] >= curr[0] && curr[2] <= curr[1])
            || (curr[3] >= curr[0] && curr[3] <= curr[1])
        {
            acc + 1
        } else {
            acc
        }
    })
}

advent_of_code_macro::generate_tests!(
    day 4,
    parse,
    part_one,
    part_two,
    sample tests [2, 4],
    star tests [450, 837]
);
