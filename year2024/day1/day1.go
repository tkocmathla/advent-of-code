package day1

import (
	. "aoc/base/aoc"
	. "aoc/base/moremath"
	"bufio"
	"os"
	"sort"
	"strconv"
	s "strings"
)

func part1(input string) int {
	file := Try(os.Open(input))

	var l, r []int
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		xs := s.Fields(scanner.Text())
		l = append(l, Try(strconv.Atoi(xs[0])))
		r = append(r, Try(strconv.Atoi(xs[1])))
	}
	sort.Ints(l)
	sort.Ints(r)

	sum := 0
	for i, x := range l {
		sum += Abs(x - r[i])
	}
	return sum
}

func part2(input string) int {
	file := Try(os.Open(input))

	var l []int
	r := make(map[int]int)
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		xs := s.Fields(scanner.Text())
		l = append(l, Try(strconv.Atoi(xs[0])))
		r[Try(strconv.Atoi(xs[1]))] += 1
	}
	sum := 0
	for _, x := range l {
		sum += x * r[x]
	}
	return sum
}

func Solve() {
	AssertEq(TimeFunc(part1, "data/day1.txt"), 1320851)
	AssertEq(TimeFunc(part2, "data/day1.txt"), 26859182)
}
