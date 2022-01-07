pub fn run(lines: Vec<String>) {
    let mut h1 = 0;
    let mut d1 = 0;

    let mut aim = 0;
    let mut d2 = 0;
    let mut h2 = 0;

    for line in lines {
        let parts = line.split(" ");
        let command = parts.clone().nth(0).unwrap();
        let num: i32 = parts.clone().nth(1).unwrap().parse().unwrap();
        match command {
            "forward" => {
                h1 += num;
                h2 += num;
                d2 += aim * num;
            },
            "up" => {
                d1 -= num;
                aim -= num;
            },
            "down" => {
                d1 += num;
                aim += num;
            },
            _ => println!("unknown command: '{}'", command)
        }
    }
    println!("{}, {}", h1 * d1, h2 * d2);
}

#[cfg(test)]
mod part1 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/2_test.txt");
        assert_eq!(part_1_solution(input), 150);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/2.txt");
        assert_eq!(part_1_solution(input), 1660158);
    }
}

#[cfg(test)]
mod part2 {
    use super::*;

    #[test]
    fn run_sample() {
        let input = include_str!("../inputs/2_test.txt");
        assert_eq!(part_2_solution(input), 900);
    }

    #[test]
    fn run() {
        let input = include_str!("../inputs/2.txt");
        assert_eq!(part_2_solution(input), 1604592846);
    }
}
