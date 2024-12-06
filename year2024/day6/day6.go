package day6

import (
	aoc "aoc/util"
	"os"
	s "strings"
	"sync"
	"sync/atomic"
)

type Guard struct {
	loc aoc.Point
	dir aoc.Point
}

var start = aoc.Point{Y: 45, X: 42}

func in_bounds(grid *[]string, loc aoc.Point) bool {
	return loc.Y >= 0 && loc.X >= 0 && loc.Y < len(*grid) && loc.X < len((*grid)[0])
}

func step(grid *[]string, guard *Guard) {
	next := aoc.Point{Y: guard.loc.Y + guard.dir.Y, X: guard.loc.X + guard.dir.X}
	if in_bounds(grid, next) && (*grid)[next.Y][next.X] == '#' {
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

func walk(grid []string, loc aoc.Point) map[aoc.Point]bool {
	guard := Guard{loc: loc, dir: aoc.N}
	seen := make(map[aoc.Point]bool)
	for seen[guard.loc] = true; in_bounds(&grid, guard.loc); step(&grid, &guard) {
		seen[guard.loc] = true
	}
	return seen
}

func Part1(input string) int {
	grid := s.Fields(string(aoc.Try(os.ReadFile(input))))
	return len(walk(grid, start))
}

func hasCycle(orig_grid *[]string, x, y int) bool {
	if (aoc.Point{Y: y, X: x} == start) || (*orig_grid)[y][x] == '#' {
		return false
	}
	local_grid := make([]string, len(*orig_grid))
	copy(local_grid, *orig_grid)
	local_grid[y] = local_grid[y][:x] + "#" + local_grid[y][x+1:]

	guard := Guard{loc: start, dir: aoc.N}
	seen := make(map[Guard]bool)
	for ; in_bounds(&local_grid, guard.loc); step(&local_grid, &guard) {
		if _, has := seen[guard]; has {
			return true
		}
		seen[guard] = true
	}
	return false
}

func Part2(input string) int32 {
	var loops int32
	var wg sync.WaitGroup
	grid := s.Fields(string(aoc.Try(os.ReadFile(input))))
	seen := walk(grid, start)
	for loc := range seen {
		wg.Add(1)
		go func(loc aoc.Point) {
			defer wg.Done()
			if hasCycle(&grid, loc.X, loc.Y) {
				atomic.AddInt32(&loops, 1)
			}
		}(loc)
	}
	wg.Wait()
	return loops
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day6.txt"), 4454)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day6.txt"), 1503)
}
