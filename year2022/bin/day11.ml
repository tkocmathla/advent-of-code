module M = Map.Make(Int)

type monkey_t = { id : int; inspect: int -> int; target: int -> int; }

let parse_monkey seq m =
  let lines = Array.of_seq seq in

  let id = Scanf.sscanf lines.(0) "Monkey %d:" (fun d -> d) in
  let items = Scanf.sscanf lines.(1) "  Starting items: %[0-9, ]" (fun s -> Str.split (Str.regexp ", ") s |> List.map int_of_string) in
  let (op, operand) = Scanf.sscanf lines.(2) "  Operation: new = old %c %s" (fun s d -> (s, d)) in
  let div = Scanf.sscanf lines.(3) "  Test: divisible by %d" Fun.id in
  let id_true = Scanf.sscanf lines.(4) "    If true: throw to monkey %d" Fun.id in
  let id_false = Scanf.sscanf lines.(5) "    If false: throw to monkey %d" Fun.id in

  let inspect = match op with
    | '*' -> (fun value -> (value * (if operand = "old" then value else int_of_string operand)) / 3)
    | '+' -> (fun value -> (value + (if operand = "old" then value else int_of_string operand)) / 3)
    | _ -> failwith "bad op"
  in
  let target_fun = (fun value -> if value mod div = 0 then id_true else id_false) in

  {id = id; inspect = inspect; target = target_fun}, M.add id (0, items) m

let parse_monkeys (seq, monkeys, m) =
  let head = Seq.take 6 seq in
  let tail = Seq.drop 7 seq in
  let (monkey, m) = parse_monkey head m in
  tail, monkey :: monkeys, m

let update m monkey =
  let (_, items) = M.find monkey.id m in
  List.fold_left (fun m item ->
    let x = monkey.inspect item in
    M.update (monkey.target x) (function None -> Some (0, [x]) | Some (n, l) -> Some (n, (x :: l))) m
    |> M.update monkey.id (function None -> Some (0, []) | Some (n, l) -> Some (n + 1, (List.filter (fun x' -> x' <> item) l))))
  m items

let round (monkeys, m) =
  Seq.drop 8 monkeys, Seq.fold_left update m (Seq.take 8 monkeys)

let part1 seq =
  let (_, monkeys, m) = Seq.iterate parse_monkeys (seq, [], M.empty) |> Seq.drop 8 |> Seq.uncons |> function None -> failwith "" | Some (x, _) -> x in
  Seq.iterate round (Seq.cycle (List.to_seq (List.rev monkeys)), m)
  |> Seq.drop 20
  |> Seq.uncons
  |> function None -> () | Some ((_, m), _) -> M.to_seq m |> Seq.map (fun (_, (n, _)) -> n) |> List.of_seq |> List.sort compare |> List.rev |> List.to_seq |> Seq.take 2 |> Seq.fold_left ( * ) 1 |> string_of_int |> print_endline

let () =
  print_endline "";
  let seq = Arg.read_arg "data/day11.txt" |> Array.to_seq in

  part1 seq
