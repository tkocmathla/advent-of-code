open Year2022.Common

let to_grid lines =
  let char_to_cell ch = (int_from_char ch, 0, 0, 0, 0) in
  let string_to_row s = String.to_seq s |> Array.of_seq |> Array.map char_to_cell in
  Array.map string_to_row lines

(* part 1 *)

let max_n grid x y this_n =
  if y > 0 then
    let that, that_n, _, _, _ = grid.(y - 1).(x) in
    Int.max this_n @@ Int.max that that_n
  else this_n

let max_e grid x y this_e =
  if x < Array.length grid.(y) - 1 then
    let that, _, that_e, _, _ = grid.(y).(x + 1) in
    Int.max this_e @@ Int.max that that_e
  else this_e

let max_s grid x y this_s =
  if y < Array.length grid - 1 then
    let that, _, _, that_s, _ = grid.(y + 1).(x) in
    Int.max this_s @@ Int.max that that_s
  else this_s

let max_w grid x y this_w =
  if x > 0 then
    let that, _, _, _, that_w = grid.(y).(x - 1) in
    Int.max this_w @@ Int.max that that_w
  else this_w

let propagate grid yrange xrange =
  let update y x =
    let value, n, e, s, w = grid.(y).(x) in
    grid.(y).(x) <- (value, max_n grid x y n, max_e grid x y e, max_s grid x y s, max_w grid x y w) in
  List.iter (fun y -> List.iter (fun x -> update y x) xrange) yrange

let part1 grid =
  let height = Array.length grid in
  let width = Array.length grid.(0) in
  propagate grid (range height) (range width);
  propagate grid (List.rev (range height)) (List.rev (range width));

  let inner a = Array.to_seq a |> Seq.drop 1 |> Seq.take (width - 2) in
  let matches = Seq.filter (fun (value, n, e, s, w) -> value > n || value > e || value > s || value > w) in
  inner grid
  |> Seq.fold_left
       (fun sum row -> sum + (inner row |> matches |> List.of_seq |> List.length))
       ((width * 2) + ((height - 2) * 2))

(* part 2 *)

let score grid x y this_v =
  let height = Array.length grid in
  let width = Array.length grid.(0) in
  let n = Seq.take_while (fun dy -> let that_v, _, _, _, _ = grid.(dy).(x) in this_v > that_v)
                         (List.to_seq (List.rev (range y)))
          |> List.of_seq |> List.length in
  let e = Seq.take_while (fun dx -> let that_v, _, _, _, _ = grid.(y).(dx) in this_v > that_v)
                         (List.to_seq (range_from (x + 1) width))
          |> List.of_seq |> List.length in
  let s = Seq.take_while (fun dy -> let that_v, _, _, _, _ = grid.(dy).(x) in this_v > that_v)
                         (List.to_seq (range_from (y + 1) height))
          |> List.of_seq |> List.length in
  let w = Seq.take_while (fun dx -> let that_v, _, _, _, _ = grid.(y).(dx) in this_v > that_v)
                         (List.to_seq (List.rev (range x)))
          |> List.of_seq |> List.length in

  let npad = if y - n > 0 then 1 else 0 in
  let epad = if x + e < width - 1 then 1 else 0 in
  let spad = if y + s < height - 1 then 1 else 0 in
  let wpad = if x - w > 0 then 1 else 0 in
  (n + npad) * (e + epad) * (s + spad) * (w + wpad)

let part2 grid =
  let update_cell y x (value, _, _, _, _) = grid.(y).(x) <- (value, score grid x y value, 0, 0, 0) in
  Array.iteri (fun y row -> Array.iteri (fun x cell -> update_cell y x cell) row) grid;

  let collect row = List.map (fun (_, score, _, _, _) -> score) (Array.to_list row) in
  Array.fold_left (fun l row -> collect row @ l) [] grid |> List.sort compare |> List.rev |> List.hd

let () =
  print_endline "";
  let grid = Arg.read_arg "data/day8.txt" |> to_grid in

  part1 grid |> string_of_int |> print_endline;
  part2 grid |> string_of_int |> print_endline
