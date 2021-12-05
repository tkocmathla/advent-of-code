use std::cmp::Ordering;
use std::collections::HashMap;

use itertools::Itertools;

use year2021::io;

#[derive(Copy, Clone, PartialEq, Eq, Hash)]
struct Point(i32, i32);
struct Line(Point, Point, Option<f32>);

fn slope(p1: Point, p2: Point) -> Option<f32> {
    if p1.0 == p2.0 {
        None
    } else {
        Some((p2.1 - p1.1) as f32 / (p2.0 - p1.0) as f32)
    }
}

fn parse_point(point: &str) -> Point {
    let mut xs = point.split(",").map(|x| x.parse().unwrap());
    Point(xs.next().unwrap(), xs.next().unwrap())
}

fn parse_line(line: &str) -> Line {
    let mut xs: Vec<Point> = line.split(" -> ").map(parse_point).collect();
    xs.sort_by(|u, v| if u.0 < v.0 || (u.0 == v.0 && u.1 < v.1) { Ordering::Less } else { Ordering::Greater });
    Line(xs[0], xs[1], slope(xs[0], xs[1]))
}

fn parse_input(input: &str) -> Vec<Line> {
    input.lines().map(parse_line).collect()
}

fn part1(lines: &Vec<Line>) -> usize {
    lines
      .iter()
      .filter(|l| l.2 == Some(0.0) || l.2 == None)
      .flat_map(|l| (l.0.0..=l.1.0).cartesian_product(l.0.1..=l.1.1).map(|(x, y)| Point(x, y)).collect::<Vec<Point>>())
      .fold(HashMap::new(), |mut m, p| { m.entry(p).and_modify(|c| *c += 1).or_insert(1); m })
      .values().filter(|&&x| x > 1).count()
}

fn part2(lines: &Vec<Line>) -> usize {
    lines
      .iter()
      .flat_map(|l| {
          let ys: Vec<i32> = if l.2 == Some(-1.0) { (l.1.1..=l.0.1).rev().collect() } else { (l.0.1..=l.1.1).collect() };
          (l.0.0..=l.1.0)
              .cartesian_product(ys)
              .map(|(x, y)| Point(x, y))
              .filter(|&p| l.0 == p || slope(l.0, p) == l.2)
              .collect::<Vec<Point>>()
      })
      .fold(HashMap::new(), |mut m, p| { m.entry(p).and_modify(|c| *c += 1).or_insert(1); m })
      .values().filter(|&&x| x > 1).count()
}

fn main() {
    let lines = parse_input(&io::read_input(5));

    assert_eq!(7297, part1(&lines));
    assert_eq!(21038, part2(&lines));
}
