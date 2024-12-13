package matrix

type Matrix []string

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
