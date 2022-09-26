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
    counts.iter().max_by_key(|c| c.1).unwrap().1 - counts.iter().min_by_key(|c| c.1).unwrap().1
}

fn part_two(input: String, transforms: Transforms) -> u128 {
    type Bag = HashMap<(char, char), u128>;
    let mut last_pair = ('a', 'b'); //hacky
    let mut bag = {
        let mut bag = Bag::new();
        let mut iter1 = input.chars().filter(|c| *c != '\n');
        let mut iter2 = iter1.clone();
        iter2.next();
        while let (Some(c1), Some(c2)) = (iter1.next(), iter2.next()) {
            last_pair = (c1, c2);
            bag.entry((c1, c2)).and_modify(|e| *e += 1).or_insert(1);
        }
        bag
    };
    for _ in 0..40 {
        let mut new_bag = Bag::new();
        for ((c1, c2), val) in bag {
            let transformation = transforms[&(c1, c2)];
            if last_pair == (c1, c2) {
                last_pair = (transformation, c2);
            }
            new_bag
                .entry((c1, transformation))
                .and_modify(|e| *e += val)
                .or_insert(val);
            new_bag
                .entry((transformation, c2))
                .and_modify(|e| *e += val)
                .or_insert(val);
        }
        bag = new_bag;
    }
    let counts = {
        type Counts = HashMap<char, u128>;
        let mut counts = Counts::new();
        for ((c1, _), val) in bag {
            counts.entry(c1).and_modify(|e| *e += val).or_insert(val);
        }
        counts.entry(last_pair.1).and_modify(|e| *e += 1);
        counts
    };

    counts.iter().max_by_key(|(_, val)| **val).unwrap().1
        - counts.iter().min_by_key(|(_, val)| **val).unwrap().1
}

#[cfg(test)]
mod tests {
    use super::*;

    fn parse_input(input: &str) -> (String, Transforms) {
        let (input, transform_list) = input.split_once("\n\n").expect("two sections");
        let mut transforms = Transforms::new();
        for line in transform_list.split('\n') {
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

    #[test]
    fn part_two_sample() {
        let (input, transforms) = parse_input(include_str!("../inputs/14_test.txt"));
        assert_eq!(part_two(input, transforms), 2188189693529);
    }

    #[test]
    fn part_two_test() {
        let (input, transforms) = parse_input(include_str!("../inputs/14.txt"));
        assert_eq!(part_two(input, transforms), 3243771149914);
    }
}
