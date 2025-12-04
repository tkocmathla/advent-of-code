USING: arrays combinators io.encodings.utf8 io.files grouping locals kernel math math.parser prettyprint sequences sets splitting tools.test ;
IN: day2

: parse ( line -- ranges )
    "," split [ "-" split [ string>number ] map ] map ;

! Part 1 -------------------------------------------------------

: halve ( str -- l r )
    dup length 2 /i
    [ head ] [ tail ] 2bi ;

: invalid? ( x -- ? )
    number>string
    [ halve = ] [ length even? ] [ >hash-set cardinality 1 = ] tri
    and or ;

: solve ( range -- n )
    first2 [a..b] [ dup invalid? [ ] [ drop 0 ] if ] map sum ;

: part1 ( line -- n ) parse [ solve ] map sum ;

! Part 2 -------------------------------------------------------

:: invalid2? ( x -- ? )
    x number>string :> s
    1 s length 2 /i [a..b] [
        s swap group >hash-set cardinality 1 =
    ] any?
    s length 1 > and ;

: solve2 ( range -- n )
    first2 [a..b] [ dup invalid2? [ ] [ drop 0 ] if ] map sum ;

: part2 ( line -- n ) parse [ solve2 ] map sum ;

! Tests --------------------------------------------------------

{ 1227775554 } [ "day2/day2_test.txt" utf8 file-lines first part1 ] unit-test
{ 30608905813 } [ "day2/day2.txt" utf8 file-lines first part1 ] unit-test

{ 4174379265 } [ "day2/day2_test.txt" utf8 file-lines first part2 ] unit-test
{ 31898925685 } [ "day2/day2.txt" utf8 file-lines first part2 ] unit-test
