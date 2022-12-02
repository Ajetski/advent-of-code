use Shape::*;

#[derive(Debug, PartialEq, Eq)]
enum Shape {
    Rock,
    Paper,
    Scissors,
}
impl Shape {
    fn from_char(c: char) -> Self {
        match c {
            'A' | 'X' => Rock,
            'B' | 'Y' => Paper,
            'C' | 'Z' => Scissors,
            _ => panic!("expected A, Y, B, X, C, or Z"),
        }
    }
    fn as_int(&self) -> i32 {
        match self {
            Rock => 1,
            Paper => 2,
            Scissors => 3,
        }
    }
    fn loses_to(&self) -> Self {
        match self {
            Rock => Paper,
            Paper => Scissors,
            Scissors => Rock,
        }
    }
    fn wins_to(&self) -> Self {
        match self {
            Paper => Rock,
            Scissors => Paper,
            Rock => Scissors,
        }
    }
}

advent_of_code_macro::solve_problem!(
    day 2,
    Input Vec<Vec<char>>,
    parse |input: &str| {
        input.lines().map(|line| line.split(' ').map(|s| s.chars().next().unwrap()).collect()).collect()
    },
    part one |data: Input| {
        data.iter().map(|round| {
            let my_move = Shape::from_char(round[1]);
            let other_move = Shape::from_char(round[0]);
            let round_result = if my_move == other_move {
                3
            } else if my_move.loses_to() == other_move {
                0
            } else {
                6
            };
            round_result + my_move.as_int()
        }).sum()
    },
    part two |data: Input| {
        data.iter().map(|round| match round[1] {
            'X' =>  Shape::from_char(round[0]).wins_to().as_int(),
            'Y' =>  Shape::from_char(round[0]).as_int() + 3,
            'Z' =>  Shape::from_char(round[0]).loses_to().as_int() + 6,
            _ => panic!("expected X, Y, or Z"),
        }).sum()
    },
    sample tests [15, 12],
    star tests [11063, 10349]
);
