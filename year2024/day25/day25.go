package day25

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
	"os"
	s "strings"
)

func parse(input string) ([][]int, [][]int) {
	var locks [][]int
	var keys [][]int
	schematics := s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n\n")
	for _, raw := range schematics {
		m := NewMatrixFromString(raw)
		var schematic []int
		for x := range m[0] {
			var height int
			for y := range m {
				if m[y][x] == '#' {
					height += 1
				}
			}
			schematic = append(schematic, height-1)
		}
		if m[0][0] == '#' {
			locks = append(locks, schematic)
		} else {
			keys = append(keys, schematic)
		}
	}
	return locks, keys
}

func Part1(input string) int {
	locks, keys := parse(input)
	fits := 0
	for _, key := range keys {
		for _, lock := range locks {
			fit := true
			for i := 0; i < 5; i++ {
				if key[i]+lock[i] > 5 {
					fit = false
					break
				}
			}
			if fit {
				fits += 1
			}
		}
	}
	return fits
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day25.txt"), 3107)
}
