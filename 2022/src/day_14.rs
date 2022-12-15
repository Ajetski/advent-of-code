use State::*;
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
enum State {
    Air,
    Rock,
    Sand,
}
type Coord = (i32, i32);
type Data = Vec<Vec<Coord>>;
type Output = i32;
fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|line| {
            line.split(" -> ")
                .map(|coord| {
                    let (x, y) = coord.split_once(',').unwrap();
                    (x.parse().unwrap(), y.parse().unwrap())
                })
                .collect()
        })
        .collect()
}
fn part_one(data: Data) -> Output {
    let sim_height = 300;
    let sim_width = 1000;
    let mut sim = vec![vec![Air; sim_height]; sim_width];
    for line in data {
        for w in line.windows(2) {
            let (x1, y1) = w[0];
            let (x2, y2) = w[1];
            if x1 == x2 {
                for y in std::cmp::min(y1, y2)..=std::cmp::max(y1, y2) {
                    sim[x1 as usize][y as usize] = Rock;
                }
            } else if y1 == y2 {
                for x in std::cmp::min(x1, x2)..=std::cmp::max(x1, x2) {
                    sim[x as usize][y1 as usize] = Rock;
                }
            }
        }
    }
    let spawn = (500, 0);
    for i in 1.. {
        let (mut x, mut y) = spawn;
        loop {
            if y + 1 >= sim_height {
                return i - 1;
            }
            if sim[x][y + 1] == Air {
                y += 1;
            } else if sim[x - 1][y + 1] == Air {
                x -= 1;
                y += 1;
            } else if sim[x + 1][y + 1] == Air {
                x += 1;
                y += 1;
            } else {
                sim[x][y] = Sand;
                break;
            }
        }
    }
    unreachable!()
}
fn part_two(mut data: Data) -> Output {
    let sim_height = 300;
    let sim_width = 1000;
    let mut sim = vec![vec![Air; sim_height]; sim_width];
    let max_y = data.iter().flatten().map(|c| c.1).max().unwrap();
    data.push(vec![(0, max_y + 2), (sim_width as i32 - 1, max_y + 2)]);
    for line in data {
        for w in line.windows(2) {
            let (x1, y1) = w[0];
            let (x2, y2) = w[1];
            if x1 == x2 {
                for y in std::cmp::min(y1, y2)..=std::cmp::max(y1, y2) {
                    sim[x1 as usize][y as usize] = Rock;
                }
            } else if y1 == y2 {
                for x in std::cmp::min(x1, x2)..=std::cmp::max(x1, x2) {
                    sim[x as usize][y1 as usize] = Rock;
                }
            }
        }
    }
    let spawn = (500, 0);
    for i in 1.. {
        let (mut x, mut y) = spawn;
        loop {
            if sim[x][y + 1] == Air {
                y += 1;
            } else if sim[x - 1][y + 1] == Air {
                x -= 1;
                y += 1;
            } else if sim[x + 1][y + 1] == Air {
                x += 1;
                y += 1;
            } else {
                if (x, y) == spawn {
                    return i;
                }
                sim[x][y] = Sand;
                break;
            }
        }
    }
    unreachable!()
}

advent_of_code_macro::generate_tests!(
    day 14,
    parse,
    part_one,
    part_two,
    sample tests [24, 93],
    star tests [674, 24_958]
);
