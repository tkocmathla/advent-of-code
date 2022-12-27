open Year2022.Common
module M = Map.Make(Int)

let dec_to_quin x =
  let seq = List.to_seq [ (-2, (0, -2)); (-1, (0, -1)); (0, (0, 0)); (1, (0, 1)); (2, (0, 2)); (3, (1, -2)); (4, (1, -1)); (5, (1, 0)) ] in
  let m = M.add_seq seq M.empty in
  M.find x m

let quin_to_dec xs =
  List.rev (List.of_seq xs)
  |> List.mapi (fun i x -> x * (power 5 i))
  |> List.fold_left ( + ) 0

let quin_to_int xs = Seq.map (function '-' -> -1 | '=' -> -2 | x -> int_from_char x) xs

let add xs ys =
  let rec aux res carry = function
    | (hx :: tx, []) -> let (c, v) = dec_to_quin (hx + carry) in aux (v :: res) c (tx, [])
    | (hx :: tx, hy :: ty) -> let (c, v) = dec_to_quin (hx + hy + carry) in aux (v :: res) c (tx, ty)
    | _ -> res
  in
  aux [] 0 (List.rev xs, List.rev ys)

let to_quinary_ints x =
  let rec aux l = function
    | 0 -> l
    | x' -> aux (dec_to_quin (x' mod 5) :: l) (x' / 5)
  in
  aux [] x

let part1 input =
  let dec = Array.fold_left (fun acc x -> acc + (quin_to_dec x)) 0 (Array.map quin_to_int input) in
  List.fold_left
    (fun acc (c, v) ->
      match acc with
      | [] -> if c = 0 then [c; v] else [c; v]
      | h :: t -> add (if c = 0 then [c; v] else [c; v]) [h] @ t)
  []
  (List.rev (to_quinary_ints dec))
  |> List.map (function -2 -> '=' | -1 -> '-' | 0 -> '0' | 1 -> '1' | 2 -> '2' | _ -> '_')
  |> List.to_seq |> String.of_seq

let () =
  print_endline "";
  let input = Arg.read_arg "data/day25.txt" |> Array.map String.to_seq in

  part1 input |> print_endline; (* 29698499442451 -> 2=-0=1-0012-=-2=0=01 *)

  print_endline "";
