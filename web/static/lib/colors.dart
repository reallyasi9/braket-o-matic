String generateBackground(List<String> colors) {
  if (colors.isEmpty) {
    return "background: #888888";
  } else if (colors.length == 1) {
    return "background: ${colors[0]}";
  } else {
    List<String> cStrings = [];
    cStrings.add(colors[0]);
    cStrings.add("${colors[0]} 50%");
    cStrings.add("${colors[1]} 50%");
    int step = (50 / (colors.length - 1)).floor();
    for (int i = 2; i < colors.length; i++) {
      cStrings.add("${colors[i-1]} ${50+step*(i-1)}%");
      // cStrings.add("black ${step*i}%");
      cStrings.add("${colors[i]} ${50+step*(i-1)}%");
    }
    String c = cStrings.join(",");
    return "background: linear-gradient(100deg,$c)";
  }
}
