package day11

import (
	aoc "aoc/util"
	"container/list"
	"fmt"
	"math"
	"os"
	"strconv"
	s "strings"
)

func dump(stones *list.List) {
	for e := stones.Front(); e != nil; e = e.Next() {
		fmt.Printf("%v ", e.Value)
	}
	fmt.Println()
}

func digits(x int) int {
	n := 0
	for ; x > 0; n, x = n+1, x/10 {
	}
	return n
}

func split(stones *list.List, stone *list.Element) Result {
	x := stone.Value.(int)
	n := digits(x)
	lhs := x
	rhs := 0
	for i := 0; i < n/2; i++ {
		rhs += lhs % 10 * int(math.Pow(10, float64(i)))
		lhs /= 10
	}
	//stones.InsertBefore(lhs, stone)
	//stone.Value = rhs
	return Result{lhs: rhs, rhs: lhs}
}

type Result struct {
	lhs int
	rhs interface{}
}

func transform(stones *list.List, stone *list.Element) Result {
	x := stone.Value.(int)
	if x == 0 {
		return Result{lhs: 1, rhs: nil}
	} else if digits(x)%2 == 0 {
		return split(stones, stone)
	}
	return Result{lhs: x * 2024, rhs: nil}
}

var cache = make(map[int]Result)

func blink(stones *list.List) {
	for stone := stones.Front(); stone != nil; stone = stone.Next() {
		res, has := cache[stone.Value.(int)]
		if !has {
			res = transform(stones, stone)
			cache[stone.Value.(int)] = res
		}
		stone.Value = res.lhs
		if res.rhs != nil {
			stones.InsertBefore(res.rhs, stone)
		}
	}
}

func solve(input string, n int) int {
	stones := list.New()
	for _, x := range s.Fields(string(aoc.Try(os.ReadFile(input)))) {
		stones.PushBack(aoc.Try(strconv.Atoi(x)))
	}
	for i := 0; i < n; i++ {
		fmt.Println(i, stones.Len())
		blink(stones)
	}
	return stones.Len()
}

func Part1(input string) int {
	return solve(input, 25)
}

func Part2(input string) int {
	return solve(input, 75)
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day11.txt"), 204022)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day11.txt"), 0)
}
