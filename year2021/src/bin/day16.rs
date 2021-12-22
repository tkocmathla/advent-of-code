use itertools::Itertools;
use std::ops;

use year2021::io;

#[derive(Clone, Copy)]
struct Pack {
    id: usize,
    ver: usize,
    len: usize,
    val: Option<usize>
}

fn hex_to_bin(hex: &str) -> String {
    hex.chars().map(|c| format!("{:04b}", c.to_digit(16).unwrap())).collect()
}

fn decode(bin: &str, i: usize, j: usize) -> usize {
    usize::from_str_radix(&bin[i..j], 2).unwrap()
}

fn decode_all(bin: &str) -> usize {
    usize::from_str_radix(&bin, 2).unwrap()
}

fn calc(id: usize, acc: Option<usize>, val: usize) -> usize {
    match (id, acc) {
        (_, None) => val,
        (0, Some(acc)) => acc + val,
        (1, Some(acc)) => acc * val,
        (2, Some(acc)) => acc.min(val),
        (3, Some(acc)) => acc.max(val),
        (5, Some(acc)) => if acc > val { 1 } else { 0 },
        (6, Some(acc)) => if acc < val { 1 } else { 0 },
        (7, Some(acc)) => if acc == val { 1 } else { 0 },
        (_, Some(acc)) => acc
    }
}

impl ops::Add<Pack> for Pack {
    type Output = Pack;
    fn add(self, rhs: Pack) -> Pack {
        let val = Some(calc(self.id, self.val, rhs.val.unwrap()));
        Pack{id: self.id, ver: self.ver + rhs.ver, len: self.len + rhs.len, val}
    }
}

fn parse_packet(bin: &str) -> Pack {
    let ver = decode(bin, 0, 3);
    match (decode(bin, 3, 6), decode(bin, 6, 7)) {
        (4, _) => {
            let mut spans = (6..).step_by(5).map(|i| (&bin[i..i+1], &bin[i+1..i+5]));
            let mut pack = spans
                .take_while_ref(|(b, _)| b == &"1")
                .fold(Pack{id: 4, ver, len: 11, val: Some(0)}, |p, (_, v)| {
                    Pack{id: 4, ver: p.ver, len: p.len + 5, val: Some((p.val.unwrap() << 4) | decode_all(v))}
                });
            pack.val = Some((pack.val.unwrap() << 4) | decode_all(spans.next().unwrap().1));
            pack
        },
        (id, 0) => {
            let len = decode(bin, 7, 22);
            let mut pack = Pack{id, ver, len: 22, val: None};
            while pack.len < 22 + len {
                let subp = parse_packet(&bin[pack.len..]);
                pack = pack + subp
            }
            pack
        },
        (id, 1) => {
            let num = decode(bin, 7, 18);
            (0..num).fold(Pack{id, ver, len: 18, val: None}, |p, _| { p + parse_packet(&bin[p.len..]) })
        },
        _ => panic!()
    }
}

fn main() {
    let input = hex_to_bin(&io::read_input(16));

    assert_eq!(1014, parse_packet(&input).ver);
    assert_eq!(1922490999789, parse_packet(&input).val.unwrap());
}
