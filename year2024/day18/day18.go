package day18

import (
	. "aoc/base/aoc"
	. "aoc/base/matrix"
	"fmt"
	"os"
	s "strings"
)

func valid(mm MutableMatrix, p Point) bool {
	return mm.Get(p) != '#'
}

func shortest(mem MutableMatrix) int {
	start := Point{Y: 0, X: 0}
	dist := make(map[Point]int)
	for y := range mem {
		for x, b := range mem[y] {
			if b == '#' {
				continue
			}
			dist[Point{Y: y, X: x}] = 9999
		}
	}
	dist[start] = 0

	for q := []Point{start}; len(q) > 0; {
		u, tail := q[0], q[1:]
		q = tail
		for _, dydx := range OrthoDirs {
			if v := u.Add(dydx); mem.ValidIf(v, valid) {
				if dist[u]+1 < dist[v] {
					dist[v] = dist[u] + 1
					q = append(q, v)
				}
			}
		}
	}

	u := Point{Y: len(mem) - 1, X: len(mem[0]) - 1}
	return dist[u]
}

func parse(input string) []Point {
	var bytes []Point
	lines := s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n")
	for _, line := range lines {
		var b Point
		fmt.Sscanf(line, "%d,%d", &b.X, &b.Y)
		bytes = append(bytes, b)
	}
	return bytes
}

func setup(input string) ([]Point, MutableMatrix) {
	w, h, n := 71, 71, 1024
	mem := NewMutableMatrix(w, h, '.')
	bytes := parse(input)
	for _, b := range bytes[:n] {
		mem[b.Y][b.X] = '#'
	}
	return bytes, mem
}

func Part1(input string) int {
	_, mem := setup(input)
	return shortest(mem)
}

func Part2(input string) string {
	bytes, mem := setup(input)
	for _, b := range bytes[1024:] {
		mem[b.Y][b.X] = '#'
		if shortest(mem) == 9999 {
			return fmt.Sprintf("%d,%d", b.X, b.Y)
		}
	}
	return "fail"
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day18.txt"), 260)
	AssertEq(Part2("data/day18.txt"), "24,48")
}
