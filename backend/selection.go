package braket

const nRounds = int32(6)

var roundMasks = [nRounds]int64{
	0x00000000ffffffff,
	0x0000ffff00000000,
	0x00ff000000000000,
	0x0f00000000000000,
	0x3000000000000000,
	0x4000000000000000,
}

const m1 int64 = 0x5555555555555555  // binary 0101...
const m2 int64 = 0x3333333333333333  // binary 00110011...
const m4 int64 = 0x0f0f0f0f0f0f0f0f  // binary 0000111100001111...
const h01 int64 = 0x0101010101010101 // binary sum(256^[0 1 2 3 ...])

func tally(sel int64, bracket int64, determined int64, rules [nRounds]float32) float32 {
	// Step 1: check first round
	mask := determined & roundMasks[0]
	correct := sel & bracket & mask

	ncorrect := popcount(correct)
	points := rules[0] * float32(ncorrect)

	// Steps 2-6: propagate errors and check round-by-round
	// TODO complete
	return points
}

func propagate(sel int64, correct int64, nextRound int) int64 {
	//incorrect := ^correct
	// TODO Complete
	return correct
}

// See https://en.wikipedia.org/wiki/Hamming_weight
func popcount(x int64) int64 {
	x -= (x >> 1) & m1
	x = (x & m2) + ((x >> 2) & m2)
	x = (x + (x >> 4)) & m4
	return (x * h01) >> 56
}
