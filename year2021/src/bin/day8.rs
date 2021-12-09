use std::collections::HashSet;
use std::iter::FromIterator;

use year2021::io;

fn main() {
    let input = io::read_input(8);

    // part 1
    //println!("{:?}",
    //         input
    //           .lines()
    //           .flat_map(|line| line.split(" | ").nth(1).unwrap().split(" "))
    //           .filter(|s| match s.len() { 2|3|4|7 => true, _ => false })
    //           .count());

    println!("{:?}",
      input.lines().map(|line| {
        let ciphers: Vec<HashSet<char>> = line.split(" | ").nth(1).unwrap().split(" ").map(|s| HashSet::from_iter(s.chars())).collect();
        let mut codes: Vec<&str> = line.split(" | ").next().unwrap().split(" ").collect();
        codes.sort_by_key(|x| x.len());

        let mut nums: Vec<HashSet<char>> = vec![HashSet::new();10];

        nums[1] = HashSet::<_>::from_iter(codes[0].chars());
        nums[7] = HashSet::<_>::from_iter(codes[1].chars());
        nums[4] = HashSet::from_iter(codes[2].chars());
        nums[8] = HashSet::<_>::from_iter(codes.last().unwrap().chars());
        nums[6] = codes[6..9].iter().map(|code| HashSet::from_iter(code.chars())).find(|seg| (&nums[1] - &seg).len() == 1).unwrap();
        nums[9] = codes[6..9].iter().map(|code| HashSet::from_iter(code.chars())).find(|seg| (&(&nums[6] - &seg) - &nums[4]).len() == 1).unwrap();
        nums[0] = codes[6..9].iter().map(|code| HashSet::from_iter(code.chars())).find(|seg| seg != &nums[6] && seg != &nums[9]).unwrap();
        nums[3] = codes[3..6].iter().map(|code| HashSet::from_iter(code.chars())).find(|seg| (seg - &nums[1]).len() == 3).unwrap();
        nums[2] = codes[3..6].iter().map(|code| HashSet::from_iter(code.chars())).find(|seg| (seg - &nums[4]).len() == 3).unwrap();
        nums[5] = codes[3..6].iter().map(|code| HashSet::from_iter(code.chars())).find(|seg| (seg - &nums[2]).len() == 2).unwrap();

        ciphers
            .iter()
            .map(|cipher| nums.iter().enumerate().find(|(_, set)| set == &cipher).unwrap())
            .fold(String::from(""), |acc, (i, _)| acc + &i.to_string())
            .parse::<i32>().unwrap()
      }).sum::<i32>());

    //assert_eq!(452, part1(&input));
    //assert_eq!(, part2(&input));
}
