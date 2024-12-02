package day2

import (
	aoc "aoc/util"
	"bufio"
	"os"
	"strconv"
	s "strings"
)

func strToInt(strs []string) []int {
	var ints []int
	for _, x := range strs {
		ints = append(ints, aoc.Try(strconv.Atoi(x)))
	}
	return ints
}

func monotonic(xs []int) bool {
	inc := true
	dec := true
	last := xs[0]
	for _, x := range xs[1:] {
		inc = inc && x > last
		dec = dec && x < last
		last = x
	}
	return inc || dec
}

func gradual(xs []int) bool {
	last := xs[0]
	for _, x := range xs[1:] {
		diff := aoc.Abs(x, last)
		if diff < 1 || diff > 3 {
			return false
		}
		last = x

	}
	return true
}

func safeWithReplacement(xs []int) bool {
	if monotonic(xs) && gradual(xs) {
		return true
	}
	for i := range xs {
		ys := append([]int{}, xs[:i]...)
		ys = append(ys, xs[i+1:]...)
		if monotonic(ys) && gradual(ys) {
			return true
		}
	}
	return false
}

func solve(input string, pred func([]int) bool) int {
	file := aoc.Try(os.Open(input))

	sum := 0
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		levels := strToInt(s.Fields(scanner.Text()))
		if pred(levels) {
			sum += 1
		}
	}
	return sum
}

func part1(input string) int {
	return solve(input, func(xs []int) bool { return monotonic(xs) && gradual(xs) })
}

func part2(input string) int {
	return solve(input, safeWithReplacement)
}

func Solve() {
	aoc.TimeFunc(part1, "data/day2.txt") // 631
	aoc.TimeFunc(part2, "data/day2.txt") // 665
}
