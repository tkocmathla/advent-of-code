let score1 a b =
  match a with
  | "A" -> (match b with "X" -> 4 | "Y" -> 8 | "Z" -> 3 | _ -> 0)
  | "B" -> (match b with "X" -> 1 | "Y" -> 5 | "Z" -> 9 | _ -> 0)
  | "C" -> (match b with "X" -> 7 | "Y" -> 2 | "Z" -> 6 | _ -> 0)
  | _ -> 0

let score2 a b =
  match a with
  | "A" -> (match b with "X" -> 3 | "Y" -> 4 | "Z" -> 8 | _ -> 0)
  | "B" -> (match b with "X" -> 1 | "Y" -> 5 | "Z" -> 9 | _ -> 0)
  | "C" -> (match b with "X" -> 2 | "Y" -> 6 | "Z" -> 7 | _ -> 0)
  | _ -> 0

let solve f seq =
  Seq.map (String.split_on_char ' ') seq
  |> Seq.fold_left (fun acc l -> match l with a :: b :: _ -> acc + f a b | _ -> acc) 0

let () =
  let seq = Arg.read_arg "data/day2.txt" |> Array.to_seq in

  solve score1 seq |> string_of_int |> print_endline;
  solve score2 seq |> string_of_int |> print_endline
