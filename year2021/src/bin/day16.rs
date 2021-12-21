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
        //4 =>  literal decode?
        (5, Some(acc)) => if acc > val { 1 } else { 0 },
        (6, Some(acc)) => if acc < val { 1 } else { 0 },
        (7, Some(acc)) => if acc == val { 1 } else { 0 },
        (_, _) => acc.unwrap()
    }
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
        (id, 0) => {
            let len = decode(pack, 7, 22);
            let mut i = 0;
            let mut v = ver;
            let mut acc = None;
            while i < len {
                let p = parse_packet(&pack[22+i..]);
                acc = Some(calc(id, acc, p.val));
                v += p.ver;
                i += p.len;
            }
            Pack{ver: v, len: 22 + len, val: acc.unwrap()}
        },
        (id, 1) => {
            let num = decode(pack, 7, 18);
            let mut acc = None;
            let mut p2 = (0..num).fold(Pack{ver, len: 18, val: 0}, |mut p, _| {
                let subp = parse_packet(&pack[p.len..]);
                acc = Some(calc(id, acc, subp.val));
                p.ver += subp.ver;
                p.len += subp.len;
                p
            });
            p2.val = acc.unwrap();
            p2

        },
        _ => panic!("bad packet: {}", pack)
    }
}

fn main() {
    let input = hex_to_bin(&io::read_input(16));

    assert_eq!(1014, parse_packet(&input).ver);
    assert_eq!(1922490999789, parse_packet(&input).val);
}
