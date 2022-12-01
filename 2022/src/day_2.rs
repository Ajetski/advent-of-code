advent_of_code_macro::solve_problem!(
    day 2,
    Input i32,
    parse |_input: &str| {
        1 + 1
    },
    part one |data: Input| {
        data + 5
    },
    part two |data: Input| {
        data + 10
    },
    sample tests [7, 12],
    star tests [7, 12]
);
