package main

import (
	"aoc/day1"
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
	default:
		fmt.Println("no solution for day", os.Args[1])
	}
}
