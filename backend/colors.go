package braket

import (
	"fmt"
	"math"
)

// HSV converts RGB bytes into hue [0,360), saturation [0,1], and variance [0,1] floats.
func HSV(rb, gb, bb byte) (h, s, v float64) {
	r := float64(rb) / 255.
	g := float64(gb) / 255.
	b := float64(bb) / 255.

	min := math.Min(r, math.Min(g, b))
	v = math.Max(r, math.Max(g, b))
	c := v - min // chroma

	switch {
	case v == 0:
		s = 0 // by definition
	default:
		s = c / v
	}

	switch {
	case c == 0:
		h = 0 // technically undefined
	case v == r:
		h = math.Mod((g-b)/c, 6)
	case v == g:
		h = (b - r) / c * 2
	case v == b:
		h = (r-g)/c + 4
	}
	h *= 60

	for h < 0 {
		h += 360
	}
	for h > 360 {
		h -= 360
	}

	return h, s, v
}

// RGB converts HSV floats int RGB bytes
func RGB(h, s, v float64) (r, g, b byte) {
	if v == 0 {
		return 0, 0, 0
	}
	if s == 0 {
		return byte(v * 255), byte(v * 255), byte(v * 255)
	}

	c := v * s // chroma
	h /= 60
	x := c * (1 - math.Abs(math.Mod(h, 2)-1))

	var r1, g1, b1 float64

	switch {
	case h < 1:
		r1, g1, b1 = c, x, 0
	case h < 2:
		r1, g1, b1 = x, c, 0
	case h < 3:
		r1, g1, b1 = 0, c, x
	case h < 4:
		r1, g1, b1 = 0, x, c
	case h < 5:
		r1, g1, b1 = x, 0, c
	default:
		r1, g1, b1 = c, 0, x
	}

	m := v - c
	return byte((r1 + m) * 255), byte((g1 + m) * 255), byte((b1 + m) * 255)

}

// RGBHex converts RGB bytes to a hex string
func RGBHex(r, g, b byte) string {
	return fmt.Sprintf("#%02x%02x%02x", r, g, b)
}
