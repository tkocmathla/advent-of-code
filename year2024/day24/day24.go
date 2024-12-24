package day24

import (
	. "aoc/base/aoc"
	"fmt"
	"os"
	s "strings"
)

type Gate struct {
	in0 string
	in1 string
	op  string
}
type Gates map[string]Gate
type Wires map[string]bool

func parse(input string) (Wires, Gates) {
	chunks := s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n\n")
	wires := make(Wires)
	for _, line := range s.Split(chunks[0], "\n") {
		var name string
		var on bool
		fmt.Sscanf(line, "%3s: %t", &name, &on)
		wires[name] = on
	}
	gates := make(Gates)
	for _, line := range s.Split(chunks[1], "\n") {
		var out string
		var gate Gate
		fmt.Sscanf(line, "%s %s %s -> %s", &gate.in0, &gate.op, &gate.in1, &out)
		gates[out] = gate
	}
	return wires, gates
}

func resolve(wires Wires, gates Gates, gate Gate) bool {
	in0, has0 := gates[gate.in0]
	in1, has1 := gates[gate.in1]
	val0, val1 := wires[gate.in0], wires[gate.in1]
	if has0 && has1 {
		val0 = resolve(wires, gates, in0)
		val1 = resolve(wires, gates, in1)
	}
	switch gate.op {
	case "AND":
		return val0 && val1
	case "OR":
		return val0 || val1
	case "XOR":
		return val0 != val1
	}
	panic("")
}

func Part1(input string) int {
	wires, gates := parse(input)
	ans := 0
	for i := 0; ; i++ {
		gate, has := gates[fmt.Sprintf("z%02d", i)]
		if !has {
			break
		}
		bit := resolve(wires, gates, gate)
		if bit {
			ans |= 1 << i
		}
	}
	return ans
}

//func Part2(input string) int { }

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day24_test1.txt"), 4)
	AssertEq(TimeFunc(Part1, "data/day24_test2.txt"), 2024)
	AssertEq(TimeFunc(Part1, "data/day24.txt"), 48508229772400)
}
