use Direction::*;
use Instruction::*;

#[derive(Debug)]
struct State {
    row: i32,
    col: i32,
    dir: Direction,
}
impl State {
    fn do_instr(
        &mut self,
        row_bounds: &Vec<CondensedFileOrRank>,
        col_bounds: &Vec<CondensedFileOrRank>,
        instr: Instruction,
    ) {
        fn go(
            loc: &mut i32,
            bound_idx: i32,
            bounds: &Vec<CondensedFileOrRank>,
            dist: i32,
            walk_positive: bool,
        ) {
            let CondensedFileOrRank {
                ref walls,
                start_idx,
                end_idx,
            } = bounds[bound_idx as usize];
            if walk_positive {
                *loc = match walls.iter().skip_while(|wall| **wall <= *loc).next() {
                    Some(x) => std::cmp::min(*loc + dist, *x - 1),
                    None => {
                        if walls.is_empty() {
                            let len = end_idx - start_idx + 1;
                            start_idx + ((*loc - start_idx + dist).rem_euclid(len))
                        } else {
                            if *walls.first().unwrap() != start_idx && *loc + dist > end_idx {
                                std::cmp::min(
                                    walls.first().unwrap() - 1,
                                    start_idx + (*loc + dist - end_idx - 1),
                                )
                            } else {
                                std::cmp::min(*loc + dist, end_idx)
                            }
                        }
                    }
                };
            } else {
                *loc = match walls.iter().rev().skip_while(|wall| **wall >= *loc).next() {
                    Some(x) => std::cmp::max(*loc - dist, *x + 1),
                    None => {
                        if walls.is_empty() {
                            let len = end_idx - start_idx + 1;
                            start_idx + (*loc - start_idx - dist).rem_euclid(len)
                        } else {
                            if *walls.last().unwrap() != end_idx && *loc - dist < start_idx {
                                std::cmp::max(
                                    *walls.last().unwrap() + 1,
                                    end_idx - (dist - (*loc - start_idx + 1)),
                                )
                            } else {
                                std::cmp::max(*loc - dist, start_idx)
                            }
                        }
                    }
                };
            }
        }
        match instr {
            Clockwise => {
                self.dir = match self.dir {
                    Left => Up,
                    Right => Down,
                    Up => Right,
                    Down => Left,
                }
            }
            Counterclockwise => {
                self.dir = match self.dir {
                    Left => Down,
                    Right => Up,
                    Up => Left,
                    Down => Right,
                }
            }
            Go(n) => match self.dir {
                Left => {
                    go(&mut self.col, self.row, &row_bounds, n, false);
                }
                Right => {
                    go(&mut self.col, self.row, &row_bounds, n, true);
                }
                Up => {
                    go(&mut self.row, self.col, &col_bounds, n, false);
                }
                Down => {
                    go(&mut self.row, self.col, &col_bounds, n, true);
                }
            },
        }
    }
}

#[derive(Clone, Copy, Debug)]
enum Direction {
    Right = 0,
    Down = 1,
    Left = 2,
    Up = 3,
}

#[derive(Debug)]
struct CondensedFileOrRank {
    walls: Vec<i32>,
    start_idx: i32,
    end_idx: i32,
}

#[derive(Clone, Copy, Debug)]
enum Instruction {
    Clockwise,
    Counterclockwise,
    Go(i32),
}
type Data = (
    Vec<CondensedFileOrRank>,
    Vec<CondensedFileOrRank>,
    Vec<Instruction>,
);
type Output = i32;
fn parse(input: &str) -> Data {
    let (map, instr) = input.split_once("\n\n").unwrap();
    let instr: String = instr.chars().filter(|c| *c != '\n').collect();
    let mut instructions = vec![];
    let mut i = 0;
    loop {
        let start = instr.chars().skip(i);
        let c = start.clone().next();
        instructions.push(if let Some(c) = c {
            if c.is_digit(10) {
                let s: String = start.take_while(|c| *c != 'L' && *c != 'R').collect();
                i += s.len();
                Go(s.parse().map_err(|_| s).unwrap())
            } else {
                i += 1;
                match c {
                    'L' => Counterclockwise,
                    'R' => Clockwise,
                    c => panic!("expected L or R, got {:?}", c),
                }
            }
        } else {
            break;
        });
    }
    let map: Vec<Vec<char>> = map.lines().map(|l| l.chars().collect()).collect();
    let row_bounds: Vec<CondensedFileOrRank> = map
        .iter()
        .map(|row| {
            let row = row.iter().enumerate().filter(|c| *c.1 != ' ');
            CondensedFileOrRank {
                walls: row
                    .clone()
                    .filter(|(_, c)| **c == '#')
                    .map(|(idx, _)| idx as i32)
                    .collect(),
                start_idx: row
                    .clone()
                    .min_by(|(a, _), (b, _)| a.cmp(b))
                    .map(|(idx, _)| idx as i32)
                    .unwrap(),
                end_idx: row
                    .clone()
                    .max_by(|(a, _), (b, _)| a.cmp(b))
                    .map(|(idx, _)| idx as i32)
                    .unwrap(),
            }
        })
        .collect();
    let col_bounds: Vec<CondensedFileOrRank> = (0..)
        .take_while(|col_idx| map.iter().any(|row| row.len() > *col_idx as usize))
        .map(|idx| {
            let col: Vec<(usize, char)> = map
                .iter()
                .map(|row| *row.get(idx).or(Some(&' ')).unwrap())
                .enumerate()
                .filter(|(_, c)| *c != ' ')
                .collect();
            CondensedFileOrRank {
                walls: col
                    .iter()
                    .filter(|(_, c)| *c == '#')
                    .map(|(idx, _)| *idx as i32)
                    .collect(),
                start_idx: col
                    .iter()
                    .min_by(|(a, _), (b, _)| a.cmp(b))
                    .map(|(idx, _)| *idx as i32)
                    .unwrap(),
                end_idx: col
                    .iter()
                    .max_by(|(a, _), (b, _)| a.cmp(b))
                    .map(|(idx, _)| *idx as i32)
                    .unwrap(),
            }
        })
        .collect();

    (row_bounds, col_bounds, instructions)
}
fn part_one((row_bounds, col_bounds, instructions): Data) -> Output {
    let mut state = State {
        row: 0,
        col: row_bounds[0].start_idx,
        dir: Right,
    };
    for instr in instructions {
        state.do_instr(&row_bounds, &col_bounds, instr);
    }
    (state.row + 1) * 1000 + (state.col + 1) * 4 + state.dir as i32
}
fn part_two(data: Data) -> Output {
    todo!()
}

advent_of_code_macro::generate_tests!(
    day 22,
    parse,
    part_one,
    part_two,
    sample tests [6_032, 0],
    star tests [149_138, 0]
);
