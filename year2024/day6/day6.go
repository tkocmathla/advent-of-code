package day6

import (
	aoc "aoc/util"
	"os"
	s "strings"
)

type Guard struct {
	loc aoc.Point
	dir aoc.Point
}

func in_bounds(grid []string, loc aoc.Point) bool {
	return loc.Y >= 0 && loc.X >= 0 && loc.Y < len(grid) && loc.X < len(grid[0])
}

func step(grid []string, guard *Guard) {
	next := aoc.Point{Y: guard.loc.Y + guard.dir.Y, X: guard.loc.X + guard.dir.X}
	if in_bounds(grid, next) && grid[next.Y][next.X] == '#' {
		switch guard.dir {
		case aoc.N:
			guard.dir = aoc.E
		case aoc.E:
			guard.dir = aoc.S
		case aoc.S:
			guard.dir = aoc.W
		case aoc.W:
			guard.dir = aoc.N
		}
		step(grid, guard)
	} else {
		guard.loc = next
	}
}

func Part1(input string) int {
	grid := s.Fields(string(aoc.Try(os.ReadFile(input))))
	guard := Guard{loc: aoc.Point{Y: 45, X: 42}, dir: aoc.N}
	seen := make(map[aoc.Point]bool)
	for ; in_bounds(grid, guard.loc); step(grid, &guard) {
		seen[guard.loc] = true
	}
	return len(seen)
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day6.txt"), 4454)
}
