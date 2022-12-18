use std::collections::HashMap;
use State::*;

type Point = (i64, i64);
#[derive(Debug, PartialEq, Eq, Hash)]
enum State {
    Sensor,
    Beacon,
    Empty,
}
type Data = Vec<(Point, Point)>;
type Output = i64;
fn parse(input: &str) -> Data {
    fn parse_point(input: &str) -> Point {
        let (_, rest) = input.split_once("x=").unwrap();
        let (x, rest) = rest.split_once(", ").unwrap();
        let (_, y) = rest.split_once("y=").unwrap();
        (x.parse().unwrap(), y.parse().unwrap())
    }

    input
        .lines()
        .map(str::to_owned)
        .map(|l| {
            let (s, b) = l.split_once(':').unwrap();
            (parse_point(s), parse_point(b))
        })
        .collect()
}
fn manhattan_distance(a: Point, b: Point) -> i64 {
    (a.0.abs_diff(b.0) + a.1.abs_diff(b.1)) as i64
}
fn is_star_input(data: &Data) -> bool {
    // hacky trick to distinguish sample vs start input
    data[0].0 .0 == 98_246
}
fn part_one(data: Data) -> Output {
    let row = if is_star_input(&data) { 2_000_000 } else { 10 };
    let mut map: HashMap<Point, State> = data
        .iter()
        .flat_map(|(s, b)| [(*s, Sensor), (*b, Beacon)])
        .collect();

    for (s, b) in data {
        let dist = manhattan_distance(s, b);
        let x_offset = dist - s.1.abs_diff(row) as i64;
        for x in s.0 - x_offset..=s.0 + x_offset {
            map.entry((x, row)).or_insert(Empty);
        }
    }
    map.iter()
        .filter(|(p, state)| p.1 == row && Empty == **state)
        .count() as Output
}
fn part_two(data: Data) -> Output {
    let max_bounds = if is_star_input(&data) { 4_000_000 } else { 20 };

    let data: Vec<_> = data
        .into_iter()
        .map(|(s, b)| (s, manhattan_distance(s, b)))
        .collect();
    for y in 0..max_bounds {
        let mut x = 0;
        while x < max_bounds {
            let next_idx = data.iter().find_map(|(s, dist)| {
                let curr_dist = manhattan_distance(*s, (x, y));
                if curr_dist > *dist {
                    return None;
                }
                let x_offset = dist - s.1.abs_diff(y) as i64;
                if (s.0 - x_offset..=s.0 + x_offset).contains(&x) {
                    Some(s.0 + x_offset + 1)
                } else {
                    None
                }
            });
            if next_idx.is_none() {
                return x * 4000000 + y;
            }
            x = next_idx.unwrap();
        }
    }
    unreachable!()
}

advent_of_code_macro::generate_tests!(
    day 15,
    parse,
    part_one,
    part_two,
    sample tests [26, 56_000_011],
    star tests [5_125_700, 11_379_394_658_764]
);
