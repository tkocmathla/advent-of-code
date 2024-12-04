package day4

import (
	aoc "aoc/util"
	"os"
	s "strings"
)

func to_grid(data string) [][]byte {
	var grid [][]byte
	for i, line := range s.Fields(data) {
		grid = append(grid, []byte{})
		for j := range line {
			grid[i] = append(grid[i], line[j])
		}
	}
	return grid
}

var xmas = [4]byte{'X', 'M', 'A', 'S'}

func is_xmas(grid [][]byte, loc aoc.Point, dir aoc.Point) bool {
	ok := true
	for i := 0; i < 4 && ok; i++ {
		y := loc.Y + (dir.Y * i)
		x := loc.X + (dir.X * i)
		in_bounds := y >= 0 && x >= 0 && y < len(grid) && x < len(grid[y])
		ok = ok && in_bounds && grid[loc.Y+(dir.Y*i)][loc.X+(dir.X*i)] == xmas[i]
	}
	return ok
}

func is_x_mas(grid [][]byte, loc aoc.Point) bool {
	in_bounds := loc.Y > 0 && loc.X > 0 && loc.Y < len(grid)-1 && loc.X < len(grid[loc.Y])-1
	if !in_bounds {
		return false
	}
	has_a := grid[loc.Y][loc.X] == 'A'
	has_mas1 := (grid[loc.Y-1][loc.X-1] == 'M' && grid[loc.Y+1][loc.X+1] == 'S') || (grid[loc.Y-1][loc.X-1] == 'S' && grid[loc.Y+1][loc.X+1] == 'M')
	has_mas2 := (grid[loc.Y-1][loc.X+1] == 'M' && grid[loc.Y+1][loc.X-1] == 'S') || (grid[loc.Y-1][loc.X+1] == 'S' && grid[loc.Y+1][loc.X-1] == 'M')
	return has_a && has_mas1 && has_mas2
}

func part1(input string) int {
	data := string(aoc.Try(os.ReadFile(input)))
	grid := to_grid(data)
	xmas := 0
	for y := range grid {
		for x := range grid[y] {
			for _, dir := range aoc.Dirs {
				if is_xmas(grid, aoc.Point{X: x, Y: y}, dir) {
					xmas += 1
				}
			}
		}
	}
	return xmas
}

func part2(input string) int {
	data := string(aoc.Try(os.ReadFile(input)))
	grid := to_grid(data)
	xmas := 0
	for y := range grid {
		for x := range grid[y] {
			if is_x_mas(grid, aoc.Point{X: x, Y: y}) {
				xmas += 1
			}
		}
	}
	return xmas
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(part1, "data/day4.txt"), 2642)
	aoc.AssertEq(aoc.TimeFunc(part2, "data/day4.txt"), 1974)
}
