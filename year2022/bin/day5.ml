open Year2022.Common (* for range *)

let filter_stacks = Seq.take_while (fun s -> String.length s > 0)
let filter_moves = Seq.filter (String.starts_with ~prefix:"move")

let parse_stacks seq =
  let lines = filter_stacks seq |> Array.of_seq in
  let nstacks = (String.length lines.(0) + 1) / 4 in
  let depth = Array.length lines - 1 in
  List.fold_left
    (fun stacks i ->
      List.iter
        (fun level ->
          match lines.(level).[1 + (4 * i)] with
          | 'A' .. 'Z' as ch -> Stack.push ch stacks.(i)
          | _ -> ())
        (range depth |> List.rev);
      stacks)
    (Array.init nstacks (fun _ -> Stack.create ()))
    (range nstacks)

let parse_move s = Scanf.sscanf s "move %d from %d to %d" (fun n src dst -> (n, src - 1, dst - 1))

let part1 stacks (n, src, dst) =
  List.iter
    (fun _ -> Stack.push (Stack.pop stacks.(src)) stacks.(dst))
    (range n)

let part2 stacks (n, src, dst) =
  let boxes = List.fold_left (fun l _ -> Stack.pop stacks.(src) :: l) [] (range n) in
  List.iter (fun box -> Stack.push box stacks.(dst)) boxes

let solve f seq =
  let stacks = parse_stacks seq in
  filter_moves seq
  |> Seq.iter (fun cmd -> (f stacks (parse_move cmd)));
  Array.map Stack.pop stacks

let () =
  let seq = Arg.read_arg "data/day5.txt" |> Array.to_list |> List.to_seq in

  solve part1 seq |> Array.iter print_char;
  print_endline "";
  solve part2 seq |> Array.iter print_char
