USING: arrays combinators io.encodings.utf8 io.files grouping locals kernel math math.parser prettyprint sequences sets splitting tools.test ;
IN: day2

: parse ( line -- ranges )
    "," split [ "-" split [ string>number ] map ] map ;

: singleton? ( seq -- ? ) >hash-set cardinality 1 = ;

:: solve-range ( f range -- n )
    range first2 [a..b] [
        dup f call( x -- ? ) [ ] [ drop 0 ] if
    ] map sum ;

:: solve ( f input -- n )
    input parse [ f swap solve-range ] map sum ;

! Part 1 -------------------------------------------------------

: halve ( str -- l r )
    dup length 2 /i
    [ head ] [ tail ] 2bi ;

: invalid? ( x -- ? )
    number>string
    [ halve = ] [ length even? ] [ singleton? ] tri
    and or ;

: part1 ( input -- n ) [ invalid? ] swap solve ;


! Part 2 -------------------------------------------------------

:: invalid2? ( x -- ? )
    x number>string :> s
    1 s length 2 /i [a..b] [ s swap group singleton? ] any?
    s length 1 > and ;

: part2 ( input -- n ) [ invalid2? ] swap solve ;


! Tests --------------------------------------------------------

{ 1227775554 } [ "day2/day2_test.txt" utf8 file-lines first part1 ] unit-test
{ 30608905813 } [ "day2/day2.txt" utf8 file-lines first part1 ] unit-test

{ 4174379265 } [ "day2/day2_test.txt" utf8 file-lines first part2 ] unit-test
{ 31898925685 } [ "day2/day2.txt" utf8 file-lines first part2 ] unit-test
