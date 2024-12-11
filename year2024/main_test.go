package main

import (
	"aoc/day10"
	"aoc/day11"
	"aoc/day4"
	"aoc/day6"
	"aoc/day7"
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

func BenchmarkDay7Part1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day7.Part1("data/day7.txt")
	}
}

func BenchmarkDay7Part2(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day7.Part2("data/day7.txt")
	}
}

func BenchmarkDay10Part1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day10.Part1("data/day10.txt")
	}
}

func BenchmarkDay10Part2(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day10.Part2("data/day10.txt")
	}
}

func BenchmarkDay11Part1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day11.Part1("data/day11.txt")
	}
}

func BenchmarkDay11Part2(b *testing.B) {
	for i := 0; i < b.N; i++ {
		day11.Part2("data/day11.txt")
	}
}
