use std::collections::HashSet;
use Direction::*;

#[derive(Debug)]
enum Direction {
    L,
    R,
    U,
    D,
}

#[derive(Debug)]
struct Instruction {
    direction: Direction,
    steps: i32,
}

#[derive(Debug, PartialEq, Eq, Hash, Clone, Copy)]
struct Location {
    x: i32,
    y: i32,
}

type Data = Vec<Instruction>;
type Output = usize;
fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|line| {
            let (direction, steps) = line.split_once(' ').unwrap();
            Instruction {
                direction: match direction {
                    "L" => L,
                    "R" => R,
                    "D" => D,
                    "U" => U,
                    _ => panic!("expected L, R, D, or U. got {}", direction),
                },
                steps: steps.parse().unwrap(),
            }
        })
        .collect()
}
fn part_one(data: Data) -> Output {
    let mut locations = HashSet::<Location>::new();
    let mut head = Location { x: 0, y: 0 };
    let mut tail = head;
    locations.insert(tail);
    for instr in data {
        for _ in 0..instr.steps {
            // println!("{:?}", tail);
            match instr.direction {
                L => {
                    head.x -= 1;
                    if tail.x == head.x + 2 {
                        tail.x = head.x + 1;
                        tail.y = head.y;
                    }
                }
                R => {
                    head.x += 1;
                    if tail.x == head.x - 2 {
                        tail.x = head.x - 1;
                        tail.y = head.y;
                    }
                }
                U => {
                    head.y += 1;
                    if tail.y + 2 == head.y {
                        tail.x = head.x;
                        tail.y = head.y - 1;
                    }
                }
                D => {
                    head.y -= 1;
                    if tail.y == head.y + 2 {
                        tail.x = head.x;
                        tail.y = head.y + 1;
                    }
                }
            }
            locations.insert(tail);
        }
    }
    locations.len()
}
fn part_two(data: Data) -> Output {
    let mut locations = HashSet::<Location>::new();
    let mut rope = [Location { x: 0, y: 0 }; 10];
    locations.insert(rope[9]);
    for instr in data {
        for _ in 0..instr.steps {
            match instr.direction {
                L => {
                    rope[0].x -= 1;
                }
                R => {
                    rope[0].x += 1;
                }
                U => {
                    rope[0].y += 1;
                }
                D => {
                    rope[0].y -= 1;
                }
            }
            for i in 1..rope.len() {
                if rope[i].x.abs_diff(rope[i - 1].x) == 2 && rope[i].y.abs_diff(rope[i - 1].y) == 2
                {
                    rope[i] = Location {
                        x: rope[i - 1].x + if rope[i - 1].x > rope[i].x { -1 } else { 1 },
                        y: rope[i - 1].y + if rope[i - 1].y > rope[i].y { -1 } else { 1 },
                    };
                } else if rope[i].x >= rope[i - 1].x + 2 {
                    rope[i].x = rope[i - 1].x + 1;
                    rope[i].y = rope[i - 1].y;
                } else if rope[i].y >= rope[i - 1].y + 2 {
                    rope[i].x = rope[i - 1].x;
                    rope[i].y = rope[i - 1].y + 1;
                } else if rope[i].y <= rope[i - 1].y - 2 {
                    rope[i].x = rope[i - 1].x;
                    rope[i].y = rope[i - 1].y - 1;
                } else if rope[i].x <= rope[i - 1].x - 2 {
                    rope[i].x = rope[i - 1].x - 1;
                    rope[i].y = rope[i - 1].y;
                }
            }
            locations.insert(rope[rope.len() - 1]);
        }
    }
    locations.len()
}

advent_of_code_macro::generate_tests!(
    day 9,
    parse,
    part_one,
    part_two,
    sample tests [88, 36],
    star tests [6337, 2455]
);
