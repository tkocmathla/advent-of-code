module PointSet = Set.Make (struct
  type t = int * int

  let compare = compare
end)

let int_from_char ch = int_of_char ch - int_of_char '0'

let range_from start stop =
  if start > stop then []
  else List.init (stop - start) succ |> List.map (fun x -> x + start - 1)

(* Returns a list of ints from 0 to n when n is > 0, or an empty list otherwise *)
let range n = if n < 0 then [] else range_from 0 n
