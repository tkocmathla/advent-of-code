package day4

import (
	aoc "aoc/util"
	"os"
	s "strings"
)

type point struct {
	x int // column
	y int // row
}

func to_grid(data string) [][]byte {
	var grid [][]byte
	for i, line := range s.Split(data, "\n") {
		if len(line) == 0 {
			break
		}
		grid = append(grid, []byte{})
		for j := range line {
			grid[i] = append(grid[i], line[j])
		}
	}
	return grid
}

var xmas = [4]byte{'X', 'M', 'A', 'S'}

func is_xmas(grid [][]byte, loc point, dir point) bool {
	ok := true
	for i := 0; i < 4 && ok; i++ {
		y := loc.y + (dir.y * i)
		x := loc.x + (dir.x * i)
		in_bounds := y >= 0 && x >= 0 && y < len(grid) && x < len(grid[y])
		ok = ok && in_bounds && grid[loc.y+(dir.y*i)][loc.x+(dir.x*i)] == xmas[i]
	}
	return ok
}

func is_x_mas(grid [][]byte, loc point) bool {
	in_bounds := loc.y > 0 && loc.x > 0 && loc.y < len(grid)-1 && loc.x < len(grid[loc.y])-1
	if !in_bounds {
		return false
	}
	has_a := grid[loc.y][loc.x] == 'A'
	has_mas1 := (grid[loc.y-1][loc.x-1] == 'M' && grid[loc.y+1][loc.x+1] == 'S') || (grid[loc.y-1][loc.x-1] == 'S' && grid[loc.y+1][loc.x+1] == 'M')
	has_mas2 := (grid[loc.y-1][loc.x+1] == 'M' && grid[loc.y+1][loc.x-1] == 'S') || (grid[loc.y-1][loc.x+1] == 'S' && grid[loc.y+1][loc.x-1] == 'M')
	return has_a && has_mas1 && has_mas2
}

var dirs = [8]point{{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}}

func part1(input string) int {
	data := string(aoc.Try(os.ReadFile(input)))
	grid := to_grid(data)
	xmas := 0
	for y := range grid {
		for x := range grid[y] {
			for _, dir := range dirs {
				if is_xmas(grid, point{x, y}, dir) {
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
			if is_x_mas(grid, point{x, y}) {
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
