use std::collections::HashSet;
use std::iter::FromIterator;

use year2021::io;

fn part1(input: &str) -> usize {
    input
      .lines()
      .flat_map(|line| line.split(" | ").nth(1).unwrap().split(" "))
      .filter(|s| match s.len() { 2|3|4|7 => true, _ => false })
      .count()
}

fn part2(input: &str) -> i32 {
    input.lines().map(|line| {
        let mut split = line.split(" | ");
        let mut codes: Vec<HashSet<char>> = split.next().unwrap().split(" ").map(|s| HashSet::from_iter(s.chars())).collect();
        codes.sort_by_key(|x| x.len());
        let ciphers: Vec<HashSet<char>> = split.next().unwrap().split(" ").map(|s| HashSet::from_iter(s.chars())).collect();
        let mut nums: Vec<&HashSet<char>> = vec![&codes[0];10];
        nums[1] = &codes[0];
        nums[7] = &codes[1];
        nums[4] = &codes[2];
        nums[8] = codes.last().unwrap();
        nums[6] = codes[6..9].iter().find(|seg| (nums[1] - seg).len() == 1).unwrap();
        nums[9] = codes[6..9].iter().find(|seg| (&(nums[6] - seg) - &nums[4]).len() == 1).unwrap();
        nums[0] = codes[6..9].iter().find(|seg| seg != &nums[6] && seg != &nums[9]).unwrap();
        nums[3] = codes[3..6].iter().find(|seg| (*seg - nums[1]).len() == 3).unwrap();
        nums[2] = codes[3..6].iter().find(|seg| (*seg - nums[4]).len() == 3).unwrap();
        nums[5] = codes[3..6].iter().find(|seg| (*seg - nums[2]).len() == 2).unwrap();

        ciphers
            .iter()
            .map(|cipher| nums.iter().enumerate().find(|(_, set)| *set == &cipher).unwrap())
            .fold(String::from(""), |acc, (i, _)| acc + &i.to_string())
            .parse::<i32>().unwrap()
    }).sum::<i32>()
}

fn main() {
    let input = io::read_input(8);

    assert_eq!(452, part1(&input));
    assert_eq!(1096964, part2(&input));
}
