use std::collections::HashMap;

use year2021::io;

type Counts = HashMap<i32, usize>;

fn parse_input(input: &str) -> Vec<i32> {
    input.split(",").map(|x| x.parse::<i32>().unwrap()).collect()
}

fn spawn(m: &Counts) -> Counts {
    let mut next = (0..=8).fold(m.clone(), |mut m, age| { m.insert(age - 1, *m.get(&age).unwrap_or(&0)); m });
    let news = *next.get(&-1).unwrap_or(&0);
    *next.entry(6).or_insert(news) += news;
    next.insert(8, news);
    next.remove(&-1);
    next
}

fn solve(xs: &Vec<i32>, days: i32) -> usize {
    let state = xs.iter().fold(HashMap::new(), |mut m: Counts, x| { m.entry(*x).and_modify(|n| *n += 1).or_insert(1); m });
    (0..days).fold(state, |m, _| spawn(&m)).values().sum()
}

fn main() {
    let input = parse_input(&io::read_input(6));

    assert_eq!(349549, solve(&input, 80));
    assert_eq!(1589590444365, solve(&input, 256));
}
