open Year2022.Common

let windows l =
  let rec step l acc =
    match l with
    | a :: b :: t -> step (b :: t) ((a, b) :: acc)
    | _ -> acc
  in
  step l [] |> List.rev

let split_rocks strs =
  let str_to_segs all_segs line =
    let to_point = (fun pair -> Scanf.sscanf pair "%d,%d" (fun x y -> (x, y))) in
    let segs = Str.split (Str.regexp " -> ") line |> List.map to_point in
    segs :: all_segs
  in
  List.fold_left str_to_segs [] strs

let update_grid grid ((x1, y1), (x2, y2)) =
  if x1 <> x2 then
    List.fold_left (fun g x -> g.(y1).(x) <- '#'; g) grid (range_from (Int.min x1 x2) ((Int.max x1 x2) + 1))
  else
    List.fold_left (fun g y -> g.(y).(x1) <- '#'; g) grid (range_from (Int.min y1 y2) ((Int.max y1 y2) + 1))

let populate_grid grid rocks =
  List.fold_left (fun g pts -> List.fold_left update_grid g (windows pts)) grid rocks

let step_sand max_y ((x, y), _, grid) =
  if y + 1 = max_y then begin
    grid.(y).(x) <- 'o';
    ((x, y), true, grid)
  end
  else if grid.(y + 1).(x) = '.' then
    ((x, y + 1), false, grid)
  else if x > 0 && grid.(y + 1).(x - 1) = '.' then
    ((x - 1, y + 1), false, grid)
  else if grid.(y + 1).(x + 1) = '.' then
    ((x + 1, y + 1), false, grid)
  else begin
    grid.(y).(x) <- 'o';
    ((x, y), true, grid)
  end

let drop_sand max_y (pos, grid) =
  Seq.iterate (step_sand max_y) ((500, 0), false, grid)
  |> Seq.drop_while (fun ((_, y), at_rest, _) -> not at_rest && y < max_y)
  |> Seq.uncons |> function None -> (pos, grid) | Some ((p, _, g), _) -> (p, g)

let part1 max_y rocks =
  let grid = populate_grid (Array.make_matrix max_y 700 '.') rocks in
  Seq.iterate (drop_sand max_y) ((0, 0), grid)
  |> Seq.take_while (fun ((_, y), _) -> y <> (pred max_y))
  |> List.of_seq |> List.length |> pred

let part2 max_y rocks =
  let grid = populate_grid (Array.make_matrix max_y 700 '.') rocks in
  Seq.iterate (drop_sand max_y) ((0, 0), grid)
  |> Seq.take_while (fun (pos, _) -> pos <> (500, 0))
  |> List.of_seq |> List.length

let () =
  (* max y = 169, max x = 521 *)
  let rocks = Arg.read_arg "data/day14.txt" |> Array.to_list |> split_rocks in
  part1 170 rocks |> string_of_int |> print_endline; (* 843 *)
  part2 171 rocks |> string_of_int |> print_endline; (* 27625 *)
