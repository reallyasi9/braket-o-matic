package main

import (
	"math/rand"
	"testing"
	"time"
)

func BenchmarkTally(b *testing.B) {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		bra := r.Int63()
		ket := r.Int63()
		tally(bra, ket)
	}
}
