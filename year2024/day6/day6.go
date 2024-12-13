package day6

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
	"os"
	s "strings"
	"sync"
	"sync/atomic"
)

type Guard struct {
	loc Point
	dir Point
}

var start = Point{Y: 45, X: 42}

func valid(grid *[]string, loc Point) bool {
	return loc.Y >= 0 && loc.X >= 0 && loc.Y < len(*grid) && loc.X < len((*grid)[0])
}

func step(grid *[]string, guard *Guard, x, y int) {
	next := Point{Y: guard.loc.Y + guard.dir.Y, X: guard.loc.X + guard.dir.X}
	if valid(grid, next) && ((*grid)[next.Y][next.X] == '#' || (next.Y == y && next.X == x)) {
		switch guard.dir {
		case N:
			guard.dir = E
		case E:
			guard.dir = S
		case S:
			guard.dir = W
		case W:
			guard.dir = N
		}
		step(grid, guard, x, y)
	} else {
		guard.loc = next
	}
}

func walk(grid []string, loc Point) map[Point]bool {
	guard := Guard{loc: loc, dir: N}
	seen := make(map[Point]bool, 5000)
	for seen[guard.loc] = true; valid(&grid, guard.loc); step(&grid, &guard, -1, -1) {
		seen[guard.loc] = true
	}
	return seen
}

func cyclic(grid *[]string, x, y int) bool {
	if (Point{Y: y, X: x} == start) || (*grid)[y][x] == '#' {
		return false
	}
	guard := Guard{loc: start, dir: N}
	seen := make(map[Guard]bool, 5000)
	for ; valid(grid, guard.loc); step(grid, &guard, x, y) {
		if _, has := seen[guard]; has {
			return true
		}
		seen[guard] = true
	}
	return false
}

func Part1(input string) int {
	grid := s.Fields(string(Try(os.ReadFile(input))))
	return len(walk(grid, start))
}

func Part2(input string) int32 {
	var loops int32
	var wg sync.WaitGroup
	grid := s.Fields(string(Try(os.ReadFile(input))))
	seen := walk(grid, start)
	for loc := range seen {
		wg.Add(1)
		go func(loc Point) {
			defer wg.Done()
			if cyclic(&grid, loc.X, loc.Y) {
				atomic.AddInt32(&loops, 1)
			}
		}(loc)
	}
	wg.Wait()
	return loops
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day6.txt"), 4454)
	AssertEq(TimeFunc(Part2, "data/day6.txt"), 1503)
}
