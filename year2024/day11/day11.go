package day11

import (
	aoc "aoc/util"
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

func split(stone int) Result {
	n := digits(stone)
	lhs := stone
	rhs := 0
	for i := 0; i < n/2; i++ {
		rhs += lhs % 10 * int(math.Pow(10, float64(i)))
		lhs /= 10
	}
	return Result{lhs: rhs, rhs: lhs}
}

type Result struct {
	lhs int
	rhs interface{}
}

func transform(stone int) Result {
	if stone == 0 {
		return Result{lhs: 1, rhs: nil}
	} else if digits(stone)%2 == 0 {
		return split(stone)
	}
	return Result{lhs: stone * 2024, rhs: nil}
}

type CacheKey struct {
	x int // stone value
	n int // remaining blinks
}

type Cache map[CacheKey]int

func blink(cache *Cache, stone int, n int) int {
	if n <= 0 {
		return 1
	}
	key := CacheKey{x: stone, n: n}
	if count, has := (*cache)[key]; has {
		return count
	}
	res := transform(stone)
	count := blink(cache, res.lhs, n-1)
	if res.rhs != nil {
		count += blink(cache, res.rhs.(int), n-1)
	}
	(*cache)[key] = count
	return count
}

func solve(input string, n int) int {
	cache := make(Cache)
	count := 0
	for _, x := range s.Fields(string(aoc.Try(os.ReadFile(input)))) {
		stone := aoc.Try(strconv.Atoi(x))
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
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day11.txt"), 204022)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day11.txt"), 241651071960597)
}
