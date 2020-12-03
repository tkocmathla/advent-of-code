#include <iostream>
#include <set>

#include "common.h"

using namespace std;

void part1(vector<string> strings) {
  set<int32_t> xs;
  for (auto s : strings)
    xs.insert(atoi(s.c_str()));

  for (auto x : xs) {
    auto y = 2020 - x;
    if (xs.count(y)) {
      cout << x << " * " << y << " = " << x * y << endl;
      break;
    }
  }
}

void part2(vector<string> strings) {
  set<int32_t> xs;
  for (auto s : strings)
    xs.insert(atoi(s.c_str()));

  for (auto a = xs.begin(); a != xs.end(); ++a) {
    for (auto b = a; b != xs.end(); ++b) {
      auto c = 2020 - (*a + *b);
      if (xs.count(c)) {
        cout << *a << " * " << *b << " * " << c << " = " << *a * *b * c << endl;
        break;
      }
    }
  }
}

int main(int argc, char **argv) {
  auto strings = load_strings(1);
  part1(strings);
  part2(strings);
  return 0;
}
