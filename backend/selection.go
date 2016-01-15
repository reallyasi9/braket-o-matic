package braket

const nRounds = uint64(6)

var nGames = [nRounds]uint64{
	32,
	16,
	8,
	4,
	2,
	1,
}

// TODO Make this external
var roundValues = [nRounds]float32{
	1, 2, 3, 5, 7, 13,
}

func expand(a int64, n uint64) int64 {
	for i := uint64(0); i < n; i++ {
		m := int64(1<<(i*2+1) - 1)
		a = (a & m) + ((a & ^m) << 1)
	}
	return a
}

func contract(a int64, n uint64) int64 {
	out := int64(0)
	for i := uint64(0); i < n; i++ {
		b := a >> 1
		out |= ((a | b) & 1) << i
		a >>= 2
	}
	return out
}

func tally(sel int64, tru int64) float32 {
	a := sel
	b := tru

	// -----first round-----

	// Nothing besides whether or not the selections match matter here.
	correct := ^(a ^ b)

	// Only look at the first round games
	correct &= 0xffffffff

	//fmt.Printf("round 1\n")
	//fmt.Printf("%032b a\n", a)
	//fmt.Printf("%032b b\n", b)

	// Count the number correct
	score := float32(popcount(correct)) * roundValues[0]

	//fmt.Printf("%032b correct * %f = %f\n", correct, roundValues[0], score)

	// Move on to the next round
	a >>= nGames[0]
	b >>= nGames[0]

	// -----all other rounds-----
	for rnd := uint64(1); rnd < nRounds; rnd++ {
		// Because we shifted the selection over, the lowest nGames bits are all we want.
		// nGames follows the pattern 2^(6-rnd) = 1 << (nRounds - rnd)
		roundMask := int64((1 << (1 << (nRounds - rnd - 1))) - 1)

		//fmt.Printf("round %d, mask %032b\n", rnd+1, roundMask)

		// This trick expands a set of bits, so 0bWXYZ -> 0b0W0X0Y0Z
		c := expand(a, nGames[rnd])

		//fmt.Printf("%032b expanded from %032b\n", c, a&roundMask)

		/* In order to determine which game in the past round produced who we picked
		   as the winner in this round, we have to convert a 0b0 in this round to a
		   0b01 in the previous round, and a 0b1 to a 0b10.  That's the same as
		   adding 1 to every 2-bit pair, or adding 0x5555..... */
		c += 0x5555555555555555

		//fmt.Printf("%032b c + odds\n", c)
		//fmt.Printf("%032b correct (last round)\n", correct)

		// Given the bits we care about last round, select out the ones we got right.
		forward := c & correct

		//fmt.Printf("%032b forward mask\n", forward)

		/* Now contract the mask, effectively ORing every two bits.  So if we picked
		   correctly at least the game in the last round that matches the selection
		   we chose this round, we can consider it for points this round. */
		forwardMask := contract(forward, nGames[rnd])

		//fmt.Printf("%032b contracted\n", forwardMask)
		//fmt.Printf("%032b a\n", a)
		//fmt.Printf("%032b b\n", b)

		/* Only select as correct those picks that match truth, are in the round we
		   are currently considering, and that we got right before (so the correct
		   team is propagated to this game). */
		correct = ^(a ^ b) & roundMask & forwardMask

		//fmt.Printf("%032b correct * %f = %f\n", correct, roundValues[rnd], float32(popcount(correct))*roundValues[rnd])

		// Count the number correct
		score += float32(popcount(correct)) * roundValues[rnd]

		// Move on to the next round
		a >>= nGames[rnd]
		b >>= nGames[rnd]
	}

	return score
}

// See https://en.wikipedia.org/wiki/Hamming_weight
const m1 int64 = 0x5555555555555555  // binary 0101...
const m2 int64 = 0x3333333333333333  // binary 00110011...
const m4 int64 = 0x0f0f0f0f0f0f0f0f  // binary 0000111100001111...
const h01 int64 = 0x0101010101010101 // binary sum(256^[0 1 2 3 ...])

func popcount(x int64) int64 {
	x -= (x >> 1) & m1
	x = (x & m2) + ((x >> 2) & m2)
	x = (x + (x >> 4)) & m4
	return (x * h01) >> 56
}
