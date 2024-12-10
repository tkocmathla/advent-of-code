package day10

import (
	aoc "aoc/util"
	"os"
	s "strings"
)

func valid(grid []string, loc aoc.Point) bool {
	return loc.Y >= 0 && loc.X >= 0 && loc.Y < len(grid) && loc.X < len(grid[0])
}

func gradual(grid []string, a aoc.Point, b aoc.Point) bool {
	return valid(grid, b) && int(grid[b.Y][b.X]-grid[a.Y][a.X]) == 1
}

func nines(grid []string, loc aoc.Point) []aoc.Point {
	if grid[loc.Y][loc.X] == '9' {
		return []aoc.Point{loc}
	}
	n := loc.Add(aoc.N)
	e := loc.Add(aoc.E)
	s := loc.Add(aoc.S)
	w := loc.Add(aoc.W)
	var all []aoc.Point
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
	grid := s.Split(s.TrimSpace(string(aoc.Try(os.ReadFile(input)))), "\n")
	scores := 0
	for y := range grid {
		for x, c := range grid[y] {
			if c == '0' {
				result := nines(grid, aoc.Point{Y: y, X: x})
				if all {
					scores += len(result)
				} else {
					unique := make(map[aoc.Point]bool)
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
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day10.txt"), 652)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day10.txt"), 1432)
}
