use year2021::io;

fn parse_input(input: String) -> Vec<(String, i32)> {
    fn pair(s: &str) -> (String, i32) {
        let mut xs = s.split(" ");
        (String::from(xs.next().unwrap()), xs.next().unwrap().parse::<i32>().unwrap())
    }
    input.lines().map(pair).collect()
}

fn part1(cmds: &Vec<(String, i32)>) -> i32 {
    let res = cmds.iter().fold((0, 0), |v, cmd|
       match cmd.0.as_str() {
           "forward" => (v.0 + cmd.1, v.1),
           "down" => (v.0, v.1 + cmd.1),
           "up" => (v.0, v.1 - cmd.1),
           _ => v
       });
    res.0 * res.1
}

fn part2(cmds: &Vec<(String, i32)>) -> i32 {
    let res = cmds.iter().fold((0, 0, 0), |v, cmd|
        match cmd.0.as_str() {
            "forward" => (v.0 + cmd.1, v.1 + cmd.1 * v.2, v.2),
            "down" => (v.0, v.1, v.2 + cmd.1),
            "up" => (v.0, v.1, v.2 - cmd.1),
            _ => v,
        }
    );
    res.0 * res.1
}

fn main() {
    let input = parse_input(io::read_input(2));

    assert_eq!(1580000, part1(&input));
    assert_eq!(1251263225, part2(&input));
}
