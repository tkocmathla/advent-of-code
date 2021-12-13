use std::collections::HashMap;
use std::collections::HashSet;

use itertools::Itertools;

use year2021::io;

type Path = Vec<String>;
type Paths = Vec<Path>;
type Graph = HashMap::<String, Vec<String>>;

fn parse_input(input: &str) -> Graph {
    input.lines()
         .fold(Graph::new(), |mut g, line| {
             let edge: Vec<_> = line.split("-").map(|s| String::from(s)).collect();
             g.entry(edge[0].clone()).or_insert(vec![]).push(edge[1].clone());
             g.entry(edge[1].clone()).or_insert(vec![]).push(edge[0].clone());
             g })
}

fn small(s: &String) -> bool { s.chars().all(|c| c.is_ascii_lowercase()) }

fn part1(graph: &Graph) -> i64 {
    fn dfs(g: &Graph, mut stack: Vec<String>, mut seen: HashSet<String>) -> i64 {
        match stack.pop().as_deref() {
            None => 0,
            Some("end") => 1,
            Some(u) => {
                if small(&String::from(u)) { seen.insert(String::from(u)); }
                g[u].iter()
                    .fold(0, |n, v| {
                        let mut next = stack.clone();
                        if seen.get(v).is_none() { next.push(v.clone()) }
                        n + dfs(g, next, seen.clone())
                    })
            }
        }
    }
    dfs(graph, vec![String::from("start")], HashSet::new())
}

fn part2(graph: &Graph) -> usize {
    fn dfs(g: &Graph, special: &String, mut path: Path, mut stack: Vec<String>, mut seen: HashMap<String, i32>) -> Paths {
        match stack.pop().as_deref() {
            None => vec![],
            Some("end") => { path.push(String::from("end")); vec![path] },
            Some(u) => {
                path.push(String::from(u));
                if small(path.last().unwrap()) { *seen.entry(String::from(u)).or_insert(0) += 1; }
                g[u].iter()
                    .fold(vec![], |mut acc, v| {
                        let mut next = stack.clone();
                        match seen.get(v) {
                            None => next.push(v.clone()),
                            Some(count) => {
                                if ((v == "start" || v == "end") && count == &0) || (v == special && count < &2) {
                                    next.push(v.clone())
                                }
                            }
                        }
                        acc.extend(dfs(g, special, path.clone(), next, seen.clone()));
                        acc
                    })
            }
        }
    }
    graph.keys()
         .filter(|&s| small(s) && s != "start" && s != "end")
         .flat_map(|k| dfs(graph, k, vec![], vec![String::from("start")], HashMap::new()))
         .unique()
         .count()
}

fn main() {
    let graph = parse_input(&io::read_input(12));

    assert_eq!(4378, part1(&graph));
    assert_eq!(133621, part2(&graph));
}
