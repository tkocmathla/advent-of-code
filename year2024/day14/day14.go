package day14

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
	"aoc/base/set"
	"fmt"
	"os"
	s "strings"
)

type Robot struct {
	p Point
	v Point
}

func render(robots []*Robot, w int, h int) {
	locs := set.New()
	for _, r := range robots {
		locs.Add(r.p)
	}
	for y := 0; y < h; y++ {
		for x := 0; x < w; x++ {
			if locs.Has(Point{X: x, Y: y}) {
				fmt.Print("#")
			} else {
				fmt.Print(".")
			}
		}
		fmt.Println()
	}
	fmt.Println()
}

func parse(input string) []*Robot {
	var robots []*Robot
	for _, line := range s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n") {
		var r Robot
		fmt.Sscanf(line, "p=%d,%d v=%d,%d", &r.p.X, &r.p.Y, &r.v.X, &r.v.Y)
		robots = append(robots, &r)
	}
	return robots
}

func move(r *Robot, w int, h int, t int) {
	r.p.X = (r.p.X + r.v.X*t) % w
	r.p.Y = (r.p.Y + r.v.Y*t) % h
	if r.p.X < 0 {
		r.p.X += w
	}
	if r.p.Y < 0 {
		r.p.Y += h
	}
}

func quadrant(r Robot, w int, h int) int {
	if r.p.X < w/2 && r.p.Y < h/2 {
		return 0
	} else if r.p.X > w/2 && r.p.Y < h/2 {
		return 1
	} else if r.p.X < w/2 && r.p.Y > h/2 {
		return 2
	} else if r.p.X > w/2 && r.p.Y > h/2 {
		return 3
	}
	return -1
}

func Part1(input string) int {
	w, h, t := 101, 103, 100
	var scores [4]int
	for _, r := range parse(input) {
		move(r, w, h, t)
		if q := quadrant(*r, w, h); q >= 0 {
			scores[quadrant(*r, w, h)] += 1
		}
	}
	return scores[0] * scores[1] * scores[2] * scores[3]
}

func Part2(input string) int {
	w, h, t := 101, 103, 0
	for {
		robots := parse(input)
		uniq := set.New()
		for _, r := range robots {
			move(r, w, h, t)
			uniq.Add(r.p)
		}
		if len(uniq) == len(robots) {
			render(robots, w, h)
			break
		}
		t += 1
	}
	return t
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day14.txt"), 221142636)
	AssertEq(TimeFunc(Part2, "data/day14.txt"), 7916)
}
