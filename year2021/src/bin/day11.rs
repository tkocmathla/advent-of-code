use itertools::iproduct;

use year2021::io;

type Grid = Vec<Vec<usize>>;

fn print_grid(grid: &Grid) {
    for row in grid {
        println!("{:?}", row);
    }
    println!();
}

fn parse_input(input: String) -> Grid {
    input.lines()
         .map(|line| line.chars().map(|ch| ch.to_digit(10).unwrap() as usize).collect())
         .collect()
}

fn zeros(grid: &Grid) -> usize {
    grid.iter().map(|v| v.iter().filter(|x| **x == 0).count()).sum::<usize>()
}

fn neighbors(grid: &Grid, y: usize, x: usize) -> Vec<(usize, usize)> {
    let x_min = if x > 0 { x - 1 } else { 0 };
    let x_max = if x < grid[0].len() - 1 { x + 2 } else { grid[0].len()};
    let y_min = if y > 0 { y - 1 } else { 0 };
    let y_max = if y < grid.len() - 1 { y + 2 } else { grid.len() };
    iproduct!(y_min..y_max, x_min..x_max)
        .filter(|(ny, nx)| (ny, nx) != (&y, &x))
        .collect()
}

fn step(grid: &mut Grid) {
    let mut stack: Vec<(usize, usize)> = iproduct!(0..grid.len(), 0..grid[0].len()).collect();
    while let Some((y, x)) = stack.pop() {
        match grid[y][x] {
            10 => {},
            9  => { stack.extend(neighbors(&grid, y, x)); grid[y][x] += 1 },
            _  => { grid[y][x] += 1; },
        }
    }
    grid.iter_mut().flatten().for_each(|x| if *x == 10 { *x = 0 });
}

fn part1(grid: &Grid, steps: i32) -> usize {
    let mut g = grid.clone();
    (0..steps).fold(0, |n, _| { step(&mut g); n + zeros(&g) })
}

fn part2(grid: &Grid) -> usize {
    let mut g = grid.clone();
    let mut n = 1;
    loop {
        step(&mut g);
        if g.iter().flatten().all(|x| *x == 0) { break; }
        n += 1;
    }
    n
}

fn main() {
    let grid = parse_input(io::read_input(11));

    assert_eq!(1688, part1(&grid, 100));
    assert_eq!(403, part2(&grid));
}
