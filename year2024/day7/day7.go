package day7

import (
	aoc "aoc/util"
	"fmt"
	"os"
	"regexp"
	"strconv"
	s "strings"
)

type Equation struct {
	val int
	x   int
	xs  []int
}

type Op func(a, b int) int

var Add = func(a, b int) int { return a + b }
var Mul = func(a, b int) int { return a * b }
var Cat = func(a, b int) int { return aoc.Try(strconv.Atoi(fmt.Sprintf("%d%d", a, b))) }

var re = regexp.MustCompile(`(\d+):((?: \d+)+)`)

func parse(input string) []Equation {
	var eqs []Equation
	data := string(aoc.Try(os.ReadFile(input)))
	for _, m := range re.FindAllStringSubmatch(data, -1) {
		xs := s.Fields(m[2])
		eq := Equation{val: aoc.Try(strconv.Atoi(m[1])), x: aoc.Try(strconv.Atoi(xs[0]))}
		for _, x := range xs[1:] {
			eq.xs = append(eq.xs, aoc.Try(strconv.Atoi(x)))
		}
		eqs = append(eqs, eq)
	}
	return eqs
}

func eval_op(eq Equation, ops []Op, op Op) bool {
	if len(eq.xs) == 0 {
		return eq.x == eq.val
	}
	eq.x = op(eq.x, eq.xs[0])
	eq.xs = eq.xs[1:]
	for _, op := range ops {
		if eval_op(eq, ops, op) {
			return true
		}
	}
	return false
}

func solve(input string, ops []Op) int {
	sum := 0
	for _, eq := range parse(input) {
		for _, op := range ops {
			if eval_op(eq, ops, op) {
				sum += eq.val
				break
			}
		}
	}
	return sum
}

func Part1(input string) int {
	return solve(input, []Op{Add, Mul})
}

func Part2(input string) int {
	return solve(input, []Op{Add, Mul, Cat})
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day7.txt"), 303876485655)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day7.txt"), 146111650210682)
}
