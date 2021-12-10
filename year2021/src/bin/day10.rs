use year2021::io;

fn pair(a: char, b: char) -> bool {
    let diff = b as i8 - a as i8;
    diff == 1 || diff == 2
}

fn collapse(xs: &Vec<char>) -> Vec<char> {
    xs.iter().fold(vec![], |mut v, &c| {
        match (v.last(), c) {
            (Some(&last), ')'|']'|'}'|'>') => { if pair(last, c) { v.pop(); } else { v.push(c); } v },
            _ => { v.push(c); v }
        }
    })
}

fn valid(xs: &Vec<char>) -> bool { xs.iter().all(|c| match c { ')'|']'|'}'|'>' => false, _ => true }) }

fn parse_input(s: String) -> Vec<Vec<char>> { s.lines().map(|ln| collapse(&ln.chars().collect())).collect() }

fn part1(input: &Vec<Vec<char>>) -> i32 {
    let points = |&c| match c { ')' => 3, ']' => 57, '}' => 1197, '>' => 25137, _ => 0 };
    input.iter()
         .filter(|xs| !valid(xs))
         .map(|xs| xs.iter().find(|c| match c { ')'|']'|'}'|'>' => true, _ => false }).unwrap())
         .map(points).sum::<i32>()
}

fn part2(input: &Vec<Vec<char>>) -> i64 {
    let points = |c| match c { '(' => 1, '[' => 2, '{' => 3, '<' => 4, _ => 0 };
    let mut v = input.iter()
                 .filter(|xs| valid(xs))
                 .map(|xs| xs.iter().rev().fold(0i64, |n, &c| n * 5 + points(c)))
                 .collect::<Vec<_>>();
    v.sort();
    v[v.len() / 2]
}

fn main() {
    let input = parse_input(io::read_input(10));

    assert_eq!(469755, part1(&input));
    assert_eq!(2762335572, part2(&input));
}
