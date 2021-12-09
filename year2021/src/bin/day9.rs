use std::collections::HashSet;

use year2021::io;

fn parse_input(input: &str) -> Vec<u32> {
    input.lines()
         .fold(String::from(""), |s, line| s + line)
         .chars()
         .map(|c| c.to_digit(10).unwrap()).collect()
}

fn lows(nums: &Vec<u32>) -> Vec<(usize, u32)> {
    let width = 100;
    nums.iter().enumerate()
        .filter(|(i, &x)| {
            let w = if i % width > 0 { nums[i-1] > x } else { true };
            let e = if i % width != (width - 1) { nums[i+1] > x } else { true };
            let n = if i >= &width { nums[i-width] > x } else { true };
            let s = if i < &(nums.len() - width) { nums[i+width] > x } else { true };
            n && e && s && w
        })
        .map(|(i, &x)| (i, x))
        .collect()
}

fn part1(nums: &Vec<u32>) -> u32 {
    lows(nums).iter().map(|(_, x)| x + 1).sum()
}

fn part2(nums: &Vec<u32>) -> usize {
    fn dfs(nums: &Vec<u32>, mut idxs: Vec<usize>, mut seen: HashSet<usize>) -> HashSet<usize> {
        let width = 100;
        match idxs.pop() {
            None => seen,
            Some(i) => {
                seen.insert(i);
                if i % width > 0 && !seen.contains(&(i-1)) && nums[i-1] != 9 { idxs.push(i-1) }
                if i % width != (width - 1) && !seen.contains(&(i+1)) && nums[i+1] != 9 { idxs.push(i+1) }
                if i >= width && !seen.contains(&(i-width)) && nums[i-width] != 9 { idxs.push(i-width) }
                if i < nums.len() - width && !seen.contains(&(i+width)) && nums[i+width] != 9 { idxs.push(i+width) }
                dfs(nums, idxs, seen)
            }
        }
    }
    let mut basins = lows(nums).iter().map(|(i, _)| dfs(nums, vec![*i], HashSet::new()).len()).collect::<Vec<_>>();
    basins.sort();
    basins.iter().rev().take(3).product()
}

fn main() {
    let nums = parse_input(&io::read_input(9));

    assert_eq!(498, part1(&nums));
    assert_eq!(1071000, part2(&nums));
}
