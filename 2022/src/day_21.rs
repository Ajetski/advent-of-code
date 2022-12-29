use std::collections::HashMap;
use std::str::FromStr;
use Monkey::*;
use Operation::*;

#[derive(Clone, Copy, Debug)]
enum Operation {
    Add,
    Subtract,
    Multiply,
    Divide,
}
#[derive(Clone, Debug)]
enum Monkey {
    Number(String, i64),
    Pending(String, String, Operation, String),
}
impl FromStr for Monkey {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let (name, rest) = s.split_once(": ").unwrap();
        if let Ok(num) = rest.parse::<i64>() {
            return Ok(Number(name.into(), num));
        }
        let (name_left, rest) = rest.split_once(' ').unwrap();
        let (operation, name_right) = rest.split_once(' ').unwrap();
        return Ok(Pending(
            name.into(),
            name_left.into(),
            match operation {
                "+" => Add,
                "-" => Subtract,
                "*" => Multiply,
                "/" => Divide,
                _ => panic!("expected math symbol"),
            },
            name_right.into(),
        ));
    }
}

type Data = HashMap<String, Monkey>;
type Output = i64;
fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|s| s.parse::<Monkey>().unwrap())
        .map(|m| {
            (
                match m.clone() {
                    Number(name, _) => name,
                    Pending(name, _, _, _) => name,
                }
                .to_string(),
                m,
            )
        })
        .collect()
}
fn part_one(mut data: Data) -> Output {
    fn eval(map: &mut Data, name: &str) -> i64 {
        let node = map.get(name).unwrap().clone();
        match node {
            Number(_, num) => num,
            Pending(name, left, op, right) => {
                let left = eval(map, left.as_ref());
                let right = eval(map, right.as_ref());
                let res = match op {
                    Add => left + right,
                    Subtract => left - right,
                    Multiply => left * right,
                    Divide => left / right,
                };
                *map.get_mut(&name).unwrap() = Number(name.clone(), res);
                res
            }
        }
    }
    eval(&mut data, "root")
}
fn part_two(mut data: Data) -> Output {
    fn eval(map: &mut Data, name: &str) -> Option<i64> {
        if name == "humn" {
            return None;
        }
        let node = map.get(name).unwrap().clone();
        match node {
            Number(_, num) => Some(num),
            Pending(name, left, op, right) => {
                let left = eval(map, left.as_ref())?;
                let right = eval(map, right.as_ref())?;
                let res = match op {
                    Add => left + right,
                    Subtract => left - right,
                    Multiply => left * right,
                    Divide => left / right,
                };
                *map.get_mut(&name).unwrap() = Number(name.clone(), res);
                Some(res)
            }
        }
    }
    fn find_humn(map: &mut Data, name: &str, equals: i64) -> i64 {
        if name == "humn" {
            return equals;
        }
        let curr = map.get(name).unwrap().clone();
        match curr {
            Pending(_, left, op, right) => {
                if let Some(n) = eval(map, left.as_str()) {
                    match op {
                        Add => find_humn(map, right.as_str(), equals - n),
                        Subtract => find_humn(map, right.as_str(), n - equals),
                        Multiply => find_humn(map, right.as_str(), equals / n),
                        Divide => find_humn(map, right.as_str(), n / equals),
                    }
                } else {
                    let n = eval(map, right.as_ref()).unwrap();
                    match op {
                        Add => find_humn(map, left.as_str(), equals - n),
                        Subtract => find_humn(map, left.as_str(), equals + n),
                        Multiply => find_humn(map, left.as_str(), equals / n),
                        Divide => find_humn(map, left.as_str(), equals * n),
                    }
                }
            }
            _ => panic!("expected to do pending"),
        }
    }
    let root = data.get("root").unwrap().clone();
    match root {
        Pending(_, left, _, right) => {
            if let Some(n) = eval(&mut data, left.as_str()) {
                find_humn(&mut data, right.as_str(), n)
            } else {
                let n = eval(&mut data, right.as_str()).unwrap();
                find_humn(&mut data, left.as_str(), n)
            }
        }
        _ => panic!(),
    }
}

advent_of_code_macro::generate_tests!(
    day 21,
    parse,
    part_one,
    part_two,
    sample tests [152, 301],
    star tests [232_974_643_455_000, 3_740_214_169_961]
);
