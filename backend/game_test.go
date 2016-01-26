package braket

import (
	"math/rand"
	"testing"
	"time"
)

func BenchmarkSelectedGames(b *testing.B) {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		bra := r.Int63()
		selectedGames(bra)
	}
}
