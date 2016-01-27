package braket

import (
	"fmt"
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

func BenchmarkGamesUpNext(b *testing.B) {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		pla := int64(1<<uint64(r.Intn(64))) - 1
		gamesUpNext(pla)
	}
}

func TestGamesUpNext(t *testing.T) {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	pla := int64(1<<uint64(r.Intn(64))) - 1
	fmt.Printf("%064b played\n", pla)
	gun := gamesUpNext(pla)
	fmt.Printf("%064b next\n", gun)
}
