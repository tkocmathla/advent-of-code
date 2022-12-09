open Year2022.Common

let unpack_cmds cmds =
  let parse dir n = Seq.take n (Seq.repeat dir) in
  Seq.flat_map (fun cmd -> Scanf.sscanf cmd "%s %d" parse) cmds |> List.of_seq

let follow (hx, hy) (tx, ty) =
  let xdist = hx - tx in
  let ydist = hy - ty in

  if Int.abs xdist > 1 then
    let x = tx + if hx > tx then 1 else -1 in
    let y = ty + if hy > ty then 1 else if hy < ty then -1 else 0 in
    x, y
  else if Int.abs ydist > 1 then
    let y = ty + if hy > ty then 1 else -1 in
    let x = tx + if hx > tx then 1 else if hx < tx then -1 else 0 in
    x, y
  else
    tx, ty

let rec update_tail head = function
  | [] -> []
  | knot :: [] -> [follow head knot]
  | knot :: knots ->
      let next_head = follow head knot in
      next_head :: update_tail next_head knots

let update_head head cmds =
  let (hx, hy) = head in
  match cmds with
  | "U" :: _ -> (hx, hy - 1)
  | "D" :: _ -> (hx, hy + 1)
  | "L" :: _ -> (hx - 1, hy)
  | "R" :: _ -> (hx + 1, hy)
  | _ -> head

let step (knots, cmds) =
  let hd = update_head (List.hd knots) cmds in
  hd :: update_tail hd (List.tl knots), List.tl cmds

let solve n seq =
  let knots = Seq.take n (Seq.repeat (0, 0)) |> List.of_seq in
  Seq.iterate step (knots, unpack_cmds seq)
  |> Seq.take_while (fun (_, cmds) -> List.length cmds > 0)
  |> Seq.fold_left (fun set (knots, _) -> PointSet.add (List.nth knots (n - 1)) set) PointSet.empty
  |> PointSet.cardinal

let () =
  print_endline "";
  let seq = Arg.read_arg "data/day9.txt" |> Array.to_seq in

  solve 2 seq |> ((+) 1) |> string_of_int |> print_endline; (* 6236 *)
  solve 10 seq |> string_of_int |> print_endline (* 2449 *)
