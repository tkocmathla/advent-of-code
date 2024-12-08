package day8

import (
	aoc "aoc/util"
	"os"
	s "strings"
)

func pairs(locs []aoc.Point) [][2]aoc.Point {
	var ps [][2]aoc.Point
	for i, p1 := range locs {
		for _, p2 := range locs[i:] {
			if p1 == p2 {
				continue
			}
			ps = append(ps, [2]aoc.Point{p1, p2})
		}
	}
	return ps
}

func slope(ps [2]aoc.Point) aoc.Point {
	return aoc.Point{Y: ps[1].Y - ps[0].Y, X: ps[1].X - ps[0].X}
}

func valid(grid *[]string, loc aoc.Point) bool {
	return loc.Y >= 0 && loc.X >= 0 && loc.Y < len((*grid)) && loc.X < len((*grid)[0])
}

func scan(grid *[]string, loc aoc.Point, m aoc.Point, self bool, max_n int, anodes *map[aoc.Point]bool) {
	if !self {
		loc.Y += m.Y
		loc.X += m.X
	}
	for n := 0; n < max_n && valid(grid, loc); n++ {
		(*anodes)[loc] = true
		loc.Y += m.Y
		loc.X += m.X
	}
}

func solve(input string, self bool, max_n int) int {
	grid := s.Split(s.TrimSpace(string(aoc.Try(os.ReadFile(input)))), "\n")
	locs := make(map[rune][]aoc.Point)
	for y := range grid {
		for x, c := range grid[y] {
			if c != '.' {
				locs[c] = append(locs[c], aoc.Point{Y: y, X: x})
			}
		}
	}
	anodes := make(map[aoc.Point]bool)
	for c := range locs {
		for _, p := range pairs(locs[c]) {
			mpos := slope(p)
			mneg := aoc.Point{Y: -mpos.Y, X: -mpos.X}
			scan(&grid, p[0], mneg, self, max_n, &anodes)
			scan(&grid, p[1], mpos, self, max_n, &anodes)
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
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day8.txt"), 271)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day8.txt"), 994)
}
