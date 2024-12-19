package day19

import (
	. "aoc/base/aoc"
	. "aoc/base/moremath"
	"os"
	s "strings"
)

func parse(input string) ([]string, []string) {
	data := s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n\n")
	towels := s.Split(data[0], ", ")
	patterns := s.Split(data[1], "\n")
	return towels, patterns
}

var Cache = make(map[string]int)

func match(towels []string, pattern string) int {
	if found, has := Cache[pattern]; has {
		return found
	}
	if len(pattern) == 0 {
		return 1
	}
	found := 0
	for _, towel := range towels {
		if len(pattern) < len(towel) {
			continue
		}
		if pattern[:len(towel)] == towel {
			found += match(towels, pattern[len(towel):])
		}
	}
	Cache[pattern] = found
	return found
}

func solve(input string) []int {
	towels, patterns := parse(input)
	var counts []int
	for _, pattern := range patterns {
		if count := match(towels, pattern); count > 0 {
			counts = append(counts, count)
		}
	}
	return counts
}

func Part1(input string) int {
	return len(solve(input))
}

func Part2(input string) int {
	return Sum(solve(input)...)
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day19.txt"), 317)
	AssertEq(TimeFunc(Part2, "data/day19.txt"), 883443544805484)
}
