package main

import (
	. "aoc/base/aoc"
	"aoc/day1"
	"aoc/day10"
	"aoc/day11"
	"aoc/day13"
	"aoc/day14"
	"aoc/day18"
	"aoc/day19"
	"aoc/day2"
	"aoc/day3"
	"aoc/day4"
	"aoc/day5"
	"aoc/day6"
	"aoc/day7"
	"aoc/day8"
	"aoc/day9"
	"fmt"
	"os"
	"strconv"
)

func main() {
	if len(os.Args) < 2 {
		fmt.Println("usage: go run main.go [DAY_NUMBER]")
		os.Exit(0)
	}

	switch Try(strconv.Atoi(os.Args[1])) {
	case 1:
		day1.Solve()
	case 2:
		day2.Solve()
	case 3:
		day3.Solve()
	case 4:
		day4.Solve()
	case 5:
		day5.Solve()
	case 6:
		day6.Solve()
	case 7:
		day7.Solve()
	case 8:
		day8.Solve()
	case 9:
		day9.Solve()
	case 10:
		day10.Solve()
	case 11:
		day11.Solve()
	case 13:
		day13.Solve()
	case 14:
		day14.Solve()
	case 18:
		day18.Solve()
	case 19:
		day19.Solve()
	default:
		fmt.Println("no solution for day", os.Args[1])
	}
}
