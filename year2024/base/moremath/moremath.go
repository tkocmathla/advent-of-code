package moremath

import (
	"golang.org/x/exp/constraints"
)

// Abs returns |a| for integer types.
func Abs[T constraints.Integer](a T) T {
	if a < 0 {
		return -a
	}
	return a
}
