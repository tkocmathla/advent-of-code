use year2021::io;

fn parse_input(input: &str) -> Vec<i32> {
    input.split(",").map(|s| s.parse().unwrap()).collect()
}

fn part1(xs: &Vec<i32>, min: i32, max: i32) -> i32 {
    (min..=max).map(|i| xs.iter().map(|y| (i - y).abs()).sum::<i32>()).min().unwrap()
}

fn part2(xs: &Vec<i32>, min: i32, max: i32) -> i32 {
    fn sum_n(n: i32) -> i32 {
        let nf = n as f32;
        (nf * ((1.0 + nf) / 2.0)) as i32
    }
    (min..=max).map(|i| xs.iter().map(|y| sum_n((i - y).abs())).sum::<i32>()).min().unwrap()
}

fn main() {
    let input = parse_input(&io::read_input(7));
    let min = input.iter().min().unwrap();
    let max = input.iter().max().unwrap();

    assert_eq!(342641, part1(&input, *min, *max));
    assert_eq!(93006301, part2(&input, *min, *max));
}
