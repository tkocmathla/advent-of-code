package day8

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
)

func pairs(locs []Point) [][2]Point {
	var ps [][2]Point
	for i, p1 := range locs {
		for _, p2 := range locs[i:] {
			if p1 == p2 {
				continue
			}
			ps = append(ps, [2]Point{p1, p2})
		}
	}
	return ps
}

func slope(ps [2]Point) Point {
	return Point{Y: ps[1].Y - ps[0].Y, X: ps[1].X - ps[0].X}
}

func scan(grid Matrix, loc Point, m Point, self bool, max_n int, anodes *map[Point]bool) {
	if !self {
		loc.Y += m.Y
		loc.X += m.X
	}
	for n := 0; n < max_n && loc.Valid(grid); n++ {
		(*anodes)[loc] = true
		loc.Y += m.Y
		loc.X += m.X
	}
}

func solve(input string, self bool, max_n int) int {
	grid := NewMatrix(input)
	locs := make(map[rune][]Point)
	for y := range grid {
		for x, c := range grid[y] {
			if c != '.' {
				locs[c] = append(locs[c], Point{Y: y, X: x})
			}
		}
	}
	anodes := make(map[Point]bool)
	for c := range locs {
		for _, p := range pairs(locs[c]) {
			mpos := slope(p)
			mneg := Point{Y: -mpos.Y, X: -mpos.X}
			scan(grid, p[0], mneg, self, max_n, &anodes)
			scan(grid, p[1], mpos, self, max_n, &anodes)
		}
	}
	return len(anodes)
}

func Part1(input string) int {
	return solve(input, false, 1)
}

func Part2(input string) int {
	return solve(input, true, 999)
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day8.txt"), 271)
	AssertEq(TimeFunc(Part2, "data/day8.txt"), 994)
}
