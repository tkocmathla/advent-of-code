package util

import (
	"fmt"
	"reflect"
	"runtime"
	"time"
)

// Try is a simple wrapper for functions that return a single value and an error result.
func Try[T any](value T, e error) T {
	if e != nil {
		panic(e)
	}
	return value
}

// DayPartFunc is a function to solve a day part that takes the path to the input file and returns the solution.
type DayPartFunc[T any] func(string) T

// TimeFunc runs the given function and prints the result formatted with the function name, result, and elapsed time.
func TimeFunc[T any](f DayPartFunc[T], arg string) {
	rfn := runtime.FuncForPC(reflect.ValueOf(f).Pointer())
	now := time.Now()
	ans := f(arg)
	elapsed := time.Since(now)
	fmt.Printf("%s: %v (%s)\n", rfn.Name(), ans, elapsed)
}
