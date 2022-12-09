open Year2022.Common

let unpack_cmds cmds =
  let parse dir n = Seq.take n (Seq.repeat dir) in
  Seq.flat_map (fun cmd -> Scanf.sscanf cmd "%s %d" parse) cmds
  |> List.of_seq

let follow (hx, hy) (tx, ty) =
  let xdist = hx - tx in
  let ydist = hy - ty in
  if Int.abs ydist > 1 then (hx, hy + (if ydist < 0 then 1 else -1))
  else if Int.abs xdist > 1 then (hx + (if xdist < 0 then 1 else -1), hy)
  else (tx, ty)

let rec update_step head = function
  | [] -> []
  | knot :: [] -> [follow head knot]
  | knot :: knots ->
      let next_head = follow head knot in
      next_head :: update_step next_head knots

let step (knots, cmds) =
  let (hx, hy) = List.hd knots in
  match cmds with
  | "U" :: cmds -> let hd = (hx, hy - 1) in hd :: update_step hd (List.tl knots), cmds
  | "D" :: cmds -> let hd = (hx, hy + 1) in hd :: update_step hd (List.tl knots), cmds
  | "L" :: cmds -> let hd = (hx - 1, hy) in hd :: update_step hd (List.tl knots), cmds
  | "R" :: cmds -> let hd = (hx + 1, hy) in hd :: update_step hd (List.tl knots), cmds
  | _ -> (knots, cmds)

let solve n seq =
  let knots = Seq.take n (Seq.repeat (0, 0)) |> List.of_seq in
  Seq.iterate step (knots, unpack_cmds seq)
  |> Seq.take_while (fun (_, cmds) -> match cmds with [] -> false | _ -> true)
  |> Seq.fold_left (fun set (knots, _) -> PointSet.add (List.nth knots (n - 1)) set) PointSet.empty
  |> PointSet.cardinal |> ((+) 1) (* FIXME WHY!? *)

let () =
  print_endline "";
  let seq = Arg.read_arg "data/day9.txt" |> Array.to_seq in

  solve 2 seq |> string_of_int |> print_endline; (* 6236 *)
  solve 10 seq |> string_of_int |> print_endline
