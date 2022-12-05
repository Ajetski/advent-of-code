struct Instruction {
    count: i32,
    start: usize,
    end: usize,
}
type Data = (Vec<Vec<char>>, Vec<Instruction>);

fn parse(input: &str) -> Data {
    let (stacks, list) = input.split_once("\n\n").unwrap();
    let mut data = vec![vec![]; 10]; // chose an arbitrary buffer size
    let lines: Vec<&str> = stacks.lines().collect();
    for line in lines.into_iter().rev().skip(1) {
        let mut y = 0;
        let mut iter = line.chars();
        while let Some(_) = iter.next() {
            let c = iter.next().unwrap();
            if c != ' ' {
                data[y].push(c);
            }
            iter.next();
            iter.next();
            y += 1;
        }
    }
    let instructions = list
        .lines()
        .map(|item| {
            let mut parts = item.split(' ');
            parts.next();
            let count = parts.next().unwrap().parse().unwrap();
            parts.next();
            let start = parts.next().unwrap().parse().unwrap();
            parts.next();
            let end = parts.next().unwrap().parse().unwrap();

            Instruction { count, start, end }
        })
        .collect();
    (data, instructions)
}
fn part_one((mut stacks, instructions): Data) -> String {
    for i in instructions {
        for _ in 0..i.count {
            let item = stacks[i.start - 1].pop().unwrap();
            stacks[i.end - 1].push(item);
        }
    }
    stacks
        .iter()
        .filter(|s| !s.is_empty())
        .map(|s| s[s.len() - 1])
        .collect()
}
fn part_two((mut stacks, instructions): Data) -> String {
    for i in instructions {
        // dbg!(&stacks, &i);
        let end_idx = stacks[i.start - 1].len() - 1;
        let mut idx = 1 + end_idx - i.count as usize;
        while idx <= end_idx {
            let item = stacks[i.start - 1][idx];
            stacks[i.end - 1].push(item);
            idx += 1;
        }
        for _ in 0..i.count {
            stacks[i.start - 1].pop();
        }
    }
    stacks
        .iter()
        .filter(|s| !s.is_empty())
        .map(|s| s[s.len() - 1])
        .collect()
}

advent_of_code_macro::generate_tests!(
    day 5,
    parse,
    part_one,
    part_two,
    sample tests ["CMZ", "MCD"],
    star tests ["WHTLRMZRC", "GMPMLWNMG"]
);
