use std::collections::{BinaryHeap, HashSet};

type Coords = (usize, usize);

#[derive(Debug, Clone, Copy, Eq, PartialEq)]
struct Location {
    coords: Coords,
    steps: i32,
    cost: i32,
}
impl std::cmp::Ord for Location {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        other.cost.cmp(&self.cost)
    }
}
impl PartialOrd for Location {
    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
        Some(self.cmp(other))
    }
}

type Data = Vec<Vec<char>>;
type Output = u64;
fn parse(input: &str) -> Data {
    input.lines().map(|l| l.chars().collect()).collect()
}

fn distance(start: Coords, end: Coords) -> usize {
    start.0.abs_diff(end.0) + start.1.abs_diff(end.1)
}

fn find_shortest_path(data: &Data, start: Coords, end: Coords) -> Output {
    let bounds = (data.len() as i32, data[0].len() as i32);

    let mut visited: HashSet<Coords> = Default::default();
    let mut queue: BinaryHeap<Location> = BinaryHeap::from([Location {
        coords: start,
        steps: 0,
        cost: distance(start, end) as i32,
    }]);

    'outer: while let Some(loc) = queue.pop() {
        if visited.contains(&loc.coords) {
            continue 'outer;
        }
        visited.insert(loc.coords);
        let (x, y) = loc.coords;
        let (x, y) = (x as i32, y as i32);
        let c = data[x as usize][y as usize];
        if c == 'E' {
            return loc.steps as Output;
        }
        'inner: for (x2, y2) in [(x - 1, y), (x + 1, y), (x, y + 1), (x, y - 1)].iter() {
            let coords = (*x2 as usize, *y2 as usize);
            if !(0..bounds.0).contains(x2)
                || !(0..bounds.1).contains(y2)
                || visited.contains(&coords)
            {
                continue 'inner;
            }
            let c2 = data[*x2 as usize][*y2 as usize];
            if c == 'S' && c2 == 'a'
                || c == 'z' && c2 == 'E'
                || (c >= c2 && c2 != 'E')
                || c as i32 + 1 == c2 as i32
            {
                let steps = loc.steps + 1;
                queue.push(Location {
                    coords,
                    steps,
                    cost: distance(start, end) as i32 + steps,
                });
            }
        }
    }

    Output::max_value()
}

fn part_one(data: Data) -> Output {
    let (start, end) = {
        let mut start = None;
        let mut end = None;
        for (i, row) in data.iter().enumerate() {
            for (j, c) in row.iter().enumerate() {
                if c == &'S' {
                    start = Some((i, j));
                }
                if c == &'E' {
                    end = Some((i, j));
                }
            }
        }
        (start.unwrap(), end.unwrap())
    };
    find_shortest_path(&data, start, end)
}
fn part_two(data: Data) -> Output {
    let (starting_points, end) = {
        let mut starting_points = vec![];
        let mut end = None;
        for (i, row) in data.iter().enumerate() {
            for (j, c) in row.iter().enumerate() {
                if c == &'S' || c == &'a' {
                    starting_points.push((i, j));
                }
                if c == &'E' {
                    end = Some((i, j));
                }
            }
        }
        (starting_points, end.unwrap())
    };
    starting_points
        .iter()
        .map(|start| find_shortest_path(&data, *start, end))
        .min()
        .unwrap()
}

advent_of_code_macro::generate_tests!(
    day 12,
    parse,
    part_one,
    part_two,
    sample tests [31, 29],
    star tests [449, 443]
);
