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

var start = aoc.Point{Y: 45, X: 42}

func Part1(input string) int {
	grid := s.Fields(string(aoc.Try(os.ReadFile(input))))
	guard := Guard{loc: start, dir: aoc.N}
	seen := make(map[aoc.Point]bool)
	for seen[guard.loc] = true; in_bounds(grid, guard.loc); step(grid, &guard) {
		seen[guard.loc] = true
	}
	return len(seen)
}

func Part2(input string) int {
	grid := s.Fields(string(aoc.Try(os.ReadFile(input))))
	loops := 0
	for y := range grid {
		for x := range grid[y] {
			if (aoc.Point{Y: y, X: x} == start) || grid[y][x] == '#' {
				continue
			}
			orig := grid[y]
			grid[y] = grid[y][:x] + "#" + grid[y][x+1:]
			guard := Guard{loc: start, dir: aoc.N}
			seen := make(map[Guard]bool)
			for ; in_bounds(grid, guard.loc); step(grid, &guard) {
				if _, has := seen[guard]; has {
					loops += 1
					break
				}
				seen[guard] = true
			}
			grid[y] = orig
		}
	}
	return loops
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day6.txt"), 4454)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day6.txt"), 1503)
}
