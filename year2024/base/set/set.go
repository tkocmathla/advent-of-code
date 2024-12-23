package set

type Empty struct{}
type Set map[interface{}]Empty

func New(items ...interface{}) Set {
	s := make(Set)
	for _, item := range items {
		s.Add(item)
	}
	return s
}

func (set Set) Add(values ...interface{}) {
	for _, v := range values {
		set[v] = Empty{}
	}
}

func (set Set) Remove(value interface{}) {
	delete(set, value)
}

func (set Set) Has(value interface{}) bool {
	_, has := set[value]
	return has
}

func (set Set) Clone() Set {
	copy := New()
	for k := range set {
		copy.Add(k)
	}
	return copy
}

func (set Set) Union(other Set) Set {
	union := New()
	for k := range set {
		if other.Has(k) {
			union.Add(k)
		}
	}
	for k := range other {
		if set.Has(k) {
			union.Add(k)
		}
	}
	return union
}

func (set Set) Intersection(other Set) Set {
	intersection := New()
	for k := range set {
		if other.Has(k) {
			intersection.Add(k)
		}
	}
	return intersection
}
