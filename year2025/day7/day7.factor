USING: arrays assocs hash-sets hashtables io.encodings.utf8 io.files kernel locals math memoize prettyprint ranges sets sequences sequences.extras vectors ;
IN: day7

: init-beam ( line -- col ) [ CHAR: S = ] find drop ;

! Part 1 -------------------------------------------------------

: find-splitters ( line -- cols )
    [ CHAR: ^ = ] find-all [ first ] map ;

: split-beams ( cols -- cols' )
    [ [ 1 - ] [ 1 + ] bi 2array ] map-concat ;

:: part1 ( lines -- x )
    lines first init-beam 1array >hash-set :> beams
    lines rest { 0 beams }
    [| state line |
        state first2 :> ( x beams )
        line find-splitters :> splitters
        beams splitters intersect :> split-cols

        x split-cols cardinality +

        beams splitters diff
        split-cols split-beams
        append

        2array
    ] reduce first ;

! Part 2 -------------------------------------------------------

MEMO:: dfs ( lines col -- x )
    {
        { [ lines length zero? ] [ 1 ] }
        { [ col lines first nth CHAR: ^ = ] [
            lines rest col 1 - dfs
            lines rest col 1 + dfs
            +
        ] }
        [ lines rest col dfs ]
    } cond ;

: part2 ( lines -- ans ) [ rest ] [ first init-beam ] bi dfs ;

! Tests --------------------------------------------------------

{ 21 } [ "day7/day7_test.txt" utf8 file-lines part1 ] unit-test
{ 1638 } [ "day7/day7.txt" utf8 file-lines part1 ] unit-test

{ 40 } [ "day7/day7_test.txt" utf8 file-lines part2 ] unit-test
{ 7759107121385 } [ "day7/day7.txt" utf8 file-lines part2 ] unit-test
