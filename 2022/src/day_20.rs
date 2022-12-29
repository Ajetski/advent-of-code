type Data = Vec<i64>;
type Output = i64;
fn parse(input: &str) -> Data {
    input.lines().map(|s| s.parse().unwrap()).collect()
}
fn part_one(data: Data) -> Output {
    let mut data: Vec<(usize, i64)> = data.into_iter().enumerate().collect();
    let start_order = data.clone();
    for (start_idx, num) in start_order.into_iter().filter(|(_, num)| *num != 0) {
        let idx = data
            .iter()
            .enumerate()
            .find(|(_, (idx, _))| start_idx == *idx)
            .map(|(idx, _)| idx as i64)
            .unwrap();

        data.remove(idx as usize);
        let len = data.len() as Output;
        let mut new_idx = (idx + num) % len;

        if new_idx <= 0 {
            new_idx += len;
        }

        data.insert(new_idx as usize, (start_idx, num));
    }
    let zero_idx = data
        .iter()
        .enumerate()
        .find(|(_, (_, num))| *num == 0)
        .map(|(idx, _)| idx)
        .unwrap();
    let get_num = |n: usize| data[(n + zero_idx) % data.len()].1;
    get_num(1000) + get_num(2000) + get_num(3000)
}
fn part_two(data: Data) -> Output {
    let mut data: Vec<(usize, i64)> = data
        .into_iter()
        .map(|num| num * 811589153)
        .enumerate()
        .collect();
    let start_order = data.clone();
    for _ in 0..10 {
        for (start_idx, num) in start_order.clone().into_iter().filter(|(_, num)| *num != 0) {
            let idx = data
                .iter()
                .enumerate()
                .find(|(_, (idx, _))| start_idx == *idx)
                .map(|(idx, _)| idx as i64)
                .unwrap();

            data.remove(idx as usize);
            let len = data.len() as Output;
            let mut new_idx = (idx + num) % len;

            if new_idx <= 0 {
                new_idx += len;
            }

            data.insert(new_idx as usize, (start_idx, num));
        }
    }
    let zero_idx = data
        .iter()
        .enumerate()
        .find(|(_, (_, num))| *num == 0)
        .map(|(idx, _)| idx)
        .unwrap();
    let get_num = |n: usize| data[(n + zero_idx) % data.len()].1;
    get_num(1000) + get_num(2000) + get_num(3000)
}

advent_of_code_macro::generate_tests!(
    day 20,
    parse,
    part_one,
    part_two,
    sample tests [3, 1_623_178_306],
    star tests [7_153, 6_146_976_244_822]
);
