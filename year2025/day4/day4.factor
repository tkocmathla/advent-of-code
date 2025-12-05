USING: arrays assocs hashtables io.encodings.utf8 io.files kernel locals math prettyprint ranges sequences sequences.extras vectors ;
IN: day4
CONSTANT: dirs
    { { -1 -1 } { -1 0 } { -1 1 }
      {  0 -1 }          {  0 1 }
      {  1 -1 } {  1 0 } {  1 1 } }

:: cell ( grid row col -- ch ) row grid nth col swap nth ;

: paper? ( grid row col -- ? ) cell CHAR: @ = ;

:: in-bounds? ( grid row col -- ? )
    grid length :> rows
    0 grid nth length :> cols
    row 0 >= col 0 >= and
    row rows < and col cols < and ;

:: neighbors ( row col -- nbrs )
    dirs
    [| dxdy |
        dxdy first2 :> ( dx dy )
        row dy + col dx + 2array
    ] map ;

:: valid? ( grid row col -- ? )
    row col neighbors
    [| elt | grid elt first2 in-bounds? ] filter
    [| elt | grid elt first2 paper? ] count
    4 < ;

:: part1 ( grid -- n )
    0 grid length [a..b) [| row |
        0 0 grid nth length [a..b) [| col |
            grid row col [ paper? ] [ valid? ] 3bi and
        ] count
    ] map sum ;

! Tests --------------------------------------------------------

{ 13 } [ "day4/day4_test.txt" utf8 file-lines part1 ] unit-test
{ 1508 } [ "day4/day4.txt" utf8 file-lines part1 ] unit-test
