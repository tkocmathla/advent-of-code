package day4

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
)

func is_xmas(grid Matrix, loc Point, dir Point) bool {
	ok := true
	for i := 0; i < 4 && ok; i++ {
		y := loc.Y + (dir.Y * i)
		x := loc.X + (dir.X * i)
		ok = grid.Valid(Point{Y: y, X: x}) && grid[y][x] == rune("XMAS"[i])
	}
	return ok
}

func is_x_mas(grid Matrix, loc Point) bool {
	if loc.Y > 0 && loc.X > 0 && loc.Y < len(grid)-1 && loc.X < len(grid[loc.Y])-1 {
		has_a := grid[loc.Y][loc.X] == 'A'
		has_mas1 := (grid[loc.Y-1][loc.X-1] == 'M' && grid[loc.Y+1][loc.X+1] == 'S') || (grid[loc.Y-1][loc.X-1] == 'S' && grid[loc.Y+1][loc.X+1] == 'M')
		has_mas2 := (grid[loc.Y-1][loc.X+1] == 'M' && grid[loc.Y+1][loc.X-1] == 'S') || (grid[loc.Y-1][loc.X+1] == 'S' && grid[loc.Y+1][loc.X-1] == 'M')
		return has_a && has_mas1 && has_mas2
	}
	return false
}

func Part1(input string) int {
	grid := NewMatrixFromFile(input)
	xmas := 0
	for y := range grid {
		for x := range grid[y] {
			for _, dir := range Dirs {
				if is_xmas(grid, Point{X: x, Y: y}, dir) {
					xmas += 1
				}
			}
		}
	}
	return xmas
}

func Part2(input string) int {
	grid := NewMatrixFromFile(input)
	xmas := 0
	for y := range grid {
		for x := range grid[y] {
			if is_x_mas(grid, Point{X: x, Y: y}) {
				xmas += 1
			}
		}
	}
	return xmas
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day4.txt"), 2642)
	AssertEq(TimeFunc(Part2, "data/day4.txt"), 1974)
}
