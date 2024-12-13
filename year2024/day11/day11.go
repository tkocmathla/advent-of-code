package day11

import (
	. "aoc/base/aoc"
	"math"
	"os"
	"strconv"
	s "strings"
)

func digits(x int) int {
	n := 0
	for ; x > 0; n, x = n+1, x/10 {
	}
	return n
}

func split(lhs int) []int {
	n := digits(lhs)
	rhs := 0
	for i := 0; i < n/2; i++ {
		rhs += lhs % 10 * int(math.Pow(10, float64(i)))
		lhs /= 10
	}
	return []int{lhs, rhs}
}

func transform(stone int) []int {
	if stone == 0 {
		return []int{1}
	} else if digits(stone)%2 == 0 {
		return split(stone)
	}
	return []int{stone * 2024}
}

type Cache map[Key]int
type Key struct {
	x int // stone value
	n int // remaining blinks
}

func blink(cache *Cache, stone int, n int) int {
	if n <= 0 {
		return 1
	}
	key := Key{x: stone, n: n}
	if count, has := (*cache)[key]; has {
		return count
	}
	count := 0
	for _, x := range transform(stone) {
		count += blink(cache, x, n-1)
	}
	(*cache)[key] = count
	return count
}

func solve(input string, n int) int {
	cache := make(Cache)
	count := 0
	for _, x := range s.Fields(string(Try(os.ReadFile(input)))) {
		stone := Try(strconv.Atoi(x))
		count += blink(&cache, stone, n)
	}
	return count
}

func Part1(input string) int {
	return solve(input, 25)
}

func Part2(input string) int {
	return solve(input, 75)
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day11.txt"), 204022)
	AssertEq(TimeFunc(Part2, "data/day11.txt"), 241651071960597)
}
