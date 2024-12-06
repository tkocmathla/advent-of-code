package util

import (
	"fmt"
	"golang.org/x/exp/constraints"
	"reflect"
	"runtime"
	"time"
)

type Point struct {
	X int // column
	Y int // row
}

func (lhs Point) Add(rhs Point) Point {
	return Point{X: lhs.X + rhs.X, Y: lhs.Y + rhs.Y}
}

var N = Point{Y: -1, X: 0}
var E = Point{Y: 0, X: 1}
var S = Point{Y: 1, X: 0}
var W = Point{Y: 0, X: -1}
var NE = N.Add(E)
var SE = S.Add(E)
var SW = S.Add(W)
var NW = N.Add(W)

var Dirs = [8]Point{N, NE, E, SE, S, SW, W, NW}

// Abs returns |a - b| for integer types.
func Abs[T constraints.Integer](a, b T) T {
	if a < b {
		return b - a
	} else {
		return a - b
	}
}

// Try is a simple wrapper for functions that return a single value and an error result.
func Try[T any](value T, e error) T {
	if e != nil {
		panic(e)
	}
	return value
}

// AssertEq panics if expect != actual.
func AssertEq[T comparable](expect, actual T) {
	if expect != actual {
		panic(fmt.Sprintf("Assert failed: %v == %v\n", expect, actual))
	}
}

// DayPartFunc is a function to solve a day part that takes the path to the input file and returns the solution.
type DayPartFunc[T any] func(string) T

// TimeFunc wraps the given function to print the result formatted with the function name, result, and elapsed time.
func TimeFunc[T any](f DayPartFunc[T], arg string) T {
	name := runtime.FuncForPC(reflect.ValueOf(f).Pointer()).Name()
	now := time.Now()
	ans := f(arg)
	elapsed := time.Since(now)
	fmt.Printf("%s: %v (%s)\n", name, ans, elapsed)
	return ans
}
