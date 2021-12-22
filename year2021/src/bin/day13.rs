use std::collections::HashSet;
use itertools::Itertools;

use year2021::io;

type Coord = (u32, u32);
type Move = (char, u32);

fn parse_input(input: &str) -> (HashSet::<Coord>, Vec<Move>) {
    let mut split = input.split("\n\n");
    let coords = split.next().unwrap().lines().map(|line| line.split(",").map(|s| s.parse().unwrap()).collect_tuple().unwrap()).collect();
    let moves = split.next().unwrap().lines().map(|line| (line.chars().nth(11).unwrap(), line[13..].parse().unwrap())).collect();
    (coords, moves)
}

fn print_coords(coords: &HashSet::<Coord>) {
    let max_x = coords.iter().map(|(x, _)| x).max().unwrap();
    let max_y = coords.iter().map(|(_, y)| y).max().unwrap();
    for y in 0..=*max_y {
        for x in 0..=*max_x {
            if coords.contains(&(x, y)) {
                print!("#");
            } else {
                print!(" ");
            }
        }
        println!();
    }
    println!();
}

fn fold_up(coords: &HashSet::<Coord>, moves: &Vec<Move>) -> HashSet::<Coord> {
    moves.iter()
         .fold(coords.clone(), |cs, mv| {
         let max_x = cs.iter().map(|(x, _)| x).max().unwrap();
         let max_y = cs.iter().map(|(_, y)| y).max().unwrap();
             match mv {
                 ('x', axis) => cs.iter().map(|(x, y)| if x > axis { (max_x - x, *y) } else { (*x, *y) }).collect(),
                 ('y', axis) => cs.iter().map(|(x, y)| if y > axis { (*x, max_y - y) } else { (*x, *y) }).collect(),
                 _ => cs
             }
         })
}

fn part1(coords: &HashSet::<Coord>, moves: &Vec<Move>) -> usize {
    let one = moves.clone().into_iter().take(1).collect();
    fold_up(coords, &one).len()
}

fn part2(coords: &HashSet::<Coord>, moves: &Vec<Move>) {
    let folded = fold_up(coords, moves);
    print_coords(&folded);
}

fn main() {
    let (coords, moves) = parse_input(&io::read_input(13));

    assert_eq!(765, part1(&coords, &moves));
    part2(&coords, &moves); // RZKZLPGH
}
