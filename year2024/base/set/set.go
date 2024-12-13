package set

type Empty struct{}
type Set map[interface{}]Empty

func New() Set {
	return make(Set)
}

func (set Set) Add(value interface{}) {
	set[value] = Empty{}
}

func (set Set) Remove(value interface{}) {
	delete(set, value)
}

func (set Set) Has(value interface{}) bool {
	_, has := set[value]
	return has
}
