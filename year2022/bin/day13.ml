type 'a tree_t = Value of int | Tree of 'a tree_t list

let untag = function Value _ -> failwith "err" | Tree t -> t

let to_tree str =
  let rec to_tree_ str tree =
    if String.length str = 0 then
      (str, tree)
    else if str.[0] = '[' then
      let (rtail, rtree) = to_tree_ (Str.string_after str 1) (Tree []) in
      to_tree_ rtail (Tree (rtree :: (untag tree)))
    else if str.[0] = ']' then
      (Str.string_after str 1, tree)
    else if str.[0] = ',' then
      to_tree_ (Str.string_after str 1) tree
    else
      let (x, tail) = Scanf.sscanf str "%d%s" (fun x s -> (x, s)) in
      to_tree_ tail (Tree (Value(x) :: (untag tree)))
  in
  let _, tree = to_tree_ str (Tree []) in tree

let rec tree_cmp ltree rtree =
  match (ltree, rtree) with
  | Tree l, Tree r -> begin
      let l = List.rev l in
      let r = List.rev r in
      Seq.iterate (fun (cmps, l', r') ->
        match (l', r') with
        | [], [] -> (0 :: cmps, [], [])
        | [], _ -> (-1 :: cmps, [], [])
        | _, [] -> (1 :: cmps, [], [])
        | lh :: lt, rh :: rt -> (tree_cmp lh rh :: cmps, lt, rt))
      ([], l, r)
      |> Seq.drop ((Int.min (List.length l) (List.length r)) + 1)
      |> Seq.uncons
      |> function None -> 0 | Some ((cmps, _, _), _) -> List.rev cmps
      |> List.find_opt (( <> ) 0)
      |> function None -> 0 | Some cmp -> cmp
    end
  | Value a, Value b -> compare a b
  | Value a, Tree _ -> tree_cmp (Tree [Value a]) rtree
  | Tree _, Value b -> tree_cmp ltree (Tree [Value b])

let rec make_pairs = function
  | a :: b :: _ :: tail -> (a, b) :: (make_pairs tail)
  | a :: b :: _ -> [(a, b)]
  | _ -> []

let part1 lines =
  make_pairs lines
  |> List.mapi (fun i (l, r) -> match tree_cmp (to_tree l) (to_tree r) with -1 -> i + 1 | _ -> 0)
  |> List.fold_left ( + ) 0

let part2 lines =
  List.filter (fun s -> String.length s > 0) ("[[2]]" :: "[[6]]" :: lines)
  |> List.map to_tree
  |> List.sort tree_cmp
  |> List.mapi (fun i l -> if l = (to_tree "[[2]]") || l = (to_tree "[[6]]") then i + 1 else 1)
  |> List.fold_left ( * ) 1

(*let rec print_tree = function*)
(*  | Value x -> print_string (Printf.sprintf "%d," x)*)
(*  | Tree t -> print_char '['; List.iter print_tree (List.rev t); print_string "],"*)

let () =
  print_endline "";
  let lines = Arg.read_arg "data/day13.txt" |> Array.to_list in

  part1 lines |> string_of_int |> print_endline; (* 5292 *)
  part2 lines |> string_of_int |> print_endline; (* 23868 *)

  print_endline ""
