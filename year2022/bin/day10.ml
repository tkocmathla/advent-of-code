let parse_cmd = function
  | [ "noop" ] -> None
  | [ "addx" ; x ] -> Some (int_of_string x, 2)
  | _ -> None

let rec parse_cmds = function
  | [] -> []
  | h :: t -> parse_cmd (String.split_on_char ' ' h) :: parse_cmds t

let draw reg cycle crt =
  let cyc = cycle mod 40 in
  let ch = if cyc >= reg && cyc <= reg + 2 then '#' else '.' in
  ch :: crt

let exe (reg, cycle, cmds, crt) =
  match cmds with
  | [] -> (reg, cycle, [], crt)
  | None :: cmds -> (reg, cycle + 1, cmds, draw reg cycle crt)
  | Some (x, left) :: cmds ->
      if left <= 1 then (reg + x, cycle + 1, cmds, draw reg cycle crt)
      else (reg, cycle + 1, Some (x, left - 1) :: cmds, draw reg cycle crt)

let part1 cmds =
  let res = Seq.iterate exe (1, 1, cmds, []) |> Seq.take 240 |> Array.of_seq in
  List.fold_left
    (fun sum x -> let (reg, cyc, _, _) = res.(x - 1) in sum + (reg * cyc))
    0 [20; 60; 100; 140; 180; 220]

let part2 cmds =
  Seq.iterate exe (1, 1, cmds, [])
  |> Seq.drop 240
  |> Seq.uncons
  |> function None -> () | Some ((_, _, _, crt), _) ->
       List.iteri (fun i ch -> print_char ch; if (i + 1) mod 40 = 0 then print_endline "") (List.rev crt)

let () =
  let cmds = Arg.read_arg "data/day10.txt" |> Array.to_list |> parse_cmds in

  print_endline "";
  part1 cmds |> string_of_int |> print_endline; (* 13760 *)
  part2 cmds; (* RFKZCPEF *)
  print_endline ""
