open Year2022.Common

module M = Map.Make(Int)

type monkey_t = { id : int; inspect: int -> int; target: int -> int; }

let parse_monkey f seq m =
  let lines = Array.of_seq seq in

  let id = Scanf.sscanf lines.(0) "Monkey %d:" (fun d -> d) in
  let items = Scanf.sscanf lines.(1) "  Starting items: %[0-9, ]" (fun s -> Str.split (Str.regexp ", ") s |> List.map int_of_string) in
  let (op, operand) = Scanf.sscanf lines.(2) "  Operation: new = old %c %s" (fun s d -> (s, d)) in
  let div = Scanf.sscanf lines.(3) "  Test: divisible by %d" Fun.id in
  let id_true = Scanf.sscanf lines.(4) "    If true: throw to monkey %d" Fun.id in
  let id_false = Scanf.sscanf lines.(5) "    If false: throw to monkey %d" Fun.id in

  let inspect = match op with
    | '*' -> (fun value -> f (value * (if operand = "old" then value else int_of_string operand)))
    | '+' -> (fun value -> f (value + (if operand = "old" then value else int_of_string operand)))
    | _ -> failwith "bad op"
  in
  let target_fun = (fun value -> if value mod div = 0 then id_true else id_false) in
  {id = id; inspect = inspect; target = target_fun}, M.add id (0, items) m

let parse_monkeys f (seq, monkeys, m) =
  let head = Seq.take 6 seq in
  let tail = Seq.drop 7 seq in
  let (monkey, m) = parse_monkey f head m in
  tail, monkey :: monkeys, m

let update m monkey =
  let (_, items) = M.find monkey.id m in
  List.fold_left (fun m item ->
    let (n', items') = M.find monkey.id m in
    let worry = monkey.inspect item in
    M.update (monkey.target worry) (function None -> Some (0, [worry]) | Some (n, l) -> Some (n, worry :: l)) m
    |> M.add monkey.id (n' + 1, (List.filter (( <> ) item) items')))
  m items

let round (monkeys, m) =
  Seq.drop 8 monkeys, Seq.fold_left update m (Seq.take 8 monkeys)

let score m =
  let counts = M.to_seq m |> Seq.map (fun (_, (n, _)) -> n) |> List.of_seq |> List.sort rcompare in
  match counts with
  | a :: b :: _ -> a * b
  | _ -> 0

let solve f n seq =
  let (_, monkeys, m) =
    Seq.iterate (parse_monkeys f) (seq, [], M.empty)
    |> Seq.drop 8 |> Seq.uncons |> function None -> failwith "derp" | Some (x, _) -> x in

  Seq.iterate round (Seq.cycle (List.to_seq (List.rev monkeys)), m)
  |> Seq.drop n
  |> Seq.uncons
  |> function None -> 0 | Some ((_, m), _) -> score m

let () =
  print_endline "";
  let seq = Arg.read_arg "data/day11.txt" |> Array.to_seq in

  solve (fun x -> x / 3) 20 seq |> string_of_int |> print_endline; (* 50830 *)
  solve (fun x -> x mod 9699690) 10000 seq |> string_of_int |> print_endline (* 14399640002 *)
