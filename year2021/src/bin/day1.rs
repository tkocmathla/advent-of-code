use year2021::io;

fn winsum(xs: Vec<i32>) -> i32 {
    xs.windows(2).map(|win| (win[0] < win[1]) as i32).sum()
}

fn part1(input: &str) -> i32 {
    winsum(input.lines().map(|x| x.parse::<i32>().unwrap()).collect())
}

fn part2(input: &str) -> i32 {
    let xs: Vec<_> = input.lines().map(|x| x.parse::<i32>().unwrap()).collect();
    winsum(xs.windows(3).map(|win| win.iter().sum()).collect())
}

fn main() {
    let input = io::read_input(1);

    assert_eq!(1462, part1(&input));
    assert_eq!(1497, part2(&input));
}
