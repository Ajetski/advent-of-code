fn get_priority(c: char) -> i32 {
    match c {
        'a'..='z' => c as i32 - 'a' as i32 + 1,
        'A'..='Z' => c as i32 - 'A' as i32 + 27,
        _ => panic!("expected letter"),
    }
}

advent_of_code_macro::solve_problem!(
    day 3,
    Input Vec<String>,
    parse |input: &str| {
        input.lines().map(str::to_owned).collect()
    },
    part one |data: Input| {
        data.iter().map(|s| {
            use std::collections::HashSet;
            let (left, right) = s.split_at(s.len() / 2);
            let right_set: HashSet<char> = right.chars().collect();
            get_priority(left.chars().find(|c| right_set.contains(c)).unwrap())
        }).sum()
    },
    part two |data: Input| {
        data.as_slice().chunks(3).map(|c| {
            use std::collections::HashSet;
            let second_set: HashSet<char> = c[1].chars().collect();
            let third_set: HashSet<char> = c[2].chars().collect();
            get_priority(c[0].chars().find(|c| second_set.contains(c) && third_set.contains(c)).unwrap())
        }).sum()
    },
    sample tests [157, 70],
    star tests [7908, 2838]
);
