USING: arrays assocs disjoint-sets grouping hash-sets hashtables io.encodings.utf8 io.files kernel locals math math.combinatorics memoize prettyprint ranges sets sequences sequences.extras vectors ;
IN: day10

: parse-lites ( str -- lites )
    >array rest but-last [ CHAR: # = ] map ;

: parse-butts ( strs -- butts )
    [ >array rest but-last >string "," split [ string>number ] map ] map ;

:: parse-jolts ( str -- jolts ) str ;

: parse-one ( line -- mach )
    " " split
    [ first ] [ rest but-last ] [ last ] tri
    [ parse-lites ] [ parse-butts ] [ parse-jolts ] tri*
    3array ;

: parse ( lines -- machs ) [ parse-one ] map ;

! Part 1 -------------------------------------------------------

: push-butt ( lites butt -- lites )
    swap clone
    [| acc i |
        i acc nth 1 xor >boolean
        i acc set-nth
        acc
    ] reduce ;

:: push-butts ( all-lites butts -- lites )
    all-lites [| curr |
        butts [| butt | curr butt push-butt ] map
    ] map-concat ;

:: solve-one ( target butts -- x )
    0 <hash-set>
    target [ drop f ] map 1array >hash-set
    2array
    [| state |
        state first2 :> ( seen q )

        seen q union!
        q members butts push-butts >hash-set seen diff!
        2array

        target q in? not
    ]
    [ dup ]
    produce nip length ;

: part1 ( machs -- ans ) [ first2 solve-one ] map sum ;

! Tests --------------------------------------------------------

{ 7 } [ "day10/day10_test.txt" utf8 file-lines parse part1 ] unit-test
{ 455 } [ "day10/day10.txt" utf8 file-lines parse part1 ] unit-test
