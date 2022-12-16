module PointSet = Set.Make (struct
  type t = int * int

  let compare = compare
end)

module PointMap = Map.Make (struct
  type t = int * int

  let compare = compare
end)

(* Converts the number represented by ch into an integer *)
let int_from_char ch = int_of_char ch - int_of_char '0'

(* Reverse comparator *)
let rcompare a b = compare b a

let ( -- ) i j =
  let rec aux n acc = if n < i then acc else aux (n - 1) (n :: acc) in
  aux j []

let range_from start stop =
  if start > stop then []
  else List.init (stop - start) succ |> List.map (fun x -> x + start - 1)

(* Returns a list of ints from 0 to n when n is > 0, or an empty list otherwise *)
let range n = if n < 0 then [] else range_from 0 n

(* https://clojuredocs.org/clojure.core/partition *)
let rec partition n step coll =
  let p = Seq.take n coll in
  if n = Seq.length p then Seq.cons p (partition n step (Seq.drop step coll))
  else Seq.empty

(* https://clojuredocs.org/clojure.core/partition-all *)
let rec partition_all n step coll =
  if Seq.is_empty coll then Seq.empty
  else Seq.cons (Seq.take n coll) (partition_all n step (Seq.drop step coll))
