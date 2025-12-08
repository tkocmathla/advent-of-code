USING: arrays assocs hashtables io.encodings.utf8 io.files kernel locals math prettyprint ranges sequences sequences.extras vectors ;
IN: day5

: parse ( lines -- ranges ids )
    [ length zero? ] split-when first2
    [ string>number ] map
    swap
    [ [ CHAR: - = ] split-when [ string>number ] map ] map
    swap ;

! Part 1 -------------------------------------------------------

:: part1 ( ranges ids -- ans )
    ids [| id | ranges [ id swap first2 between? ] any? ] count ;

! Part 2 -------------------------------------------------------

: >sorted-intervals ( ranges -- ivs )
    [ first2 [a,b] ] map [ from>> ] sort-by ;

: fresh ( ivs -- x )
    [ interval>points [ first ] bi@ swap - 1 + ] map sum ;

: part2 ( ranges -- ans )
    >sorted-intervals { empty-interval }
    [| ivs curr |
        ivs last curr interval-union :> prev'
        ivs last curr interval-intersect empty-interval =
        [ ivs curr suffix ] [ ivs but-last prev' suffix ] if
    ] reduce
    rest
    fresh ;

! Tests --------------------------------------------------------

{ 3 } [ "day5/day5_test.txt" utf8 file-lines parse part1 ] unit-test
{ 712 } [ "day5/day5.txt" utf8 file-lines parse part1 ] unit-test

{ 14 } [ "day5/day5_test.txt" utf8 file-lines parse drop part2 ] unit-test
{ 332998283036769 } [ "day5/day5.txt" utf8 file-lines parse drop part2 ] unit-test
