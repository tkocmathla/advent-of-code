package matrix

import (
	. "aoc/base/aoc"
	"fmt"
	"os"
	s "strings"
)

type Matrix []string

func NewMatrix(file string) Matrix {
	return s.Split(s.TrimSpace(string(Try(os.ReadFile(file)))), "\n")
}

type MutableMatrix [][]rune

func NewMutableMatrix(w int, h int, init rune) MutableMatrix {
	var m MutableMatrix
	for i := 0; i < h; i++ {
		var row []rune
		for j := 0; j < w; j++ {
			row = append(row, init)
		}
		m = append(m, row)
	}
	return m
}

func NewMutableMatrixFromString(data string) MutableMatrix {
	var mm MutableMatrix
	for _, line := range s.Split(s.TrimSpace(data), "\n") {
		var row []rune
		for _, char := range line {
			row = append(row, char)
		}
		mm = append(mm, row)
	}
	return mm
}

func NewMutableMatrixFromFile(file string) MutableMatrix {
	return NewMutableMatrixFromString(string(Try(os.ReadFile(file))))
}

func (mm MutableMatrix) Print() {
	for _, row := range mm {
		fmt.Println(string(row))
	}
}

func (mm MutableMatrix) Valid(p Point) bool {
	return p.Y >= 0 && p.X >= 0 && p.Y < len(mm) && p.X < len(mm[0])
}

func (mm MutableMatrix) ValidIf(p Point, f func(MutableMatrix, Point) bool) bool {
	return mm.Valid(p) && f(mm, p)
}

func (mm MutableMatrix) Get(p Point) rune {
	return mm[p.Y][p.X]
}

func (m Matrix) Get(p Point) byte {
	return m[p.Y][p.X]
}

type Point struct {
	X int // Column
	Y int // Row
}

func (p1 Point) Add(p2 Point) Point {
	return Point{X: p1.X + p2.X, Y: p1.Y + p2.Y}
}

func (p Point) Valid(m Matrix) bool {
	return p.Y >= 0 && p.X >= 0 && p.Y < len(m) && p.X < len(m[0])
}

func (p Point) ValidIf(m Matrix, f func(Matrix, Point) bool) bool {
	return p.Valid(m) && f(m, p)
}

var N = Point{0, -1}
var E = Point{1, 0}
var S = Point{0, 1}
var W = Point{-1, 0}
var NE = N.Add(E)
var SE = S.Add(E)
var SW = S.Add(W)
var NW = N.Add(W)

var Dirs = [8]Point{N, NE, E, SE, S, SW, W, NW}
var OrthoDirs = [4]Point{N, E, S, W}
