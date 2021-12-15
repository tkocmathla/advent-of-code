use std::cmp::Reverse;
use std::collections::HashMap;
use itertools::iproduct;
use priority_queue::PriorityQueue;

use year2021::io;

type Coord = (usize, usize);
type Grid = Vec<Vec<usize>>;

fn parse_input(input: &str) -> Grid {
    input.lines()
         .map(|line| line.chars().map(|ch| ch.to_digit(10).unwrap() as usize).collect())
         .collect()
}

fn neighbors(grid: &Grid, (x, y): Coord) -> Vec<Coord> {
    let mut xs = vec![];
    if x < grid[0].len() - 1 { xs.push((x + 1, y)) }
    if y > 0 { xs.push((x, y - 1)) }
    if y < grid.len() - 1 { xs.push((x, y + 1)) }
    xs
}

fn part1(grid: &Grid) -> usize {
    let mut pq = PriorityQueue::new();
    let mut dist: HashMap<_, _> = iproduct!(0..grid.len(), 0..grid[0].len()).map(|coord| (coord, usize::MAX)).collect();

    pq.push((0, 0), Reverse(0));
    dist.entry((0, 0)).and_modify(|v| *v = 0);

    while let Some((coord, Reverse(cost))) = pq.pop() {
        for nbr in neighbors(grid, coord) {
            let next = cost + grid[nbr.1][nbr.0];
            if next < dist[&nbr] {
                pq.push(nbr, Reverse(next));
                dist.entry(nbr).and_modify(|v| *v = next);
            }
        }
    }
    dist[&(grid.len() - 1, grid[0].len() - 1)]
}

fn part2(grid: &Grid) -> usize {
    let roll = |i, x| if x + i > 9 { (x + i) % 9 } else { x + i };
    let mut big_grid: Grid = grid.iter()
        .map(|row| (0..5).flat_map(|i| row.iter().map(|x| roll(i, x)).collect::<Vec<_>>()).collect())
        .collect();

    // TODO surely there's a less disgusting way to do this
    big_grid = (0..5)
        .flat_map(|i| { let mut xs = big_grid.clone();
                        iproduct!(0..big_grid.len(), 0..big_grid[0].len()).for_each(|(y, x)| xs[y][x] = if xs[y][x] + i > 9 { (xs[y][x] + i) % 9 } else { xs[y][x] + i });
                        xs })
        .collect::<Grid>();

    part1(&big_grid)
}

fn main() {
    let grid = parse_input(&io::read_input(15));

    assert_eq!(393, part1(&grid));
    assert_eq!(2823, part2(&grid));
}
