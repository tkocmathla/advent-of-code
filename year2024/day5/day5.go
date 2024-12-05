package day5

import (
	aoc "aoc/util"
	"os"
	"sort"
	"strconv"
	s "strings"
)

type Rules map[string][]string

func parseRules(raw string) Rules {
	rules := make(Rules)
	for _, pair := range s.Split(raw, "\n") {
		nums := s.Split(pair, "|")
		rules[nums[0]] = append(rules[nums[0]], nums[1])
	}
	return rules
}

func Part1(input string) int {
	data := s.Split(string(aoc.Try(os.ReadFile(input))), "\n\n")
	rules := parseRules(data[0])

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
		if sorted {
			mid_sums += aoc.Try(strconv.Atoi(nums[len(nums)/2]))
		}
	}
	return mid_sums
}

func Part2(input string) int {
	data := s.Split(string(aoc.Try(os.ReadFile(input))), "\n\n")
	rules := parseRules(data[0])

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
		if !sorted {
			sort.Slice(nums, func(i, j int) bool {
				for _, next := range rules[nums[j]] {
					if nums[i] == next {
						return false
					}
				}
				return true
			})
			mid_sums += aoc.Try(strconv.Atoi(nums[len(nums)/2]))
		}
	}
	return mid_sums
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day5.txt"), 5651)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day5.txt"), 4743)
}
