#[macro_export]
macro_rules! generate_tests {
    (day $day:literal,
     $parse_input:ident,
     $part_one:ident,
     $part_two:ident,
     sample tests [$part_one_sample_ans:literal, $part_two_sample_ans:literal],
     star tests [$part_one_ans:literal, $part_two_ans:literal]) => {
        #[cfg(test)]
        paste::paste! {
            mod [<day_ $day _tests>] {
                use super::{$parse_input, $part_one, $part_two};

                #[test]
                fn part_one_sample() {
                    let input = include_str!(concat!("../input/day_", $day, "_sample.txt"));
                    let data = $parse_input(input);
                    assert_eq!($part_one_sample_ans, $part_one(data));
                }

                #[test]
                fn part_one_star() {
                    let input = include_str!(concat!("../input/day_", $day, ".txt"));
                    let data = $parse_input(input);
                    assert_eq!($part_one_ans, $part_one(data));
                }

                #[test]
                fn part_two_sample() {
                    let input = include_str!(concat!("../input/day_", $day, "_sample.txt"));
                    let data = $parse_input(input);
                    assert_eq!($part_two_sample_ans, $part_two(data));
                }

                #[test]
                fn part_two_star() {
                    let input = include_str!(concat!("../input/day_", $day, ".txt"));
                    let data = $parse_input(input);
                    assert_eq!($part_two_ans, $part_two(data));
                }
            }
        }
    };
}
