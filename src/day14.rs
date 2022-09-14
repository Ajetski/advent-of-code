use std::collections::HashMap;

type Transforms = HashMap<(char, char), char>;

fn part_one(input: String, transforms: Transforms) -> i32 {
    let mut input: Vec<char> = input.chars().collect();
    for _ in 1..=10 {
        let pairs = input.windows(2);
        let mut next = vec![input[0]];
        for pair in pairs {
            let key = (pair[0], pair[1]);
            if let Some(val) = transforms.get(&key) {
                next.push(*val);
            }
            next.push(pair[1]);
        }
        input = next;
    }
    let mut counts = HashMap::<char, i32>::new();
    for char in input {
        if char != '\n' {
            counts.insert(
                char,
                match counts.get(&char) {
                    Some(val) => *val + 1,
                    _ => 1,
                },
            );
        }
    }
    counts.iter().fold(0, |max, (_, count)| {
        let count = *count;
        if count > max {
            count
        } else {
            max
        }
    }) - counts.iter().fold(i32::max_value(), |min, (_, count)| {
        let count = *count;
        if count < min {
            count
        } else {
            min
        }
    })
}

fn part_two(input: String, transforms: Transforms) -> u128 {
    todo!()
}

#[cfg(test)]
mod tests {
    use super::*;

    fn parse_input(input: &str) -> (String, Transforms) {
        let (input, transform_list) = input.split_once("\n\n").expect("two sections");
        let mut transforms = Transforms::new();
        for line in transform_list.split("\n") {
            let (input, output) = line.split_once(" -> ").expect("transform");
            let mut chars = input.chars();
            transforms.insert(
                (chars.next().unwrap(), chars.next().unwrap()),
                output.chars().next().unwrap(),
            );
        }
        (input.to_owned(), transforms)
    }

    #[test]
    fn part_one_test_sample() {
        let (input, transforms) = parse_input(include_str!("../inputs/14_test.txt"));
        assert_eq!(part_one(input, transforms), 1588);
    }

    #[test]
    fn part_one_test() {
        let (input, transforms) = parse_input(include_str!("../inputs/14.txt"));
        assert_eq!(part_one(input, transforms), 2112);
    }
}
