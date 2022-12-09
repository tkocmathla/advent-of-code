open Year2022.Common

let unpack_cmds cmds =
  let parse dir n = Seq.take n (Seq.repeat dir) in
  Seq.flat_map (fun cmd -> Scanf.sscanf cmd "%s %d" parse) cmds
  |> List.of_seq

let follow hx hy (tx, ty) =
  let xdist = hx - tx in
  let ydist = hy - ty in
  if Int.abs ydist > 1 then (hx, hy + (if ydist < 0 then 1 else -1))
  else if Int.abs xdist > 1 then (hx + (if xdist < 0 then 1 else -1), hy)
  else (tx, ty)

let step ((hx, hy), tail, visited, cmds) =
  let (newh, newt, newcmds) =
    match cmds with
    | "U" :: rest -> ((hx, hy - 1), follow hx (hy - 1) tail, rest)
    | "D" :: rest -> ((hx, hy + 1), follow hx (hy + 1) tail, rest)
    | "L" :: rest -> ((hx - 1, hy), follow (hx - 1) hy tail, rest)
    | "R" :: rest -> ((hx + 1, hy), follow (hx + 1) hy tail, rest)
    | _ -> failwith "derp"
  in
  (newh, newt, PointSet.add newt visited, newcmds)

let part1 seq =
  Seq.iterate step ((0, 0), (0, 0), PointSet.add (0, 0) PointSet.empty, unpack_cmds seq)
  |> Seq.drop_while (fun (_, _, _, cmds) -> match cmds with [] -> false | _ -> true)
  |> Seq.uncons
  |> function | None -> 0 | Some ((_, _, set, _), _) -> PointSet.cardinal set

let () =
  print_endline "";
  let seq = Arg.read_arg "data/day9.txt" |> Array.to_seq in

  part1 seq |> string_of_int |> print_endline;
