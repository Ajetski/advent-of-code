use std::collections::HashSet;
use Side::*;

type Point = (i32, i32, i32);

#[derive(Debug, Clone, Copy, Hash, Eq, PartialEq)]
enum Side {
    /// positive z side
    Top,

    /// negative z side
    Bottom,

    /// negative x side
    Left,

    /// positive x side
    Right,

    /// positive y side
    Front,

    /// negative y side
    Back,
}
impl Side {
    fn inverse(&self) -> Self {
        match *self {
            Top => Bottom,
            Bottom => Top,
            Left => Right,
            Right => Left,
            Front => Back,
            Back => Front,
        }
    }
}

type Data = HashSet<Point>;

type Output = i32;

fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|l| l.split(',').map(str::parse).map(Result::unwrap))
        .map(|mut iter| {
            (
                iter.next().unwrap(),
                iter.next().unwrap(),
                iter.next().unwrap(),
            )
        })
        .collect()
}

fn part_one(data: Data) -> Output {
    data.iter()
        .map(|(x, y, z)| {
            let offsets = [
                (0, 0, 1),
                (0, 1, 0),
                (1, 0, 0),
                (0, 0, -1),
                (0, -1, 0),
                (-1, 0, 0),
            ];
            offsets
                .iter()
                .filter(|(x_off, y_off, z_off)| !data.contains(&(x + x_off, y + y_off, z + z_off)))
                .count() as Output
        })
        .sum()
}

fn part_two(data: Data) -> Output {
    let mut queue = vec![(
        *data
            .iter()
            .max_by(|(x1, _, _), (x2, _, _)| x1.cmp(x2))
            .unwrap(),
        Top,
    )];
    let mut visited: HashSet<(Point, Side)> = queue.clone().into_iter().collect();
    for i in 0.. {
        if i >= queue.len() {
            return visited.len() as Output;
        }
        let ((curr_x, curr_y, curr_z), curr_side) = queue[i];
        let offsets = [(1, 0), (0, 1), (-1, 0), (0, -1)];
        let positions = offsets.iter().map(|(a, b)| match curr_side {
            Top | Bottom => (curr_x + a, curr_y + b, curr_z),
            Left | Right => (curr_x, curr_y + a, curr_z + b),
            Front | Back => (curr_x + a, curr_y, curr_z + b),
        });
        for (new_x, new_y, new_z) in positions {
            let res = match curr_side {
                Top | Bottom => {
                    let side = if curr_x > new_x {
                        Right
                    } else if curr_x < new_x {
                        Left
                    } else if curr_y > new_y {
                        Front
                    } else {
                        Back
                    };
                    if curr_side == Top {
                        if data.contains(&(new_x, new_y, new_z + 1)) {
                            ((new_x, new_y, new_z + 1), side)
                        } else if data.contains(&(new_x, new_y, new_z)) {
                            ((new_x, new_y, new_z), Top)
                        } else {
                            ((curr_x, curr_y, curr_z), side.inverse())
                        }
                    } else {
                        if data.contains(&(new_x, new_y, new_z - 1)) {
                            ((new_x, new_y, new_z - 1), side)
                        } else if data.contains(&(new_x, new_y, new_z)) {
                            ((new_x, new_y, new_z), Bottom)
                        } else {
                            ((curr_x, curr_y, curr_z), side.inverse())
                        }
                    }
                }
                Left | Right => {
                    let side = if curr_z > new_z {
                        Top
                    } else if curr_z < new_z {
                        Bottom
                    } else if curr_y > new_y {
                        Front
                    } else {
                        Back
                    };
                    if curr_side == Left {
                        if data.contains(&(new_x - 1, new_y, new_z)) {
                            ((new_x - 1, new_y, new_z), side)
                        } else if data.contains(&(new_x, new_y, new_z)) {
                            ((new_x, new_y, new_z), Left)
                        } else {
                            ((curr_x, curr_y, curr_z), side.inverse())
                        }
                    } else {
                        if data.contains(&(new_x + 1, new_y, new_z)) {
                            ((new_x + 1, new_y, new_z), side)
                        } else if data.contains(&(new_x, new_y, new_z)) {
                            ((new_x, new_y, new_z), Right)
                        } else {
                            ((curr_x, curr_y, curr_z), side.inverse())
                        }
                    }
                }
                Front | Back => {
                    let side = if curr_z > new_z {
                        Top
                    } else if curr_z < new_z {
                        Bottom
                    } else if curr_x > new_x {
                        Right
                    } else {
                        Left
                    };
                    if curr_side == Front {
                        if data.contains(&(new_x, new_y + 1, new_z)) {
                            ((new_x, new_y + 1, new_z), side)
                        } else if data.contains(&(new_x, new_y, new_z)) {
                            ((new_x, new_y, new_z), Front)
                        } else {
                            ((curr_x, curr_y, curr_z), side.inverse())
                        }
                    } else {
                        if data.contains(&(new_x + 1, new_y, new_z)) {
                            ((new_x + 1, new_y, new_z), side)
                        } else if data.contains(&(new_x, new_y, new_z)) {
                            ((new_x, new_y, new_z), Back)
                        } else {
                            ((curr_x, curr_y, curr_z), side.inverse())
                        }
                    }
                }
            };
            if !visited.contains(&res) {
                visited.insert(res);
                println!("{:?}", res);
                queue.push(res);
            }
        }
    }

    unreachable!()
}

advent_of_code_macro::generate_tests!(
    day 18,
    parse,
    part_one,
    part_two,
    sample tests [64, 58],
    star tests [4300, 0]
);
