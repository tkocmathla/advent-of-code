package day13

import (
	aoc "aoc/util"
	"fmt"
	"os"
	s "strings"
)

type Machine struct {
	ax int
	ay int
	bx int
	by int
	px int
	py int
}

var format = `Button A: X+%d, Y+%d
Button B: X+%d, Y+%d
Prize: X=%d, Y=%d`

func parse(input string, offset int) []Machine {
	var machines []Machine
	groups := s.Split(s.TrimSpace(string(aoc.Try(os.ReadFile(input)))), "\n\n")
	for _, group := range groups {
		var m Machine
		fmt.Sscanf(group, format, &m.ax, &m.ay, &m.bx, &m.by, &m.px, &m.py)
		m.px, m.py = m.px+offset, m.py+offset
		machines = append(machines, m)
	}
	return machines
}

func score(m Machine) int {
	anum := m.bx*m.py - m.by*m.px
	aden := m.bx*m.ay - m.by*m.ax
	if anum%aden == 0 {
		a := anum / aden
		bnum := m.px - m.ax*a
		bden := m.bx
		if bnum%bden == 0 {
			b := bnum / bden
			return 3*a + b
		}
	}
	return 0
}

func solve(input string, offset int) int {
	ans := 0
	for _, m := range parse(input, offset) {
		ans += score(m)
	}
	return ans
}

func Part1(input string) int {
	return solve(input, 0)
}

func Part2(input string) int {
	return solve(input, 10000000000000)
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day13.txt"), 29436)
	aoc.AssertEq(aoc.TimeFunc(Part2, "data/day13.txt"), 103729094227877)
}
