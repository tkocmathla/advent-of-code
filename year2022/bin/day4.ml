let contains1 a b c d = c >= a && d <= b
let contains2 a b c d = (c <= a && d >= a) || (c <= b && d >= b)

let step f acc s =
  Scanf.sscanf s "%d-%d,%d-%d" (fun a b c d ->
      acc + Bool.to_int (f a b c d || f c d a b))

let solve f seq = Seq.fold_left (step f) 0 seq

let () =
  let seq = Arg.read_arg "data/day4.txt" |> Array.to_list |> List.to_seq in

  solve contains1 seq |> string_of_int |> print_endline;
  solve contains2 seq |> string_of_int |> print_endline
