#[derive(Debug, Clone, Copy)]
enum Param {
    Old,
    Value(u64),
}

#[derive(Debug, Clone, Copy)]
enum Op {
    Add,
    Mulitply,
}

#[derive(Debug, Clone, Copy)]
struct Operation {
    left: Param,
    op: Op,
    right: Param,
}

#[derive(Debug, Clone)]
struct Monkey {
    id: usize,
    items: Vec<u64>,
    operation: Operation,
    test: u64,
    test_failed_go_to: usize,
    test_passed_go_to: usize,
}

type Data = Vec<Monkey>;
type Output = u64;
fn parse(input: &str) -> Data {
    input
        .split("\n\n")
        .map(|lines| {
            let mut line_iter = lines.lines();

            let id = {
                let title_line = line_iter.next().unwrap();
                let (_prefix, id_block) = title_line.split_once(' ').unwrap();
                let (id, _rest) = id_block.split_once(':').unwrap();
                id.parse().unwrap()
            };

            let items = {
                let items_line = line_iter.next().unwrap();
                let (_prefix, items) = items_line.split_once(": ").unwrap();
                items.split(", ").map(|num| num.parse().unwrap()).collect()
            };

            let operation = {
                let operation_line = line_iter.next().unwrap();
                let (_prefix, rest) = operation_line.split_once("= ").unwrap();
                let (left_str, rest) = rest.split_once(' ').unwrap();
                let left = match left_str {
                    "old" => Param::Old,
                    s => Param::Value(s.parse().unwrap()),
                };

                let (op_str, right_str) = rest.split_once(' ').unwrap();
                let op_char = op_str.chars().next().unwrap();
                let op = match op_char {
                    '+' => Op::Add,
                    '*' => Op::Mulitply,
                    _ => panic!("expected + or *; got {}", op_str),
                };

                let right = match right_str {
                    "old" => Param::Old,
                    s => Param::Value(s.parse().unwrap()),
                };
                Operation { left, op, right }
            };

            let test = {
                let test_line = line_iter.next().unwrap();
                let (_prefix, num) = test_line.split_once("by ").unwrap();
                num.parse().unwrap()
            };

            let test_passed_go_to = {
                let passed_line = line_iter.next().unwrap();
                let (_prefix, num) = passed_line.split_once("monkey ").unwrap();
                num.parse().unwrap()
            };

            let test_failed_go_to = {
                let failed_line = line_iter.next().unwrap();
                let (_prefix, num) = failed_line.split_once("monkey ").unwrap();
                num.parse().unwrap()
            };

            Monkey {
                id,
                items,
                operation,
                test,
                test_failed_go_to,
                test_passed_go_to,
            }
        })
        .collect()
}
fn part_one(mut data: Data) -> Output {
    let mut counts = vec![0; data.len()];

    for _ in 1..=20 {
        for i in 0..data.len() {
            for j in 0..data[i].items.len() {
                let left = match data[i].operation.left {
                    Param::Value(v) => v,
                    Param::Old => data[i].items[j],
                };
                let right = match data[i].operation.right {
                    Param::Value(v) => v,
                    Param::Old => data[i].items[j],
                };
                let new_value = match data[i].operation.op {
                    Op::Add => left + right,
                    Op::Mulitply => left * right,
                } / 3;
                if new_value % data[i].test == 0 {
                    let idx = data[i].test_passed_go_to;
                    data[idx].items.push(new_value);
                } else {
                    let idx = data[i].test_failed_go_to;
                    data[idx].items.push(new_value);
                }
            }
            counts[i] += data[i].items.len();
            data[i].items.clear();
        }
    }

    counts.sort();
    counts.reverse();

    counts[0] as u64 * counts[1] as u64
}
fn part_two(mut data: Data) -> Output {
    let mut counts = vec![0; data.len()];

    let test_gcd = data.iter().fold(1, |acc, curr| acc * curr.test);

    for round in 1..=10000 {
        for i in 0..data.len() {
            for j in 0..data[i].items.len() {
                let left = match data[i].operation.left {
                    Param::Value(v) => v,
                    Param::Old => data[i].items[j],
                };
                let right = match data[i].operation.right {
                    Param::Value(v) => v,
                    Param::Old => data[i].items[j],
                };
                let new_value = match data[i].operation.op {
                    Op::Add => left + right,
                    Op::Mulitply => left * right,
                };
                if new_value % data[i].test == 0 {
                    let idx = data[i].test_passed_go_to;
                    data[idx].items.push(new_value % test_gcd);
                } else {
                    let idx = data[i].test_failed_go_to;
                    data[idx].items.push(new_value % test_gcd);
                }
            }
            counts[i] += data[i].items.len();
            data[i].items.clear();
        }
        match round {
            1 | 20 | 1000 => {
                dbg!(&counts, round);
            }
            _ => {}
        }
    }

    counts.sort();
    counts.reverse();

    counts[0] as u64 * counts[1] as u64
}

advent_of_code_macro::generate_tests!(
    day 11,
    parse,
    part_one,
    part_two,
    sample tests [10_605, 2_713_310_158],
    star tests [90_294, 18_170_818_354]
);
