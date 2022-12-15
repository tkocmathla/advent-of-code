let parse_input lines =
  let to_pairs x1 y1 x2 y2 = ((x1, y1), (x2, y2)) in
  let parse s = Scanf.sscanf s "Sensor at x=%d, y=%d: closest beacon is at x=%d, y=%d" to_pairs in
  List.fold_left (fun l s -> (parse s) :: l) [] lines

let part1 y sensors =
  List.fold_left
    (fun l ((sx, sy), (bx, by)) ->
      let dist = Int.abs (sx - bx) + Int.abs (sy - by) in
      if dist >= Int.abs (sy - y) then
        let to_y = Int.abs (sy - y) in
        let dx = Int.abs (dist - to_y) in
        (sx - dx, sx + dx) :: l
      else l)
    [] sensors
  |> List.fold_left
       (fun (min, max) (rmin, rmax) -> (Int.min min rmin, Int.max max rmax))
       (Int.max_int, Int.min_int)
  |> fun (min, max) -> max - min

let () =
  print_endline "";
  let sensors = Arg.read_arg "data/day15.txt" |> Array.to_list |> parse_input in

  part1 2000000 sensors |> string_of_int |> print_endline (* 4876693 *)
