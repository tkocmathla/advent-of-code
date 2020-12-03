#include <iostream>
#include <map>
#include <regex>
#include <set>

#include "common.h"

using namespace std;

tuple<int, int, char, string> parse(string line) {
  regex re{"^(.+)-(.+) (.): (.+)"};
  smatch matches;
  assert(regex_search(line, matches, re));

  auto lo = atoi(matches.str(1).c_str());
  auto hi = atoi(matches.str(2).c_str());
  auto letter = matches.str(3).at(0);
  auto pwd = matches.str(4);

  return {lo, hi, letter, pwd};
}

void part1(vector<string> strings) {
  int valid = 0;
  for (auto s : strings) {
    auto [ lo, hi, letter, pwd ] = parse(s);

    map<char, int32_t> freq;
    for (auto c : pwd)
      freq[c]++;

    if (freq.find(letter) != freq.end() && freq[letter] >= lo && freq[letter] <= hi)
      valid++;
  }
  cout << valid << endl;
}

void part2(vector<string> strings) {
  int valid = 0;
  for (auto s : strings) {
    auto [ lo, hi, letter, pwd ] = parse(s);
    valid += (pwd.at(--lo) == letter) ^ (pwd.at(--hi) == letter);
  }
  cout << valid << endl;
}

int main(int argc, char **argv) {
  auto strings = load_strings(2);
  part1(strings); // 477
  part2(strings); // 686
  return 0;
}

