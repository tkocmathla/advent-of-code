package day9

import (
	aoc "aoc/util"
	"container/list"
	"fmt"
	"os"
	"strconv"
	s "strings"
)

type Block struct {
	i  int
	id int
}

var FreeId = -1

func dump(disk *list.List) {
	for e := disk.Front(); e != nil; e = e.Next() {
		if e.Value == FreeId {
			fmt.Print(".")
		} else {
			fmt.Printf("%v|", e.Value.(*Block).id)
		}
	}
	fmt.Println()
}

func find_free(disk *list.List, start *list.Element) *list.Element {
	for e := start; e != nil; e = e.Next() {
		if e.Value.(*Block).id == FreeId {
			return e
		}
	}
	return nil
}

func parse(input string) *list.List {
	data := s.TrimSpace(string(aoc.Try(os.ReadFile(input))))
	disk := list.New()
	index := 0
	for i, c := range data {
		size := aoc.Try(strconv.Atoi(string(c)))
		for j := 0; j < size; j++ {
			if i%2 == 0 {
				disk.PushBack(&Block{i: index, id: i / 2})
			} else {
				disk.PushBack(&Block{i: index, id: FreeId})
			}
			index += 1
		}
	}
	return disk
}

func Part1(input string) int {
	disk := parse(input)
	free := find_free(disk, disk.Front())
	for e := disk.Back(); e != nil; e = e.Prev() {
		blk := e.Value.(*Block)
		if blk.id == FreeId {
			continue
		}
		if free = find_free(disk, free); free.Value.(*Block).i > blk.i {
			break
		}
		free.Value.(*Block).id = blk.id
		blk.id = FreeId
	}

	var sum int
	for e := disk.Front(); e != nil; e = e.Next() {
		blk := e.Value.(*Block)
		if blk.id == FreeId {
			break
		}
		sum += blk.id * blk.i
	}

	return sum
}

//func Part2(input string) int {
//}

func Solve() {
	aoc.AssertEq(aoc.TimeFunc(Part1, "data/day9.txt"), 6356833654075)
	//aoc.AssertEq(aoc.TimeFunc(Part2, "data/day9.txt"), 0)
}
