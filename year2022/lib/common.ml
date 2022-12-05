module PointSet = Set.Make (struct
  type t = int * int

  let compare = compare
end)

(* Returns a range of ints from 0 to n *)
let range n = List.init n succ |> List.map (fun x -> x - 1)
