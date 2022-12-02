#[macro_export]
macro_rules! solve_problem {
    (day $day:literal,
     Input $input_type:ty,
     parse $parse_input:expr,
     part one $part_one:expr,
     part two $part_two:expr,
     sample tests [$part_one_sample_ans:literal, $part_two_sample_ans:literal],
     star tests [$part_one_ans:literal, $part_two_ans:literal]) => {
        #[cfg(test)]
        paste::paste! {
            mod [<day_ $day _tests>] {
                use super::*;
                type Input = $input_type;
                fn parse_input(s: &str) -> Input {
                    $parse_input(s)
                }

                fn part_one_solution(data: Input) -> i32 {
                    $part_one(data)
                }

                fn part_two_solution(data: Input) -> i32 {
                    $part_two(data)
                }

                #[test]
                fn part_one_sample() {
                    let input = include_str!(concat!("../input/day_", $day, "_sample.txt"));
                    let data = parse_input(input);
                    assert_eq!($part_one_sample_ans, part_one_solution(data));
                }

                #[test]
                fn part_one() {
                    let input = include_str!(concat!("../input/day_", $day, ".txt"));
                    let data = parse_input(input);
                    assert_eq!($part_one_ans, part_one_solution(data));
                }

                #[test]
                fn part_two_sample() {
                    let input = include_str!(concat!("../input/day_", $day, "_sample.txt"));
                    let data = parse_input(input);
                    assert_eq!($part_two_sample_ans, part_two_solution(data));
                }

                #[test]
                fn part_two() {
                    let input = include_str!(concat!("../input/day_", $day, ".txt"));
                    let data = parse_input(input);
                    assert_eq!($part_two_ans, part_two_solution(data));
                }
            }
        }
    };
}
