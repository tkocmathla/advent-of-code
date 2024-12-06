package main

import (
	"aoc/day4"
	"aoc/day6"
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

func BenchmarkDay6Part1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day6.Part1("data/day6.txt")
	}
}

func BenchmarkDay6Part2(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day6.Part2("data/day6.txt")
	}
}
