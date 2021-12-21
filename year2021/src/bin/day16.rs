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

fn parse_packet(pack: &str) -> Pack {
    let ver = decode(pack, 0, 3);
    match (decode(pack, 3, 6), decode(pack, 6, 7)) {
        (4, _) => {
            let mut spans = (6..).step_by(5).map(|i| (&pack[i..i+1], &pack[i+1..i+5]));
            let mut p = spans.take_while_ref(|(b, _)| b == &"1")
                 .fold(Pack{ver, len: 11, val: 0}, |mut p, (_, v)| {
                     p.len += 5;
                     p.val = (p.val << 4) | decode_all(v);
                     p
                 });
            p.val = (p.val << 4) | decode_all(spans.next().unwrap().1);
            p
        },
        // TODO do this with fold?
        (_, 0) => {
            let len = decode(pack, 7, 22);
            let mut i = 0;
            let mut v = ver;
            while i < len {
                let p = parse_packet(&pack[22+i..]);
                v += p.ver;
                i += p.len;
            }
            Pack{ver: v, len: 22 + len, val: 0}
        },
        (_, 1) => {
            let num = decode(pack, 7, 18);
            (0..num).fold(Pack{ver, len: 18, val: 0}, |mut p, _| {
                let subp = parse_packet(&pack[p.len..]);
                p.ver += subp.ver; p.len += subp.len; p
            })
        },
        _ => panic!("bad packet: {}", pack)
    }
}

fn main() {
    let input = hex_to_bin(&io::read_input(16));

    assert_eq!(1014, parse_packet(&input).ver);
}
