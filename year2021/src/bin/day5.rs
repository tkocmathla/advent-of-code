use std::cmp::Ordering;
use std::collections::HashMap;

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
      .fold(HashMap::new(), |mut m, line| {
          for x in line.0.0..=line.1.0 {
              for y in line.0.1..=line.1.1 {
                   *m.entry(Point(x,y)).or_insert(0) += 1;
              }
          }
          m
    }).values().filter(|&&x| x > 1).count()
}

fn part2(lines: &Vec<Line>) -> usize {
    lines
      .iter()
      .fold(HashMap::new(), |mut m, line| {
          let ys: Vec<i32> = if line.2 == Some(-1.0) { (line.1.1..=line.0.1).rev().collect() } else { (line.0.1..=line.1.1).collect() };
          for x in line.0.0..=line.1.0 {
              for y in &ys {
                  let p = Point(x, *y);
                  if line.0 == p || slope(line.0, p) == line.2 {
                      *m.entry(p).or_insert(0) += 1;
                  }
              }
          }
          m
    }).values().filter(|&&x| x > 1).count()
}

fn main() {
    let lines = parse_input(&io::read_input(5));

    assert_eq!(7297, part1(&lines));
    assert_eq!(21038, part2(&lines));
}
