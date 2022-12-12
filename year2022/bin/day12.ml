open Year2022.Common

let to_grid lines =
  let string_to_row s = String.to_seq s |> Array.of_seq |> Array.map int_of_char in
  Array.map string_to_row lines

let inside grid x y = y >= 0 && y < Array.length grid && x >= 0 && x < Array.length grid.(0)
let valid_loc grid height (x, y) = inside grid x y && height + 1 >= grid.(y).(x)
let rvalid_loc grid height (x, y) = inside grid x y && height - 1 <= grid.(y).(x)

let neighbors grid nbr_fn (x, y) =
  let height = grid.(y).(x) in
  List.fold_left (fun locs (dx, dy) ->
      if nbr_fn grid height (x + dx, y + dy) then
        (x + dx, y + dy) :: locs
      else locs)
  []
  [(0, -1) ; (1, 0) ; (0, 1) ; (-1, 0)]

let cost_or_max m loc = match PointMap.find_opt loc m with None -> Int.max_int | Some x -> x

let step nbr_fn (grid, costs, q) =
  let (x, y) = Queue.pop q in
  let cost = cost_or_max costs (x, y) in
  let costs = List.fold_left
    (fun costs (nx, ny) ->
      let ncost = cost_or_max costs (nx, ny) in
      if ncost > cost + 1 then begin
        Queue.add (nx, ny) q;
        PointMap.add (nx, ny) (cost + 1) costs
      end
      else costs)
    costs (neighbors grid nbr_fn (x, y)) in
  (grid, costs, q)

let bfs grid nbr_fn costs q =
  Seq.iterate (step nbr_fn) (grid, costs, q)
  |> Seq.drop_while (fun (_, _, q) -> Bool.not @@ Queue.is_empty q)
  |> Seq.uncons
  |> function None -> failwith "e" | Some ((_, costs, _), _) -> costs

let part1 (sx, sy) (ex, ey) grid =
  grid.(sy).(sx) <- int_of_char 'a';
  grid.(ey).(ex) <- int_of_char 'z';
  let q = Queue.create () in Queue.add (sx, sy) q;
  let costs = bfs grid valid_loc (PointMap.add (sx, sy) 0 PointMap.empty) q in
  PointMap.find (ex, ey) costs

let part2 (sx, sy) grid =
  grid.(sy).(sx) <- int_of_char 'z';
  let q = Queue.create () in Queue.add (sx, sy) q;
  let costs = bfs grid rvalid_loc (PointMap.add (sx, sy) 0 PointMap.empty) q in
  PointMap.to_seq costs
  |> Seq.fold_left
       (fun min ((x, y), cost) ->
         if grid.(y).(x) = int_of_char 'a' && cost < min then cost else min)
       Int.max_int

let () =
  print_endline "";
  let grid = Arg.read_arg "data/day12.txt" |> to_grid in

  part1 (0, 20) (154, 20) grid |> string_of_int |> print_endline; (* 484 *)
  part2 (154, 20) grid |> string_of_int |> print_endline; (* 478 *)
