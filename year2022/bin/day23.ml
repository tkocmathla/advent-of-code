open Year2022.Common

let range n = Seq.iterate succ 0 |> Seq.take n

let to_elves ary =
  let ylen = Array.length ary in
  let xlen = String.length ary.(0) in
  let assign = (fun y set x -> if ary.(y).[x] = '#' then PointSet.add (x, y) set else set) in
  Seq.fold_left (fun set y -> Seq.fold_left (assign y) set (range xlen)) PointSet.empty (range ylen)

let vacant elves x y dxys =
  List.fold_left (fun acc (dx, dy) -> acc && not (PointSet.mem (x + dx, y + dy) elves)) true dxys

let alone elves x y = vacant elves x y [(-1, -1); (0, -1); (1, -1); (-1, 0); (1, 0); (-1, 1); (0, 1); (1, 1)]
let n elves x y = (x, y - Bool.to_int (vacant elves x y [(-1, -1); (0, -1); (1, -1)]))
let s elves x y = (x, y + Bool.to_int (vacant elves x y [(-1, 1); (0, 1); (1, 1)]))
let w elves x y = (x - Bool.to_int (vacant elves x y [(-1, 0); (-1, -1); (-1, 1)]), y)
let e elves x y = (x + Bool.to_int (vacant elves x y [(1, 0); (1, -1); (1, 1)]), y)

let propose elves fns (proposals, counts) (x, y) =
  if not (alone elves x y) then
    let found = Seq.find (fun f -> f elves x y <> (x, y)) fns in
    let next = match found with | Some f -> f elves x y | _ -> (x, y) in
    (PointMap.add (x, y) next proposals,
     PointMap.update next (function None -> Some 1 | Some count -> Some (succ count)) counts)
  else
    (proposals, counts)

let move counts elves ((x, y), (px, py)) =
  if PointMap.find (px, py) counts = 1 then
    PointSet.remove (x, y) elves |> PointSet.add (px, py)
  else
    elves

let round (elves, fns, _) =
  let fns' = Seq.take 4 fns in
  let (proposals, counts) =
    Seq.fold_left (propose elves fns') (PointMap.empty, PointMap.empty) (PointSet.to_seq elves) in
  let elves' = Seq.fold_left (move counts) elves (PointMap.to_seq proposals) in
  (elves', Seq.drop 1 fns, elves = elves')

let area elves =
  let elves' = PointSet.to_seq elves in
  let min_x = Seq.fold_left (fun min (x, _) -> Int.min min x) 0 elves' in
  let max_x = Seq.fold_left (fun max (x, _) -> Int.max max x) 0 elves' in
  let min_y = Seq.fold_left (fun min (_, y) -> Int.min min y) 0 elves' in
  let max_y = Seq.fold_left (fun max (_, y) -> Int.max max y) 0 elves' in
  (max_x - min_x + 1) * (max_y - min_y + 1) - PointSet.cardinal elves

let part1 elves =
  Seq.iterate round (elves, Seq.cycle (List.to_seq [n; s; w; e]), false)
  |> Seq.drop 10
  |> Seq.uncons
  |> function Some ((elves, _, _), _) -> area elves | _ -> 0

let part2 elves =
  Seq.iterate round (elves, Seq.cycle (List.to_seq [n; s; w; e]), false)
  |> Seq.take_while (fun (_, _, same) -> not same)
  |> List.of_seq |> List.length

let () =
  print_endline "";
  let elves = Arg.read_arg "data/day23.txt" |> to_elves in

  part1 elves |> string_of_int |> print_endline; (* 4302 *)
  part2 elves |> string_of_int |> print_endline (* 1025 *)
