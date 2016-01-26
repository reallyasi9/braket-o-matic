package braket

// Team represents a single team in the tournament.
type Team struct {
	ID   int
	Name string
	Seed int
	Elo  float32
}
