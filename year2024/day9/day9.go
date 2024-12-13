package day9

import (
	. "aoc/base/aoc"
	"container/list"
	"fmt"
	"os"
	"strconv"
	s "strings"
)

type Block struct {
	fid int // file id
	n   int // size
}

var FreeId = -1

func dump(disk *list.List) {
	for e := disk.Front(); e != nil; e = e.Next() {
		blk := e.Value.(*Block)
		if blk.fid == FreeId {
			fmt.Printf("{. %d}", blk.n)
		} else {
			fmt.Printf("{%d %d}", blk.fid, blk.n)
		}
	}
	fmt.Println()
}

func parse(input string) *list.List {
	data := s.TrimSpace(string(Try(os.ReadFile(input))))
	disk := list.New()
	index := 0
	for i, c := range data {
		size := Try(strconv.Atoi(string(c)))
		for j := 0; j < size; j++ {
			if i%2 == 0 {
				disk.PushBack(&Block{fid: i / 2, n: 1})
			} else {
				disk.PushBack(&Block{fid: FreeId, n: 1})
			}
			index += 1
		}
	}
	return disk
}

func find_free(disk *list.List, start *list.Element, end *list.Element, size int) *list.Element {
	for e := start; e != end; e = e.Next() {
		if e.Value.(*Block).fid == FreeId && e.Value.(*Block).n >= size {
			return e
		}
	}
	return nil
}

func Part1(input string) int {
	disk := parse(input)
	free := find_free(disk, disk.Front(), disk.Back(), 1)
	for e := disk.Back(); e != nil; e = e.Prev() {
		blk := e.Value.(*Block)
		if blk.fid == FreeId {
			continue
		}
		if free = find_free(disk, free, e, 1); free == nil {
			break
		}
		free.Value.(*Block).fid = blk.fid
		blk.fid = FreeId
	}
	i := 0
	sum := 0
	for e := disk.Front(); e != nil && e.Value.(*Block).fid != FreeId; e = e.Next() {
		sum += e.Value.(*Block).fid * i
		i += 1
	}
	return sum
}

func parse2(input string) *list.List {
	data := s.TrimSpace(string(Try(os.ReadFile(input))))
	disk := list.New()
	index := 0
	for i, c := range data {
		size := Try(strconv.Atoi(string(c)))
		if i%2 == 0 {
			disk.PushBack(&Block{fid: i / 2, n: size})
		} else {
			disk.PushBack(&Block{fid: FreeId, n: size})
		}
		index += 1
	}
	return disk
}

func Part2(input string) int {
	disk := parse2(input)
	var free *list.Element
	for e := disk.Back(); e != nil; e = e.Prev() {
		blk := e.Value.(*Block)
		if blk.fid == FreeId {
			continue
		}
		if free = find_free(disk, disk.Front(), e, blk.n); free == nil {
			continue
		}
		disk.InsertBefore(&Block{fid: blk.fid, n: blk.n}, free)
		blk.fid = FreeId
		if free.Value.(*Block).n > blk.n {
			free.Value.(*Block).n -= blk.n
		} else {
			disk.Remove(free)
		}
	}
	i := 0
	sum := 0
	for e := disk.Front(); e != nil; e = e.Next() {
		blk := e.Value.(*Block)
		for j := 0; j < blk.n; j++ {
			if blk.fid != FreeId {
				sum += blk.fid * i
			}
			i += 1
		}
	}
	return sum
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day9.txt"), 6356833654075)
	AssertEq(TimeFunc(Part2, "data/day9.txt"), 6389911791746)
}
