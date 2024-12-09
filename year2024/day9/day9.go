package day9

import (
	aoc "aoc/util"
	"os"
	"strconv"
	s "strings"
)

var FreeId = -1

func parse(input string) []int {
	data := s.TrimSpace(string(aoc.Try(os.ReadFile(input))))
	var disk []int
	for i, c := range data {
		size := aoc.Try(strconv.Atoi(string(c)))
		for j := 0; j < size; j++ {
			if i%2 == 0 {
				disk = append(disk, i/2)
			} else {
				disk = append(disk, FreeId)
			}
		}
	}
	return disk
}

func find_free(disk *[]int, i int) int {
	for ; i < len(*disk); i++ {
		if (*disk)[i] == FreeId {
			return i
		}
	}
	return -1
}

func Part1(input string) int {
	disk := parse(input)
	free := find_free(&disk, 0)
	for i := len(disk) - 1; i >= 0; i-- {
		if disk[i] == FreeId {
			continue
		}
		if free = find_free(&disk, free); free > i {
			break
		}
		disk[free], disk[i] = disk[i], disk[free]
	}
	sum := 0
	for i, fid := range disk {
		if fid != FreeId {
			sum += i * fid
		}
	}
	return sum
}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day9.txt"), 6356833654075)
}
