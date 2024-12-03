package main

import (
	"aoc/day1"
	"aoc/day2"
	"aoc/day3"
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
	default:
		fmt.Println("no solution for day", os.Args[1])
	}
}
