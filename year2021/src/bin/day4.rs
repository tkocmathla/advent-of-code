use year2021::io;

fn parse_input(input: &str) -> (Vec<i32>, Vec<Vec<i32>>) {
    let mut lines = input.lines();
    let nums = lines.next().unwrap().split(",").map(|x| x.parse::<i32>().unwrap()).collect();
    let boards = (0..100).fold(vec!(), |mut v, i| {
        v.push(lines
                 .clone()
                 .skip(i * 6 + 1).take(5)
                 .flat_map(|s| s.split_whitespace().map(|x| x.parse::<i32>().unwrap()))
                 .collect());
        v});
    (nums, boards)
}

fn won(board: &&Vec<i32>) -> bool {
    let row = (0..=20).step_by(5).any(|i| (i..i+5).all(|j| board[j] < 0));
    let col = (0..=4).any(|i| (i..25).step_by(5).all(|j| board[j] < 0));
    row || col
}

fn part1(nums: &Vec<i32>, boards: &Vec<Vec<i32>>) -> i32 {
    let mut ans = 0;
    let mut bs = boards.clone();
    for &n in nums {
        bs = bs.iter().map(|b| b.iter().map(|&x| if x == n { x | 1 << 31 } else { x }).collect()).collect();
        if let Some(b) = bs.iter().find(won) {
            let sum: i32 = b.iter().filter(|&&x| x >= 0).sum();
            ans = n * sum;
            break;
        }
    }
    ans
}

fn part2(nums: &Vec<i32>, boards: &Vec<Vec<i32>>) -> i32 {
    let mut ans = 0;
    let mut bs = boards.clone();
    for &n in nums {
        bs = bs.iter().map(|b| b.iter().map(|&x| if x == n { x | 1 << 31 } else { x }).collect()).collect();
        if bs.len() == 1 && won(&&bs[0]) {
            ans = n * bs[0].iter().filter(|&&x| x >= 0).sum::<i32>();
            break;
        }
        bs = bs.into_iter().filter(|b| !won(&b)).collect();
    }
    ans
}

fn main() {
    let (nums, boards) = parse_input(&io::read_input(4));

    assert_eq!(33462, part1(&nums, &boards));
    assert_eq!(30070, part2(&nums, &boards));
}
