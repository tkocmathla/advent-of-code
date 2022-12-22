module M = Map.Make(String)

let resolve l m =
  match l with
  | [ a; op; b ] -> begin
      let ax = (M.find a m) m in
      let bx = (M.find b m) m in
      match op with
      | "+" -> Int64.add ax bx
      | "-" -> Int64.sub ax bx
      | "*" -> Int64.mul ax bx
      | "/" -> Int64.div ax bx
      | _ -> 0L
    end
  | [ x ] -> Int64.of_string x
  | _ -> 0L

let parse lines =
  List.fold_left
    (fun m line ->
      if Str.string_match (Str.regexp ".*[0-9]$") line 0 then
        Scanf.sscanf line "%4s: %s" (fun a x -> M.add a (resolve [x]) m)
      else
        Scanf.sscanf line "%4s: %4s %1s %4s" (fun a b op c -> M.add a (resolve [b; op; c]) m))
  M.empty lines

let part1 m = (M.find "root" m) m

let part2 m =
  let rhs = (M.find "zfhn" m) m in
  Seq.iterate
    (fun (lo, hi, _) ->
      let x = Int64.add lo (Int64.div (Int64.sub hi lo) 2L) in
      let m' = M.add "humn" (resolve [Int64.to_string x]) m in
      let lhs = (M.find "jgtb" m') m' in
      if lhs < rhs then
        (lo, Int64.pred x, lhs)
      else
        (Int64.succ x, hi, lhs))
    (2199023255552L, 4398046511104L, 0L)
  |> Seq.drop_while (fun (_, _, lhs) -> lhs <> rhs)
  |> Seq.uncons
  |> function None -> 0L | Some ((lo, _, _), _) -> Int64.pred lo

let () =
  print_endline "";
  let m = Arg.read_arg "data/day21.txt" |> Array.to_list |> parse in

  Printf.printf "part1: %Ld\n" (part1 m); (* 309248622142100 *)
  Printf.printf "part2: %Ld\n" (part2 m); (* 3757272361782 *)

  print_endline ""
