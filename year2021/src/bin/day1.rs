use year2021::io;

fn parse_input(input: String) -> Vec<i32> {
    input.lines().map(|x| x.parse::<i32>().unwrap()).collect()
}

fn part1(xs: &Vec<i32>) -> i32 {
    xs.windows(2).map(|win| (win[0] < win[1]) as i32).sum()
}

fn part2(xs: &Vec<i32>) -> i32 {
    part1(&xs.windows(3).map(|win| win.iter().sum()).collect())
}

fn main() {
    let xs = parse_input(io::read_input(1));

    assert_eq!(1462, part1(&xs));
    assert_eq!(1497, part2(&xs));
}
