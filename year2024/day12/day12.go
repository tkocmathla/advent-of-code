package day12

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
)

func score(garden Matrix, seen *map[Point]bool, start Point) (int, int) {
	a := 0
	p := 0
	for q := []Point{start}; len(q) > 0; {
		head, tail := q[0], q[1:]
		q = tail
		if _, has := (*seen)[head]; has {
			continue
		}
		(*seen)[head] = true
		a += 1
		p += 4
		for _, dir := range OrthoDirs {
			if next := head.Add(dir); garden.Valid(next) {
				if garden[next.Y][next.X] != garden[head.Y][head.X] {
					continue
				}
				p -= 1
				q = append(q, next)
			}
		}
	}
	return a, p
}

func Part1(input string) int {
	garden := NewMatrixFromFile(input)
	price := 0
	seen := map[Point]bool{}
	for y := range garden {
		for x := range garden[y] {
			a, p := score(garden, &seen, Point{Y: y, X: x})
			price += a * p
		}
	}
	return price
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day12.txt"), 1549354)
}
