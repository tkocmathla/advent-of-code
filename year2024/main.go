package main

import (
	"aoc/day1"
	"aoc/day10"
	"aoc/day11"
	"aoc/day2"
	"aoc/day3"
	"aoc/day4"
	"aoc/day5"
	"aoc/day6"
	"aoc/day7"
	"aoc/day8"
	"aoc/day9"
	aoc "aoc/util"
	"fmt"
	"os"
	"strconv"
)

func main() {
	if len(os.Args) < 2 {
		fmt.Println("usage: go run main.go [DAY_NUMBER]")
		os.Exit(0)
	}

	switch aoc.Try(strconv.Atoi(os.Args[1])) {
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
	default:
		fmt.Println("no solution for day", os.Args[1])
	}
}
