use Instruction::*;
#[derive(Clone, Copy)]
enum Instruction {
    Addx(i32),
    Noop,
}
type Data = Vec<Instruction>;
type Output = i32;
fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|line| {
            if line.starts_with("addx") {
                let (_addx, num) = line.split_once(' ').unwrap();
                Addx(num.parse().unwrap())
            } else {
                Noop
            }
        })
        .collect()
}
fn part_one(data: Data) -> Output {
    let mut r_x = 1;
    let mut instr_iter = data.iter();
    let mut cycles_left = 0;
    let mut last_instr = Noop;
    let mut sum = 0;
    for cycle in 1.. {
        if cycles_left == 0 {
            match last_instr {
                Noop => {}
                Addx(num) => {
                    r_x += num;
                }
            }
            last_instr = match instr_iter.next() {
                Some(instr) => *instr,
                _ => break,
            };
            cycles_left = match last_instr {
                Addx(_) => 2,
                Noop => 1,
            }
        }
        if (cycle - 20) % 40 == 0 {
            sum += r_x * cycle;
        }
        cycles_left -= 1;
    }
    sum
}
fn part_two(data: Data) -> String {
    let mut r_x = 1;
    let mut instr_iter = data.iter();
    let mut cycles_left = 0;
    let mut last_instr = Noop;
    let mut output = vec![];
    for cycle in 0.. {
        if cycles_left == 0 {
            match last_instr {
                Noop => {}
                Addx(num) => {
                    r_x += num;
                }
            }
            last_instr = match instr_iter.next() {
                Some(instr) => *instr,
                _ => break,
            };
            cycles_left = match last_instr {
                Addx(_) => 2,
                Noop => 1,
            }
        }
        let i: i32 = cycle % 40;
        if i == 0 {
            output.push("".to_string());
        }
        if i.abs_diff(r_x) <= 1 {
            *output.last_mut().unwrap() += "#";
        } else {
            *output.last_mut().unwrap() += ".";
        }
        cycles_left -= 1;
    }
    output.join("\n")
}

advent_of_code_macro::generate_tests!(
    day 10,
    parse,
    part_one,
    part_two,
    sample tests [
        13140,
"##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######....."
    ],
    star tests [
        17380,
"####..##...##..#..#.####.###..####..##..
#....#..#.#..#.#..#....#.#..#.#....#..#.
###..#....#....#..#...#..#..#.###..#....
#....#.##.#....#..#..#...###..#....#....
#....#..#.#..#.#..#.#....#.#..#....#..#.
#.....###..##...##..####.#..#.####..##.."
    ]
);
