use std::collections::HashMap;
use itertools::Itertools;

use year2021::io;

type Polymer = HashMap::<(char, char), i64>;
type Rules = HashMap::<(char, char), char>;

fn parse_input(input: &str) -> (Polymer, Rules) {
    let mut split = input.split("\n\n");
    let polymer: Polymer = split.next().unwrap().chars().tuple_windows().map(|(a, b)| ((a, b), 1))
        .fold(Polymer::new(), |mut p, (pair, _)| {
            p.entry(pair).and_modify(|v| *v += 1).or_insert(1); p
        });
    let rules = split.next().unwrap().lines().map(|s| {
        let mut split = s.split(" -> ");
        (split.next().unwrap().chars().collect_tuple().unwrap(),
         split.next().unwrap().chars().nth(0).unwrap())
    }).collect();
    (polymer, rules)
}

fn polymerize(polymer: &Polymer, rules: &Rules) -> Polymer {
    polymer.iter()
        .fold(polymer.clone(), |mut m, ((a, b), count)| {
            if count > &0 {
                let pair = (*a, *b);
                m.entry(pair).and_modify(|v| *v -= count);
                m.entry((*a, rules[&pair])).and_modify(|v| *v += count).or_insert(*count);
                m.entry((rules[&pair], *b)).and_modify(|v| *v += count).or_insert(*count);
            }
            m
        })
}

fn solve(steps: usize, first: char, polymer: &Polymer, rules: &Rules) -> i64 {
    let p = (0..steps).fold(polymer.clone(), |m, _| polymerize(&m, rules));
    let mut counts = p.iter().fold(HashMap::new(), |mut m, ((_, b), count)| {
        m.entry(b).and_modify(|v| *v += *count).or_insert(*count); m
    });
    *counts.entry(&first).or_default() += 1;
    counts.values().max().unwrap() - counts.values().min().unwrap()
}

fn main() {
    let input = io::read_input(14);
    let (polymer, rules) = parse_input(&input);

    assert_eq!(2003, solve(10, input.chars().nth(0).unwrap(), &polymer, &rules));
    assert_eq!(2276644000111, solve(40, input.chars().nth(0).unwrap(), &polymer, &rules));
}
