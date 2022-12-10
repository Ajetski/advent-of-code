type Data = Vec<char>;
fn parse(input: &str) -> Data {
    input.chars().collect()
}
fn get_ans(data: Data, num: usize) -> usize {
    num + data
        .as_slice()
        .windows(num)
        .enumerate()
        .find(|(_idx, window)| {
            num == window
                .iter()
                .map(char::to_owned)
                .collect::<std::collections::HashSet<char>>()
                .len()
        })
        .unwrap()
        .0
}
fn part_one(data: Data) -> usize {
    get_ans(data, 4)
}
fn part_two(data: Data) -> usize {
    get_ans(data, 14)
}

advent_of_code_macro::generate_tests!(
    day 6,
    parse,
    part_one,
    part_two,
    sample tests [7, 19],
    star tests [1282, 3513]
);
