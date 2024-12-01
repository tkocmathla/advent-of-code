package day1

import (
	aoc "aoc/util"
	"bufio"
	"os"
	"sort"
	"strconv"
	s "strings"
)

func part1(input string) int {
	file := aoc.Try(os.Open(input))
	defer file.Close()

	var l, r []int
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		xs := s.Fields(scanner.Text())
		l = append(l, aoc.Try(strconv.Atoi(xs[0])))
		r = append(r, aoc.Try(strconv.Atoi(xs[1])))
	}
	sort.Ints(l)
	sort.Ints(r)

	sum := 0
	for i, x := range l {
		y := r[i]
		sum += aoc.Abs(x, y)
	}
	return sum
}

func part2(input string) int {
	file := aoc.Try(os.Open(input))
	defer file.Close()

	var l []int
	r := make(map[int]int)
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		xs := s.Fields(scanner.Text())
		l = append(l, aoc.Try(strconv.Atoi(xs[0])))
		r[aoc.Try(strconv.Atoi(xs[1]))] += 1
	}
	sum := 0
	for _, x := range l {
		sum += x * r[x]
	}
	return sum
}

func Solve() {
	aoc.TimeFunc(part1, "data/day1.txt") // 1320851
	aoc.TimeFunc(part2, "data/day1.txt") // 26859182
}
