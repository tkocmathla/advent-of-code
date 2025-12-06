USING: arrays assocs hashtables io.encodings.utf8 io.files kernel locals math prettyprint ranges sequences sequences.extras vectors ;
IN: day3

: index-pairs ( s -- v )
    >array [ 2array ] map-index
    [ [ first neg ] [ second ] bi 2array ] sort-by ;

:: solve-one ( pairs lo hi -- ans )
    pairs [ second dup lo >= swap hi < and ] find nip ;

:: solve ( pairs n -- ans )
    pairs length n - 1 + :> hi
    0 n [a..b)
    { 0 hi { } }
    [| state _ |
        state first3 :> ( lo hi chs )
        pairs lo hi solve-one first2 :> ( ch i )
        i 1 + dup hi 1 + max chs ch suffix 3array
    ] reduce
    last >string string>number ;

: part1 ( lines -- n ) [ index-pairs 2 solve ] map sum ;

: part2 ( lines -- n ) [ index-pairs 12 solve ] map sum ;

! Tests --------------------------------------------------------

{ 357 } [ "day3/day3_test.txt" utf8 file-lines part1 ] unit-test
{ 16858 } [ "day3/day3.txt" utf8 file-lines part1 ] unit-test

{ 3121910778619 } [ "day3/day3_test.txt" utf8 file-lines part2 ] unit-test
{ 167549941654721 } [ "day3/day3.txt" utf8 file-lines part2 ] unit-test
