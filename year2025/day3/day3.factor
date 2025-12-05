USING: arrays assocs hashtables io.encodings.utf8 io.files kernel locals math prettyprint ranges sequences sequences.extras vectors ;
IN: day3

:: imap ( s -- m )
    H{ } clone :> m
    s [| ch i | i ch m push-at ] each-index
    m ;

:: left ( m s -- i )
    CHAR: 0 CHAR: 9 [a..b] reverse
    [| ch | ch m at length 0 > [ ch m at ] [ { } ] if ] map-concat
    [| i | i s length 1 - = not ] find swap drop ;

:: right ( m i -- j )
    CHAR: 0 CHAR: 9 [a..b] reverse
    [| ch | ch m at length 0 > [ ch m at ] [ { } ] if ] map-concat
    [| j | j i > ] find swap drop ;

:: numberify ( i j s -- x )
    i s nth
    j s nth
    2array >string string>number ;

:: jolts ( s -- ij )
    s imap :> m
    m s left :> i
    m i right :> j
    i j s numberify ;

: part1 ( batteries -- ns ) [ jolts ] map sum ;

! Tests --------------------------------------------------------

{ 357 } [ "day3/day3_test.txt" utf8 file-lines part1 ] unit-test
{ 16858 } [ "day3/day3.txt" utf8 file-lines part1 ] unit-test
