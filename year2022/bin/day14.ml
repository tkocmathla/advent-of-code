open Year2022.Common

let windows l =
  let rec step acc = function
    | a :: b :: t -> step ((a, b) :: acc) (b :: t)
    | _ -> acc
  in step [] l |> List.rev

let parse_rocks strs =
  let to_point = (fun s -> Scanf.sscanf s "%d,%d" (fun x y -> (x, y))) in
  let to_segs acc line = (List.map to_point (Str.split (Str.regexp " -> ") line)) :: acc in
  List.fold_left to_segs [] strs

let add_rock_line grid ((x1, y1), (x2, y2)) =
  if x1 <> x2 then
    List.fold_left (fun g x -> g.(y1).(x) <- '#'; g) grid (range_from (Int.min x1 x2) ((Int.max x1 x2) + 1))
  else
    List.fold_left (fun g y -> g.(y).(x1) <- '#'; g) grid (range_from (Int.min y1 y2) ((Int.max y1 y2) + 1))

let add_rocks grid rocks =
  List.fold_left (fun g pts -> List.fold_left add_rock_line g (windows pts)) grid rocks

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
  let grid = add_rocks (Array.make_matrix max_y 700 '.') rocks in
  Seq.iterate (drop_sand max_y) ((0, 0), grid)
  |> Seq.take_while (fun ((_, y), _) -> y <> (pred max_y))
  |> List.of_seq |> List.length |> pred

let part2 max_y rocks =
  let grid = add_rocks (Array.make_matrix max_y 700 '.') rocks in
  Seq.iterate (drop_sand max_y) ((0, 0), grid)
  |> Seq.take_while (fun (pos, _) -> pos <> (500, 0))
  |> List.of_seq |> List.length

let () =
  (* max y = 169, max x = 521 *)
  let rocks = Arg.read_arg "data/day14.txt" |> Array.to_list |> parse_rocks in
  part1 170 rocks |> string_of_int |> print_endline; (* 843 *)
  part2 171 rocks |> string_of_int |> print_endline; (* 27625 *)
