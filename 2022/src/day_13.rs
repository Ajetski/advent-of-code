use json::{array, JsonValue};
use std::cmp::Ordering::{self, *};

fn compare(a: &JsonValue, b: &JsonValue) -> Ordering {
    if !(a.is_array() && b.is_array()) {
        panic!("expected two arrays. got {} and {}", a, b);
    }
    for i in 0.. {
        if i >= a.len() && i >= b.len() {
            return Equal;
        } else if i >= a.len() {
            return Less;
        } else if i >= b.len() {
            return Greater;
        }
        let mut left = a[i].clone();
        let mut right = b[i].clone();
        if left.is_array() {
            if let Some(num) = right.as_i32() {
                right = array![num];
            }
        } else if right.is_array() {
            if let Some(num) = left.as_i32() {
                left = array![num];
            }
        }
        if left.is_number() && right.is_number() {
            let l = left.as_i32().unwrap();
            let r = right.as_i32().unwrap();
            let res = l.cmp(&r);
            if res != Equal {
                return res;
            }
        } else {
            let res = compare(&left, &right);
            if res != Equal {
                return res;
            }
        }
    }
    unreachable!()
}

type Data = Vec<(JsonValue, JsonValue)>;
type Output = i32;
fn parse(input: &str) -> Data {
    input
        .split("\n\n")
        .map(|chunk| {
            let mut iter = chunk.lines();
            let left_line = iter.next().unwrap();
            let left = json::parse(left_line).unwrap();

            let right_line = iter.next().unwrap();
            let right = json::parse(right_line).unwrap();

            (left, right)
        })
        .collect()
}
fn part_one(data: Data) -> Output {
    let ans = data
        .into_iter()
        .enumerate()
        .filter(|(_, (left, right))| compare(left, right).is_le())
        .map(|(i, _)| (i + 1) as Output);
    ans.sum()
}
fn part_two(data: Data) -> Output {
    // ignore chucking; flatten data into vec of lines
    let mut data: Vec<JsonValue> = data
        .into_iter()
        .flat_map(|(left, right)| vec![left, right])
        .collect();

    // add signals
    for thing in ["[[2]]", "[[6]]"] {
        data.push(json::parse(thing).unwrap());
    }

    data.sort_by(compare);

    // find 1-based indexes of the signals and multiply them
    data.iter()
        .enumerate()
        .find(|(_, v)| json::parse("[[2]]").unwrap().eq(*v))
        .map(|(i, _)| i as i32 + 1)
        .unwrap()
        * data
            .iter()
            .enumerate()
            .find(|(_, v)| json::parse("[[6]]").unwrap().eq(*v))
            .map(|(i, _)| i as i32 + 1)
            .unwrap()
}

advent_of_code_macro::generate_tests!(
    day 13,
    parse,
    part_one,
    part_two,
    sample tests [13, 140],
    star tests [4734, 0]
);
