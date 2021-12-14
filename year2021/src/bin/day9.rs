//use std::collections::HashSet;

use year2021::io;

type Coord = (usize, usize);
type Grid = Vec<Vec<usize>>;

fn parse_input(input: &str) -> Grid {
    input.lines()
         .map(|line| line.chars().map(|ch| ch.to_digit(10).unwrap() as usize).collect())
         .collect()
}

fn coords(grid: &Grid) -> Vec<Coord> { (0..grid.len()).flat_map(|i| (0..grid[i].len()).map(move |j| (i, j))).collect() }
fn valid(grid: &Grid, p: Coord) -> bool { grid.get(p.0).is_some() && grid[p.0].get(p.1).is_some() }

fn lows(grid: &Grid) -> Vec<Coord> {
    let neighbors = vec![(0, -1), (0, 1), (-1, 0), (1, 0)];
    coords(grid)
        .iter()
        .filter(|(i, j)| neighbors((*i, *j)).iter().all(|p| valid(grid, *p)))
        .map(|p| *p)
        .collect()
}

fn part1(grid: &Grid) -> usize {
    lows(grid).iter().map(|(i, j)| grid[*i][*j] + 1).sum()
}

//fn part2(grid: &Grid) -> usize {
//    fn dfs(grid: &Grid, mut idxs: Vec<Coord>, mut seen: HashSet<Coord>) -> HashSet<Coord> {
//        let width = 100;
//        match idxs.pop() {
//            None => seen,
//            Some(i) => {
//                seen.insert(i);
//                if i % width > 0 && !seen.contains(&(i-1)) && nums[i-1] != 9 { idxs.push(i-1) }
//                if i % width != (width - 1) && !seen.contains(&(i+1)) && nums[i+1] != 9 { idxs.push(i+1) }
//                if i >= width && !seen.contains(&(i-width)) && nums[i-width] != 9 { idxs.push(i-width) }
//                if i < nums.len() - width && !seen.contains(&(i+width)) && nums[i+width] != 9 { idxs.push(i+width) }
//                dfs(nums, idxs, seen)
//            }
//        }
//    }
//    let mut basins = lows(nums).iter().map(|(i, _)| dfs(nums, vec![*i], HashSet::new()).len()).collect::<Vec<_>>();
//    basins.sort();
//    basins.iter().rev().take(3).product()
//}

fn main() {
    let nums = parse_input(&io::read_input(9));

    assert_eq!(498, part1(&nums));
    //assert_eq!(1071000, part2(&nums));
}
