USING: arrays assocs disjoint-sets grouping grouping.extras hash-sets hashtables io.encodings.utf8 io.files kernel locals math math.combinatorics memoize prettyprint ranges sets sequences sequences.extras vectors ;
IN: day11

:: parse-one ( line -- edges )
    line " " split [ first but-last ] [ rest ] bi :> ( src dsts )
    dsts [ src swap 2array ] map ;

: parse ( lines -- g )
    [ parse-one ] map-concat
    [ first ] sort-by [ first ] group-by
    [ first2 [ second ] map 2array ] map
    >hashtable ;

! Part 1 -------------------------------------------------------

MEMO:: dfs1 ( v g -- count )
    v "out" = [ 1 ] [ v g at [ g dfs1 ] map sum ] if ;

:: part1 ( g -- ans ) "you" g dfs1 ;

! Part 2 -------------------------------------------------------

MEMO:: dfs2 ( v dac? fft? g -- count )
    v "dac" = dac? or :> dac'
    v "fft" = fft? or :> fft'

    v "out" =
    [ dac' fft' and [ 1 ] [ 0 ] if ]
    [ v g at [ dac' fft' g dfs2 ] map sum ]
    if ;

:: part2 ( g -- ans ) "svr" f f g dfs2 ;

! Tests --------------------------------------------------------

{ 5 } [ "day11/day11_test1.txt" utf8 file-lines parse part1 ] unit-test
{ 555 } [ "day11/day11.txt" utf8 file-lines parse part1 ] unit-test

{ 2 } [ "day11/day11_test2.txt" utf8 file-lines parse part2 ] unit-test
{ 502447498690860 } [ "day11/day11.txt" utf8 file-lines parse part2 ] unit-test
