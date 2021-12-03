use year2021::io;

fn parse_input(input: String) -> Vec<i32> {
    input.lines().map(|s| i32::from_str_radix(s, 2).unwrap()).collect()
}

fn part1(xs: &Vec<i32>) -> i32 {
    (0..12)
     .map(|i| xs.iter().fold(0, |n, x| { n + ((x >> i) & 1) }))
     .enumerate()
     .fold([0, 0], |[g, e], (i, x)| {
         if x >= 500 {
             [g + (1 << i), e]
         } else {
             [g, e + (1 << i)]
         }
     }).iter().take(2).product()
}

fn part2(input: Vec<i32>) -> i32 {
    fn rate(xs: Vec<i32>, co2: bool) -> i32 {
        (0..12).rev().fold(xs, |acc, i| {
          let (zeros, ones): (Vec<i32>, Vec<i32>) = acc.iter().partition(|&x| ((x >> i) & 1) == 0);
          if acc.len() == 1 {
              acc
          } else if (co2 && ones.len() < zeros.len()) || (!co2 && ones.len() >= zeros.len()) {
              ones
          } else {
              zeros
          }
        }).first().cloned().unwrap()
    }
    rate(input.clone(), false) * rate(input, true)
}

fn main() {
    let input = parse_input(io::read_input(3));

    assert_eq!(4139586, part1(&input));
    assert_eq!(1800151, part2(input));
}
