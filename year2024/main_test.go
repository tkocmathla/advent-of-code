package main

import (
	"aoc/day4"
	"testing"
)

func BenchmarkDay4Part1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day4.Part1("data/day4.txt")
	}
}

func BenchmarkDay4Part2(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day4.Part2("data/day4.txt")
	}
}
