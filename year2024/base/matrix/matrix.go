package matrix

type Point struct {
	X int // column
	Y int // row
}

func (lhs Point) Add(rhs Point) Point {
	return Point{X: lhs.X + rhs.X, Y: lhs.Y + rhs.Y}
}

func (loc Point) Valid(grid []string) bool {
	return loc.Y >= 0 && loc.X >= 0 && loc.Y < len(grid) && loc.X < len(grid[0])
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
