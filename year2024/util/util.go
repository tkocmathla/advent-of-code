package util

import (
	"time"
	"reflect"
	"runtime"
	"fmt"
)

func Try[T any](value T, e error) T {
	if e != nil {
		panic(e)
	}
	return value
}

// TODO: some return values will be strings
type DayPartFunc func(string) int

func TimeFunc(f DayPartFunc, arg string) {
	rfn := runtime.FuncForPC(reflect.ValueOf(f).Pointer())
	now := time.Now()
	ans := f(arg)
	elapsed := time.Since(now)
	fmt.Printf("%s: %d (%s)\n", rfn.Name(), ans, elapsed)
}
