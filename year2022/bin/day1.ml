let step (sum, sums) line =
  match line with
  | "" -> (0, List.cons sum sums)
  | x -> (sum + int_of_string x, sums)

let topn n sums =
  List.sort compare sums |> List.rev |> List.to_seq |> Seq.take n
  |> Seq.fold_left (+) 0

let solve n seq = Seq.fold_left step (0, []) seq |> fun (_, sums) -> topn n sums

let () =
  let seq = Arg.read_arg "data/day1.txt" |> Array.to_seq in

  solve 1 seq |> string_of_int |> print_endline;
  solve 3 seq |> string_of_int |> print_endline
