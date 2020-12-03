#include <fstream>
#include <string>
#include <vector>

const std::string DATA_DIR = "data/";

std::vector<std::string> load_strings(int8_t day) {
    std::vector<std::string> strings;
    std::ifstream ifs{DATA_DIR + "day" + std::to_string(day) + ".txt"};

    std::string s;
    while (getline(ifs, s))
        strings.push_back(s);

    return strings;
}
