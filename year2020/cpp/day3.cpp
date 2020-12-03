#include <iostream>
#include <map>
#include <regex>
#include <set>

#include "common.h"

using namespace std;

int part1(vector<string> strings, pair<int, int> slope = {3, 1}) {
  int trees = 0;
  int j = 0;
  for (int i = 0; i < strings.size(); i += get<1>(slope)) {
    trees += strings[i][j] == '#';
    j = (j + get<0>(slope)) % strings[0].size();
  }
  return trees;
}

int part2(vector<string> strings) {
  int ans = 1;
  vector<pair<int, int>> slopes = {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};
  for (auto slope : slopes)
    ans *= part1(strings, slope);
  return ans;
}

int main(int argc, char **argv) {
  auto strings = load_strings(3);
  assert(225 == part1(strings));
  assert(1115775000 == part2(strings));
  return 0;
}


