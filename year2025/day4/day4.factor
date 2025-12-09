USING: arrays assocs hashtables io.encodings.utf8 io.files kernel locals math prettyprint ranges sequences sequences.extras sets vectors ;
IN: day4
CONSTANT: dxdys
    { { -1 -1 } { -1 0 } { -1 1 }
      {  0 -1 }          {  0 1 }
      {  1 -1 } {  1 0 } {  1 1 } }

:: parse-one ( line row -- papers )
    line [ CHAR: @ = ] find-all [ first row swap 2array ] map ;

: parse ( lines -- papers )
    [ parse-one ] map-index concat >hash-set ;

:: neighbor ( row col dxdy -- nbr )
    dxdy first2 :> ( dx dy )
    row dy + col dx + 2array ;

:: neighbors ( row col -- nbrs )
    dxdys [ row col rot neighbor ] map >hash-set ;

:: solve ( papers -- kills )
    papers members
    [| paper |
        paper first2 neighbors :> nbrs
        nbrs papers intersect cardinality 4 <
    ] filter >hash-set ;

: part1 ( papers -- x ) solve cardinality ;

:: part2 ( papers -- papers )
    papers
    [| papers |
        papers solve :> kills
        papers kills diff :> papers'
        papers' kills cardinality 0 >
    ]
    [ dup ] produce nip :> acc

    acc { papers 0 }
    [| state next |
        state first2 :> ( prev x )
        prev next diff cardinality x + :> kills
        { next kills }
    ]
    reduce second ;

! Tests --------------------------------------------------------

{ 13 } [ "day4/day4_test.txt" utf8 file-lines parse part1 ] unit-test
{ 1508 } [ "day4/day4.txt" utf8 file-lines parse part1 ] unit-test

{ 43 } [ "day4/day4_test.txt" utf8 file-lines parse part2 ] unit-test
{ 8538 } [ "day4/day4.txt" utf8 file-lines parse part2 ] unit-test
