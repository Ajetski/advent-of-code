use std::collections::HashMap;
#[derive(Debug)]
struct Node {
    id: String,
    rate: i64,
    edges: Vec<String>,
}
type Data = (String, HashMap<String, Node>);
type Output = i64;
fn parse(input: &str) -> Data {
    let start = input.lines().next().unwrap()[6..=7].to_string();
    let map = input
        .lines()
        .map(|l| {
            let id = l[6..=7].to_string();

            let rate = l
                .chars()
                .skip_while(|c| *c != '=')
                .skip(1)
                .take_while(|c| *c != ';')
                .collect::<String>()
                .parse()
                .unwrap();

            let edges_str = l
                .chars()
                .skip_while(|c| *c != ';')
                .skip_while(|c| *c != 'v')
                .skip_while(|c| *c != ' ')
                .skip(1)
                .collect::<String>();
            let edges = edges_str.split(", ").map(str::to_owned).collect();

            (id.clone(), Node { id, edges, rate })
        })
        .collect();
    (start, map)
}
fn part_one((start, mut graph): Data) -> Output {
    fn solve(
        graph: &mut HashMap<String, Node>,
        curr: &str,
        parent: &str,
        amount_released: i64,
        time_left: i64,
    ) -> Output {
        if time_left == 0 {
            return amount_released;
        }

        let mut max = 0;
        let rate = graph.get(curr).unwrap().rate;
        if rate > 0 {
            graph.get_mut(curr).unwrap().rate = 0;
            max = solve(
                &mut *graph,
                curr,
                curr,
                amount_released + rate * (time_left - 1),
                time_left - 1,
            );
            graph.get_mut(curr).unwrap().rate = rate;
        }

        for edge in &graph.get(curr).unwrap().edges.clone() {
            if edge != parent {
                max = std::cmp::max(
                    max,
                    solve(graph, edge, curr, amount_released, time_left - 1),
                );
            }
        }
        max
    }
    solve(&mut graph, &start, "", 0, 30)
}
fn part_two(_data: Data) -> Output {
    todo!()
}

advent_of_code_macro::generate_tests!(
    day 16,
    parse,
    part_one,
    part_two,
    sample tests [1_651, 0],
    star tests [0, 0]
);
