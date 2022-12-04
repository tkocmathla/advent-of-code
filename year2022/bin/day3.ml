module S = Set.Make (Char)

let seq2set seq = Seq.fold_left (fun set ch -> S.add ch set) S.empty seq
let list2set l = List.fold_right S.add l S.empty
let str2set s = String.to_seq s |> List.of_seq |> list2set

let rec partition = function
  | a :: b :: c :: t -> List.cons [ a; b; c ] (partition t)
  | l -> [ l ]

let score ch =
  match ch with 'a' .. 'z' -> Char.code ch - 96 | _ -> Char.code ch - 38

let part1 s =
  let mid = String.length s / 2 in
  let seq = String.to_seq s in
  S.inter (Seq.take mid seq |> seq2set) (Seq.drop mid seq |> seq2set)
  |> S.find_first (fun _ -> true)
  |> score

let part2 = function
  | a :: b :: c :: _ ->
      S.inter (S.inter (str2set a) (str2set b)) (str2set c)
      |> S.find_first (fun _ -> true)
      |> score
  | _ -> 0

let solve f seq = Seq.map f seq |> Seq.fold_left ( + ) 0

let () =
  let l = Arg.read_arg "data/day3.txt" |> Array.to_list in

  List.to_seq l |> solve part1 |> string_of_int |> print_endline;
  partition l |> List.to_seq |> solve part2 |> string_of_int |> print_endline
