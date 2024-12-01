package util

func Try[T any](value T, e error) T {
	if e != nil {
		panic(e)
	}
	return value
}
