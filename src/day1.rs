fn part_1_solution(input: &str) -> u64 {
    

    todo!();
}

#[cfg(test)]
mod part1 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/1_test.txt");
        assert_eq!(part_1_solution(input), 7);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/1.txt");
        assert_eq!(part_1_solution(input), 1139);
    }
}

#[cfg(test)]
mod part2 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/1_test.txt");
        assert_eq!(part_2_solution(input), 5);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/1.txt");
        assert_eq!(part_2_solution(input), 1103);
    }
}
