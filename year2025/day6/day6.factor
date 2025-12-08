USING: arrays assocs hashtables io.encodings.utf8 io.files kernel locals math prettyprint ranges sequences sequences.extras unicode vectors ;
IN: day6

! Part 1 -------------------------------------------------------

:: parse1 ( lines -- eqs )
    lines [ [ blank? ] split-when [ empty? ] reject ] map 
    [ reverse ] map flip ;

:: solve1 ( eq -- x )
    eq [ but-last ] [ last ] bi :> ( xs op )
    xs [ string>number ] map [ rest ] [ first ] bi
    [ op "+" = [ + ] [ * ] if ] reduce ;

:: part1 ( eqs -- x ) eqs [ solve1 ] map sum ;

! Part 2 -------------------------------------------------------

: parse2 ( lines -- nums )
    [ >array ] map                          ! convert lines to char arrays
    [ reverse ] map flip                    ! rotate 90° ccw
    [ [ 32 = ] trim ] map                   ! drop leading and trailing spaces
    [ empty? ] split-when                   ! split on blank lines
    [ [ >string string>number ] map ] map ; ! convert char arrays to ints

:: solve2 ( eq -- x )
    eq first2 :> ( xs op )
    xs [ rest ] [ first ] bi
    [ op CHAR: + = [ + ] [ * ] if ] reduce ;

:: part2 ( lines -- x )
    lines [ but-last ] [ last ] bi :> ( strs ops )
    ops >array [ 32 = ] reject reverse :> ops'
    strs parse2 ops' zip
    [ solve2 ] map sum ;

! Tests --------------------------------------------------------

{ 4277556 } [ "day6/day6_test.txt" utf8 file-lines parse1 part1 ] unit-test
{ 5322004718681 } [ "day6/day6.txt" utf8 file-lines parse1 part1 ] unit-test

{ 3263827 } [ "day6/day6_test.txt" utf8 file-lines part2 ] unit-test
{ 9876636978528 } [ "day6/day6.txt" utf8 file-lines part2 ] unit-test
