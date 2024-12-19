package moremath

import (
	"golang.org/x/exp/constraints"
	"math"
)

// Abs returns |a| for integer types.
func Abs[T constraints.Integer](x T) T {
	if x < 0 {
		return -x
	}
	return x
}

// Pow returns x^exp for integer types.
func Pow[T constraints.Integer](x T, exp T) T {
	return T(math.Pow(float64(x), float64(exp)))
}

// Sum returns the sum of the arguments.
func Sum[T constraints.Integer](xs ...T) T {
	var sum T = 0
	for _, x := range xs {
		sum += x
	}
	return sum
}
