#[derive(Debug)]
struct Command {
    input: String,
    output: Vec<String>,
}
#[derive(Debug)]
struct File {
    location: String,
    size: i32,
}
type Data = Vec<Command>;
type Output = i32;
fn parse(input: &str) -> Data {
    let mut data = vec![];
    for line in input.lines() {
        const CMD_START: &str = "$ ";
        if line.starts_with(CMD_START) {
            let (_, cmd) = line.split_once(CMD_START).unwrap();
            data.push(Command {
                input: cmd.to_string(),
                output: vec![],
            });
        } else {
            data.last_mut().unwrap().output.push(line.to_string());
        }
    }
    data
}
fn get_dir_sizes_and_files(data: Data) -> (Vec<i32>, Vec<File>) {
    let mut files = vec![];
    let mut dirs = vec![];
    let mut cwd = vec!["/".to_owned()];
    for c in data {
        if c.input == "ls" {
            let cwd = cwd.join("/");
            for line in c.output {
                let (left, right) = line.split_once(' ').unwrap();
                if left == "dir" {
                    dirs.push(cwd.clone() + "/" + right);
                } else {
                    let size: i32 = left.parse().unwrap();
                    files.push(File {
                        size,
                        location: cwd.clone() + "/" + right,
                    });
                }
            }
        } else if c.input.clone().starts_with("cd") {
            let (_, loc) = c.input.split_once(' ').unwrap();
            if loc == ".." {
                cwd.pop();
            } else {
                cwd.push(loc.to_string());
            }
        }
    }
    (
        dirs.into_iter()
            .map(|curr| {
                let mut sum = 0;
                for file in &files {
                    if file.location.starts_with(&curr) {
                        sum += file.size;
                    }
                }
                sum
            })
            .collect(),
        files,
    )
}
fn part_one(data: Data) -> Output {
    let (dirs_sizes, _) = get_dir_sizes_and_files(data);
    dirs_sizes.into_iter().filter(|size| size <= &100_000).sum()
}
fn part_two(data: Data) -> Output {
    let (dirs_sizes, files) = get_dir_sizes_and_files(data);
    let total: i32 = files.iter().map(|f| f.size).sum();
    dirs_sizes
        .into_iter()
        .filter(|size| 70_000_000 - total + size >= 30_000_000)
        .min()
        .unwrap()
}

advent_of_code_macro::generate_tests!(
    day 7,
    parse,
    part_one,
    part_two,
    sample tests [95437, 24_933_642],
    star tests [1_348_005, 12_785_886]
);
