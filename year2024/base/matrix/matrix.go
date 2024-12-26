package matrix

import (
	. "aoc/base/aoc"
	"fmt"
	"os"
	s "strings"
)

// Matrix is a row-major 2D slice of runes.
type Matrix [][]rune

// NewMatrix returns a new `w`x`h` Matrix with all cells set to `init`.
func NewMatrix(w int, h int, init rune) Matrix {
	var m Matrix
	for i := 0; i < h; i++ {
		var row []rune
		for j := 0; j < w; j++ {
			row = append(row, init)
		}
		m = append(m, row)
	}
	return m
}

// NewMatrixFromString returns a new Matrix parsed from the given string.
func NewMatrixFromString(data string) Matrix {
	var m Matrix
	for _, line := range s.Split(s.TrimSpace(data), "\n") {
		var row []rune
		for _, char := range line {
			row = append(row, char)
		}
		m = append(m, row)
	}
	return m
}

// NewMatrixFromFile returns a new Matrix parsed from the given file.
func NewMatrixFromFile(file string) Matrix {
	return NewMatrixFromString(string(Try(os.ReadFile(file))))
}

// Print dumps the matrix to stdout.
func (m Matrix) Print() {
	for _, row := range m {
		fmt.Println(string(row))
	}
}

// Valid returns true if the given Point is within the bounds of the Matrix.
func (m Matrix) Valid(p Point) bool {
	return p.Y >= 0 && p.X >= 0 && p.Y < len(m) && p.X < len(m[0])
}

// ValidIf returns true if the given Point is Valid and f(m, p) returns true.
func (m Matrix) ValidIf(p Point, f func(Matrix, Point) bool) bool {
	return m.Valid(p) && f(m, p)
}

// Get returns the rune at the given Point.
func (m Matrix) Get(p Point) rune {
	return m[p.Y][p.X]
}

// ===--------------------------------------------------------------------=== //

// Point is a 2D coordinate.
type Point struct {
	X int // Column
	Y int // Row
}

// Add returns a new Point that is the sum of `p1` and `p2`.
func (p1 Point) Add(p2 Point) Point {
	return Point{X: p1.X + p2.X, Y: p1.Y + p2.Y}
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
