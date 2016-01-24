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

// expand will insert zeros ahead of each bit in the first n bits of a, pushing
// the remaining bits left with each insert.  For example, a=0b11001001 and n=8
// would expand to 0b0101000001000001, while the same a but n=4 would expand to
// 0b110001000001.
func expand(a int64, n uint64) int64 {
	m := int64(1) // start with the first bit
	for i := uint64(0); i < n; i++ {
		// Keep the bits masked by m, and shift the ones not masked by m to the left
		a = (a & m) + ((a & ^m) << 1)
		// Add '11' to the start of m.
		m = (m << 2) | 3
	}
	return a
}

// contract will effectively 'or' the first 2n bits of a pairwise, reducing
// those to n bits, and shifting everything else to the right.  For example,
// a=0b11001001 and n=4 would contract to 0b1011, while the same a but n=2 would
// contract to 0b110011.
func contract(a int64, n uint64) int64 {
	out := int64(0)
	b := a >> 1
	for i := uint64(0); i < n; i++ {
		// a & 1 is the first bit of a, b & 1 is the second.  'Or' them.
		out |= ((a | b) & 1) << i
		// On to the next few bits.
		a >>= 2
		b >>= 2
	}
	return out
}

// tally given selection (sel) against a tournament outcome (tru) given
// certain games have been played already (played).
// This function assumes a 6-round (64-team) tournament, and the existance of a
// global 6-element vector roundValues containing the number of points per
// tournament round.
func tally(sel int64, tru int64, played int64) (float32, float32) {
	a := sel
	b := tru
	p := played

	// -----first round-----
	roundMask := int64(0x00000000ffffffff)

	// In the first round, just check if the selection matches the outcome.
	correct := ^(a ^ b) & roundMask
	maybe := ^p & roundMask

	//fmt.Printf("round 1\n")
	//fmt.Printf("%064b a\n", a)
	//fmt.Printf("%064b b\n", b)

	// Count the number correct.  Only count the games played
	score := float32(popcount(correct&p)) * roundValues[0]

	// Count the number yet to be played
	potential := float32(popcount(maybe)) * roundValues[0]

	//fmt.Printf("%064b correct * %f = %f\n", correct, roundValues[0], score)

	// At this point, assume correct all those games that have not yet been played
	correct |= ^p

	// Move on to the next round
	a >>= nGames[0]
	b >>= nGames[0]
	p >>= nGames[0]

	// -----all other rounds-----
	for rnd := uint64(1); rnd < nRounds; rnd++ {
		// Because we shifted the selection over, the lowest nGames bits are all we want.
		// nGames follows the pattern 2^(6-rnd) = 1 << (nRounds - rnd)
		roundMask = int64((1 << (1 << (nRounds - rnd - 1))) - 1)

		//fmt.Printf("round %d, mask %064b\n", rnd+1, roundMask)

		// This trick expands a set of bits, so 0bWXYZ -> 0b0W0X0Y0Z
		c := expand(a, nGames[rnd])

		//fmt.Printf("%064b expanded from\n%064b\n", c, a&roundMask)

		/* In order to determine which game in the past round produced who we picked
		   as the winner in this round, we have to convert a 0b0 in this round to a
		   0b01 in the previous round, and a 0b1 to a 0b10.  That's the same as
		   adding 1 to every 2-bit pair, or adding 0x5555..... */
		c += m1

		//fmt.Printf("%064b c + odds\n", c)
		//fmt.Printf("%064b correct (last round)\n", correct)

		// Given the bits we care about last round, select out the ones we got right.
		forward := c & correct

		//fmt.Printf("%064b forward mask\n", forward)

		/* Now contract the mask, effectively ORing every two bits.  So if we picked
		   correctly at least the game in the last round that matches the selection
		   we chose this round, we can consider it for points this round. */
		forwardMask := contract(forward, nGames[rnd])

		//fmt.Printf("%064b contracted\n", forwardMask)
		//fmt.Printf("%064b a\n", a)
		//fmt.Printf("%064b b\n", b)

		/* Only select as correct those picks that match truth, are in the round we
		   are currently considering, and that we got right before (so the correct
		   team is propagated to this game). */
		correct = ^(a ^ b) & roundMask & forwardMask
		maybe = ^p & roundMask & forwardMask

		//fmt.Printf("%064b correct * %f = %f\n", correct, roundValues[rnd], float32(popcount(correct))*roundValues[rnd])

		// Count the number correct
		score += float32(popcount(correct&p)) * roundValues[rnd]
		potential += float32(popcount(maybe)) * roundValues[rnd]

		// At this point, assume correct all those games that have not yet been played,
		// but also are not ruled out by getting previous picks wrong.
		correct |= ^p & forwardMask

		// Move on to the next round
		a >>= nGames[rnd]
		b >>= nGames[rnd]
		p >>= nGames[rnd]
	}

	return score, potential
}

const m1 int64 = 0x5555555555555555  // binary 0101...
const m2 int64 = 0x3333333333333333  // binary 00110011...
const m4 int64 = 0x0f0f0f0f0f0f0f0f  // binary 0000111100001111...
const h01 int64 = 0x0101010101010101 // binary sum(256^[0 1 2 3 ...])

// popcount counts the number of 1's in the binary representation of the
// argument.  Most processors can do this in a single instruction.  See
// https://en.wikipedia.org/wiki/Hamming_weight
func popcount(x int64) int64 {
	x -= (x >> 1) & m1
	x = (x & m2) + ((x >> 2) & m2)
	x = (x + (x >> 4)) & m4
	return (x * h01) >> 56
}
