use itertools::Itertools;

use year2021::io;

struct Pack {
    ver: usize,
    len: usize,
    val: usize
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

fn parse_packet(bin: &str) -> Pack {
    let ver = decode(bin, 0, 3);
    match (decode(bin, 3, 6), decode(bin, 6, 7)) {
        (4, _) => {
            let mut spans = (6..).step_by(5).map(|i| (&bin[i..i+1], &bin[i+1..i+5]));
            let mut pack = spans
                .take_while_ref(|(b, _)| b == &"1")
                .fold(Pack{ver, len: 11, val: 0}, |p, (_, v)| {
                    Pack{ver: p.ver, len: p.len + 5, val: (p.val << 4) | decode_all(v)}
                });
            pack.val = (pack.val << 4) | decode_all(spans.next().unwrap().1);
            pack
        },
        (id, 0) => {
            let len = decode(bin, 7, 22);
            let mut i = 0;
            let mut v = ver;
            let mut acc = None;
            while i < len {
                let pack = parse_packet(&bin[22+i..]);
                acc = Some(calc(id, acc, pack.val));
                v += pack.ver;
                i += pack.len;
            }
            Pack{ver: v, len: 22 + len, val: acc.unwrap()}
        },
        (id, 1) => {
            let num = decode(bin, 7, 18);
            let mut acc = None;
            let mut pack = (0..num).fold(Pack{ver, len: 18, val: 0}, |p, _| {
                let subp = parse_packet(&bin[p.len..]);
                acc = Some(calc(id, acc, subp.val));
                Pack{ver: p.ver + subp.ver, len: p.len + subp.len, val: 0}
            });
            pack.val = acc.unwrap();
            pack
        },
        _ => panic!()
    }
}

fn main() {
    let input = hex_to_bin(&io::read_input(16));

    assert_eq!(1014, parse_packet(&input).ver);
    assert_eq!(1922490999789, parse_packet(&input).val);
}
