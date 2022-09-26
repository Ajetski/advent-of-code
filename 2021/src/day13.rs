use std::collections::HashSet;

type Point = (i32, i32);
#[derive(Debug)]
enum Fold {
    X(i32),
    Y(i32),
}

fn part_one(points: Vec<Point>, folds: Vec<Fold>) -> i32 {
    match folds.first().expect("first fold") {
        Fold::X(coord) => points
            .into_iter()
            .map(|(x, y): Point| (x.abs_diff(*coord) as i32, y))
            .collect::<HashSet<_>>(),
        Fold::Y(coord) => points
            .into_iter()
            .map(|(x, y): Point| (x, y.abs_diff(*coord) as i32))
            .collect::<HashSet<_>>(),
    }
    .len() as i32
}

fn part_two(points: Vec<Point>, folds: Vec<Fold>) {
    let mut points = points.into_iter().collect::<HashSet<_>>();
    for fold in folds {
        points = match fold {
            Fold::X(coord) => points
                .into_iter()
                .map(|(x, y): Point| (coord - x.abs_diff(coord) as i32, y))
                .collect::<HashSet<_>>(),
            Fold::Y(coord) => points
                .into_iter()
                .map(|(x, y): Point| (x, coord - y.abs_diff(coord) as i32))
                .collect::<HashSet<_>>(),
        };
    }
    let max_x = points
        .iter()
        .fold(0, |acc, (x, _)| if *x > acc { *x } else { acc });
    let max_y = points
        .iter()
        .fold(0, |acc, (_, y)| if *y > acc { *y } else { acc });
    let lines: Vec<Vec<char>> = (0..=max_y)
        .map(|y| {
            (0..=max_x)
                .map(|x| if points.contains(&(x, y)) { '#' } else { ' ' })
                .collect()
        })
        .collect();
    for line in lines {
        let line = line.into_iter().collect::<String>();
        println!("{line}");
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn parse_inputs(input: &str) -> (Vec<Point>, Vec<Fold>) {
        let mut sections = input.split("\n\n");
        (
            sections
                .next()
                .expect("coords section")
                .lines()
                .map(|line| {
                    let mut coords = line
                        .split(',')
                        .map(|num_str| num_str.parse().expect("coord number"));
                    (
                        coords.next().expect("coord x value"),
                        coords.next().expect("coord y value"),
                    )
                })
                .collect(),
            sections
                .next()
                .expect("fold instructions section")
                .lines()
                .map(|line| {
                    let (direction, num) =
                        line.split(' ').nth(2).expect("fold equation").split_at(2);
                    let num = num.parse().expect("coordinate to fold at");
                    if direction == "x=" {
                        Fold::X(num)
                    } else {
                        Fold::Y(num)
                    }
                })
                .collect(),
        )
    }

    #[test]
    fn part_one_sample_test() {
        let input = include_str!("../inputs/13_test.txt");
        let (points, folds) = parse_inputs(input);
        assert_eq!(part_one(points, folds), 17);
    }

    #[test]
    fn part_one_test() {
        let input = include_str!("../inputs/13.txt");
        let (points, folds) = parse_inputs(input);
        assert_eq!(part_one(points, folds), 653);
    }

    #[test]
    fn part_two_test() {
        let input = include_str!("../inputs/13.txt");
        let (points, folds) = parse_inputs(input);
        part_two(points, folds);
    }
}
