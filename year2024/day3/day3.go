package day3

import (
	. "aoc/base/aoc"
	"os"
	"regexp"
	"strconv"
)

var re = regexp.MustCompile(`mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)`)

func solve(input string, allow_cond bool) int {
	data := string(Try(os.ReadFile(input)))
	enabled := true
	prod := 0
	for _, match := range re.FindAllStringSubmatch(data, -1) {
		if match[0] == "don't()" {
			enabled = false
		} else if match[0] == "do()" {
			enabled = true
		} else if match[0][:3] == "mul" {
			if !allow_cond || enabled {
				prod += Try(strconv.Atoi(match[1])) * Try(strconv.Atoi(match[2]))
			}
		}
	}
	return prod
}

func part1(input string) int {
	return solve(input, false)
}

func part2(input string) int {
	return solve(input, true)
}

func Solve() {
	AssertEq(TimeFunc(part1, "data/day3.txt"), 189527826)
	AssertEq(TimeFunc(part2, "data/day3.txt"), 63013756)
}
