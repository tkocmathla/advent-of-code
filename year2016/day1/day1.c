#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *slurp(const char *file) {
  FILE *fp = fopen(file, "rb");
  assert(fp);
  fseek(fp, 0, SEEK_END);
  long len = ftell(fp);
  fseek(fp, 0, SEEK_SET);

  char *buf = malloc(len + 1);
  assert(buf);
  size_t read = fread(buf, 1, len, fp);
  assert(read == (size_t)len);
  buf[read] = '\0';

  fclose(fp);
  return buf;
}

int part1(char *input) {
  int ans = 0;
  int dx = 0, dy = -1;

  char dir;
  int dist;
  char *tok = strtok(input, ", ");
  while (tok != NULL) {
    assert(sscanf(tok, "%c%d", &dir, &dist) == 2);

    int tmp = dx;
    if (dir == 'L') {
      dx = -dy;
      dy = tmp;
    } else { // dir == 'R'
      dx = dy;
      dy = -tmp;
    }

    ans += (dx * dist) + (dy * dist);
    tok = strtok(NULL, ", ");
  }

  return ans;
}

int main(int argc, char **argv) {
  assert(argc > 1);
  char *input = slurp(argv[1]);

  assert(part1(input) == 299);

  printf("OK\n");
  free(input);
  return 0;
}
