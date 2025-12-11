USING: arrays assocs disjoint-sets hash-sets hashtables io.encodings.utf8 io.files kernel locals math math.combinatorics memoize prettyprint ranges sets sequences sequences.extras vectors ;
IN: day8

: parse-one ( line -- xyz ) "," split [ string>number ] map ;

: parse ( lines -- xyzs ) [ parse-one ] map ;

: dist ( xyz1 xyz2 -- d ) zip [ first2 - 2 ^ ] map sum sqrt ;

: pairs ( xyzs -- pairs )
    2 all-combinations
    [ dup first2 dist 2array ] map
    [ second ] sort-by
    [ first ] map ;

:: update-ckts ( ckts xyz1 xyz2 -- )
    xyz1 ckts disjoint-set-member? not [ xyz1 ckts add-atom ] when
    xyz2 ckts disjoint-set-member? not [ xyz2 ckts add-atom ] when
    xyz1 xyz2 ckts equate ;

! Part 1 -------------------------------------------------------

:: part1 ( xyzs n -- ans )
    xyzs pairs n head
    <disjoint-set> [ dupd first2 update-ckts ] reduce
    counts>> values sort 3 tail* product ;

! Part 2 -------------------------------------------------------

:: singleton? ( ckts n -- ? ) ckts counts>> values supremum n = ;

:: part2 ( xyzs n -- ans )
    <disjoint-set> :> ckts
    xyzs pairs
    [| pairs |
        ckts pairs first first2 update-ckts
        pairs rest-slice ckts n singleton? not
    ] [ dup first ] produce nip last
    [ first ] map product ;

! Tests --------------------------------------------------------

{ 40 } [ "day8/day8_test.txt" utf8 file-lines parse 10 part1 ] unit-test
{ 129564 } [ "day8/day8.txt" utf8 file-lines parse 1000 part1 ] unit-test

{ 25272 } [ "day8/day8_test.txt" utf8 file-lines parse 20 part2 ] unit-test
{ 42047840 } [ "day8/day8.txt" utf8 file-lines parse 1000 part2 ] unit-test
