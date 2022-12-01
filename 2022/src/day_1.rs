advent_of_code_macro::solve_problem!(
    day 1,
    Input Vec<Vec<i32>>,
    parse |input: &str| {
        let lines = input.split('\n');
        let mut output = vec![];
        let mut curr_elf = vec![];
        for line in lines {
            if line.is_empty() {
                output.push(curr_elf);
                curr_elf = vec![];
            } else {
                curr_elf.push(line.parse().unwrap())
            }
        }
        output
    },
    part one |data: Input| {
        data.iter().map(|cals| cals.iter().sum()).max().unwrap()
    },
    part two |data: Input| {
        use std::mem::swap;
        let cals = data.iter().map(|cals| cals.iter().sum()).fold(
            (-1, -1, -1),
            |(mut first, mut second, mut third), mut curr: i32| {
                if curr > first {
                    swap(&mut first, &mut curr);
                }
                if curr > second {
                    swap(&mut second, &mut curr);
                }
                if curr > third {
                    swap(&mut third, &mut curr);
                }

                (first, second, third)
            },
        );
        cals.0 + cals.1 + cals.2
    },
    sample tests [24_000, 45_000],
    star tests [68_923, 200_044]
);
