open Year2022.Common
module S = Set.Make (Char)

let windows4 l =
  let rec step l acc =
    match l with
    | a :: b :: c :: d :: t -> step (b :: c :: d :: t) ([ a; b; c; d ] :: acc)
    | _ -> acc
  in
  step l [] |> List.rev

let windows14 l =
  let rec step l acc =
    match l with
    | a :: b :: c :: d :: e :: f :: g :: h :: i :: j :: k :: l :: m :: n :: t ->
        step
          (b :: c :: d :: e :: f :: g :: h :: i :: j :: k :: l :: m :: n :: t)
          ([ a; b; c; d; e; f; g; h; i; j; k; l; m; n ] :: acc)
    | _ -> acc
  in
  step l [] |> List.rev

let solve f n l =
  List.combine (f l) (range (List.length l - (n - 1)))
  |> List.find (fun (win, _) -> S.of_list win |> S.cardinal |> ( = ) n)
  |> fun (_, i) -> i + n

let part1 = solve windows4 4
let part2 = solve windows14 14

let () =
  let seq = Arg.read_arg "data/day6.txt" |> Array.to_list |> List.hd |> String.to_seq |> List.of_seq in

  part1 seq |> string_of_int |> print_endline;
  part2 seq |> string_of_int |> print_endline
