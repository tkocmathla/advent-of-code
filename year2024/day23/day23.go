package day23

import (
	. "aoc/base/aoc"
	"aoc/base/set"
	"aoc/base/slice"
	"os"
	"sort"
	s "strings"
)

type Graph map[string][]string

func parse(input string) Graph {
	g := make(Graph)
	data := s.Split(s.TrimSpace(string(Try(os.ReadFile(input)))), "\n")
	for _, line := range data {
		uv := s.Split(line, "-")
		g[uv[0]] = append(g[uv[0]], uv[1])
		g[uv[1]] = append(g[uv[1]], uv[0])
	}
	return g
}

func Part1(input string) int {
	g := parse(input)
	groups := set.New()
	for u, vs := range g {
		for _, v := range vs {
			for _, w := range g[v] {
				if u != v && u != w && slice.Contains(g[u], w) {
					group := []string{u, v, w}
					sort.Strings(group)
					groups.Add(s.Join(group, ","))
				}
			}
		}
	}
	count := 0
	for group := range groups {
		if group.(string)[0] == 't' || s.Contains(group.(string), ",t") {
			count += 1
		}
	}
	return count
}

func bronKerbosch(r, p, x set.Set, g Graph, max *set.Set) {
	if len(p) == 0 && len(x) == 0 {
		if len(r) > len(*max) {
			*max = r
		}
	}
	for v := range p {
		nbrs := set.New()
		for _, nbr := range g[v.(string)] {
			nbrs.Add(nbr)
		}
		r2 := r.Clone()
		r2.Add(v)
		bronKerbosch(r2, p.Intersection(nbrs), x.Intersection(nbrs), g, max)
		p.Remove(v)
		x.Add(v)
	}
}

func maxClique(g Graph) set.Set {
	p := set.New()
	for u := range g {
		p.Add(u)
	}
	r := set.New()
	x := set.New()
	max := set.New()
	bronKerbosch(r, p, x, g, &max)
	return max
}

func Part2(input string) string {
	g := parse(input)
	var max []string
	for v := range maxClique(g) {
		max = append(max, v.(string))
	}
	sort.Strings(max)
	return s.Join(max, ",")
}

func Solve() {
	AssertEq(TimeFunc(Part1, "data/day23_test.txt"), 7)
	AssertEq(TimeFunc(Part1, "data/day23.txt"), 1184)
	AssertEq(Part2("data/day23_test.txt"), "co,de,ka,ta")
	AssertEq(Part2("data/day23.txt"), "hf,hz,lb,lm,ls,my,ps,qu,ra,uc,vi,xz,yv")
}
