module PointSet = Set.Make (struct
  type t = int * int

  let compare = compare
end)
