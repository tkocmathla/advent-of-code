package day5

import (
	. "aoc/base/aoc"
	"os"
	"sort"
	"strconv"
	s "strings"
)

type Rules map[string][]string

func parse(raw string) Rules {
	rules := make(Rules)
	for _, pair := range s.Split(raw, "\n") {
		nums := s.Split(pair, "|")
		rules[nums[0]] = append(rules[nums[0]], nums[1])
	}
	return rules
}

func solve(input string, f func(Rules, []string, bool) int) int {
	data := s.Split(string(Try(os.ReadFile(input))), "\n\n")
	rules := parse(data[0])
	mid_sums := 0
	for _, update := range s.Split(s.TrimSpace(data[1]), "\n") {
		sorted := true
		seen := make(map[string]bool)
		nums := s.Split(update, ",")
		for _, num := range nums {
			for _, next := range rules[num] {
				if _, has := seen[next]; has {
					sorted = false
				}
			}
			seen[num] = true
		}
		mid_sums += f(rules, nums, sorted)
	}
	return mid_sums
}

func Part1(input string) int {
	return solve(input, func(rules Rules, nums []string, sorted bool) int {
		if !sorted {
			return 0
		}
		return Try(strconv.Atoi(nums[len(nums)/2]))
	})
}

func Part2(input string) int {
	return solve(input, func(rules Rules, nums []string, sorted bool) int {
		if sorted {
			return 0
		}
		sort.Slice(nums, func(i, j int) bool {
			for _, next := range rules[nums[j]] {
				if nums[i] == next {
					return false
				}
			}
			return true
		})
		return Try(strconv.Atoi(nums[len(nums)/2]))
	})
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day5.txt"), 5651)
	AssertEq(TimeFunc(Part2, "data/day5.txt"), 4743)
}
