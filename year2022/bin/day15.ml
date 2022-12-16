open Year2022.Common

let parse_input lines =
  let to_pairs x1 y1 x2 y2 = ((x1, y1), (x2, y2)) in
  let parse s = Scanf.sscanf s "Sensor at x=%d, y=%d: closest beacon is at x=%d, y=%d" to_pairs in
  List.fold_left (fun l s -> (parse s) :: l) [] lines

let coverages y sensors =
  List.fold_left
    (fun l ((sx, sy), (bx, by)) ->
      let dist = Int.abs (sx - bx) + Int.abs (sy - by) in
      if dist >= Int.abs (sy - y) then
        let to_y = Int.abs (sy - y) in
        let dx = Int.abs (dist - to_y) in
        (sx - dx, sx + dx) :: l
      else l)
    [] sensors

let part1 y sensors =
  coverages y sensors
  |> List.fold_left (fun (min, max) (x1, x2) -> Int.min min x1, Int.max max x2) (Int.max_int, Int.min_int)
  |> fun (min, max) -> max - min

let coalesce ranges =
  let rec step min1 max1 = function
    | (min2, max2) :: rest ->
        if max1 <> Int.min_int && max1 < min2 then
          (min1, max1) :: step min2 (Int.max max1 max2) rest
        else
          step (Int.min min1 min2) (Int.max max1 max2) rest
    | [] -> [(min1, max1)]
  in
  step Int.max_int Int.min_int ranges

let part2 extent sensors =
  Seq.iterate succ 0 |> Seq.take extent
  |> Seq.find_map
       (fun y ->
         coverages y sensors |> List.sort compare |> coalesce |> List.to_seq
         |> partition 2 1
         |> Seq.find_map
              (fun p ->
                match List.of_seq p with
                | [(_, x1); (x2, _)] -> if x1 < x2 then Some (x1 + 1) else None
                | _ -> None)
         |> function Some x -> Some (x * extent + y) | _ -> None)
  |> function Some ans -> ans | _ -> failwith ""

let () =
  print_endline "";
  let sensors = Arg.read_arg "data/day15.txt" |> Array.to_list |> parse_input in

  part1 2000000 sensors |> string_of_int |> print_endline; (* 4876693 *)
  part2 4000000 sensors |> string_of_int |> print_endline (* 11645454855041 *)
