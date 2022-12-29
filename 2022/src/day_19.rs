use std::collections::{BinaryHeap, HashSet};

#[derive(Debug)]
struct Blueprint {
    id: i64,
    ore_robot_cost: i64,
    clay_robot_cost: i64,
    obsidian_robot_cost: (i64, i64),
    geode_robot_cost: (i64, i64),
}
#[derive(Debug, Hash, PartialEq, Eq, Clone, Copy)]
struct Search {
    time_left: i64,
    ore: i64,
    ore_robots: i64,
    clay: i64,
    clay_robots: i64,
    obsidian: i64,
    obsidian_robots: i64,
    geode: i64,
    geode_robots: i64,
}
impl std::cmp::Ord for Search {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        (self.geode_robots * self.time_left + self.geode)
            .cmp(&(other.geode_robots * other.time_left + other.geode))
            .then_with(|| self.obsidian_robots.cmp(&other.obsidian_robots))
            .then_with(|| self.clay_robots.cmp(&other.clay_robots))
            .then_with(|| self.ore_robots.cmp(&other.ore_robots))
    }
}
impl std::cmp::PartialOrd for Search {
    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
        Some(self.cmp(other))
    }
}
type Data = Vec<Blueprint>;
type Output = i64;
fn parse(input: &str) -> Data {
    input
        .lines()
        .map(|l| Blueprint {
            id: get_id(l),
            ore_robot_cost: get_cost_one(l, "ore"),
            clay_robot_cost: get_cost_one(l, "clay"),
            obsidian_robot_cost: get_cost_two(l, "obsidian"),
            geode_robot_cost: get_cost_two(l, "geode"),
        })
        .collect()
}
fn max_geodes(bp: &Blueprint, initial_search: Search) -> Output {
    let mut queue: BinaryHeap<Search> = BinaryHeap::new();
    let mut visited: HashSet<Search> = HashSet::new();
    queue.push(initial_search);
    let mut max = 0;
    while let Some(search) = queue.pop() {
        if search.time_left == 0 {
            println!("{:?}, {:?}", max, search);
            max = std::cmp::max(search.geode, max);
        }
        {
            let mut temp_search = search;
            while temp_search.ore >= bp.ore_robot_cost {
                temp_search.ore -= bp.ore_robot_cost;
                temp_search.ore_robots += 1;
            }
            if !visited.contains(&temp_search) {
                visited.insert(temp_search);
                queue.push(temp_search);
            }
        }
        {
            let mut temp_search = search;
            while temp_search.ore >= bp.clay_robot_cost {
                temp_search.ore -= bp.clay_robot_cost;
                temp_search.clay_robots += 1;
            }
            if !visited.contains(&temp_search) {
                visited.insert(temp_search);
                queue.push(temp_search);
            }
        }
        {
            let mut temp_search = search;
            while temp_search.ore >= bp.obsidian_robot_cost.0
                && temp_search.clay >= bp.obsidian_robot_cost.1
            {
                temp_search.ore -= bp.obsidian_robot_cost.0;
                temp_search.clay -= bp.obsidian_robot_cost.1;
                temp_search.obsidian_robots += 1;
            }
            if !visited.contains(&temp_search) {
                visited.insert(temp_search);
                queue.push(temp_search);
            }
        }
        {
            let mut temp_search = search;
            while temp_search.ore >= bp.geode_robot_cost.0
                && temp_search.obsidian >= bp.geode_robot_cost.1
            {
                temp_search.ore -= bp.geode_robot_cost.0;
                temp_search.obsidian -= bp.geode_robot_cost.1;
                temp_search.geode_robots += 1;
            }
            if !visited.contains(&temp_search) {
                visited.insert(temp_search);
                queue.push(temp_search);
            }
        }
        {
            let mut temp_search = search;
            temp_search.ore += temp_search.ore_robots;
            temp_search.clay += temp_search.clay_robots;
            temp_search.obsidian += temp_search.obsidian_robots;
            temp_search.geode += temp_search.geode_robots;
            temp_search.time_left -= 1;
            if !visited.contains(&temp_search) {
                visited.insert(temp_search);
                queue.push(temp_search);
            }
        }
    }
    max
}
fn part_one(data: Data) -> Output {
    for bp in data {
        let res = max_geodes(
            &bp,
            Search {
                time_left: 24,
                ore: 0,
                ore_robots: 1,
                clay: 0,
                clay_robots: 0,
                obsidian: 0,
                obsidian_robots: 0,
                geode: 0,
                geode_robots: 0,
            },
        );
        dbg!(res);
    }
    todo!()
}
fn part_two(data: Data) -> Output {
    todo!()
}

advent_of_code_macro::generate_tests!(
    day 19,
    parse,
    part_one,
    part_two,
    sample tests [0, 0],
    star tests [0, 0]
);

// helper funtions for parsing
fn get_cost_one(l: &str, t: &str) -> i64 {
    l.split_once(&format!("{} robot costs ", t))
        .unwrap()
        .1
        .split_once(' ')
        .unwrap()
        .0
        .parse()
        .unwrap()
}
fn get_cost_two(l: &str, t: &str) -> (i64, i64) {
    (
        l.split_once(&format!("{} robot costs ", t))
            .unwrap()
            .1
            .split_once(' ')
            .unwrap()
            .0
            .parse()
            .unwrap(),
        l.split_once(&format!("{} robot costs ", t))
            .unwrap()
            .1
            .split_once("and ")
            .unwrap()
            .1
            .split_once(' ')
            .unwrap()
            .0
            .parse()
            .unwrap(),
    )
}
fn get_id(l: &str) -> i64 {
    l.split_once(' ')
        .unwrap()
        .1
        .split_once(':')
        .unwrap()
        .0
        .parse()
        .unwrap()
}
