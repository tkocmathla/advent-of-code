package day10

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
	"os"
	s "strings"
)

func gradual(grid Matrix, from Point, to Point) bool {
	return to.Valid(grid) && int(grid[to.Y][to.X]-grid[from.Y][from.X]) == 1
}

func nines(grid Matrix, loc Point) []Point {
	if grid[loc.Y][loc.X] == '9' {
		return []Point{loc}
	}
	n := loc.Add(N)
	e := loc.Add(E)
	s := loc.Add(S)
	w := loc.Add(W)
	var all []Point
	if gradual(grid, loc, n) {
		all = append(all, nines(grid, n)...)
	}
	if gradual(grid, loc, e) {
		all = append(all, nines(grid, e)...)
	}
	if gradual(grid, loc, s) {
		all = append(all, nines(grid, s)...)
	}
	if gradual(grid, loc, w) {
		all = append(all, nines(grid, w)...)
	}
	return all
}

func solve(input string, all bool) int {
	grid := s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n")
	scores := 0
	for y := range grid {
		for x, c := range grid[y] {
			if c == '0' {
				result := nines(grid, Point{Y: y, X: x})
				if all {
					scores += len(result)
				} else {
					unique := make(map[Point]bool)
					for _, r := range result {
						unique[r] = true
					}
					scores += len(unique)
				}
			}
		}
	}
	return scores
}

func Part1(input string) int {
	return solve(input, false)
}

func Part2(input string) int {
	return solve(input, true)
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day10.txt"), 652)
	AssertEq(TimeFunc(Part2, "data/day10.txt"), 1432)
}
