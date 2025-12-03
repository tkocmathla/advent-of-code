USING: arrays combinators io.encodings.utf8 io.files locals kernel math math.parser prettyprint sequences tools.test ;
IN: day1

: parse ( line -- move )
    [ first CHAR: L = [ -1 ] [ 1 ] if ]
    [ rest string>number ]
    bi * ;

! Part 1 -------------------------------------------------------

: turn1 ( state move -- next-state )
    swap first2
    [ + 100 mod ] dip
    over zero? [ 1 + ] when
    2array ;

: part1 ( lines -- n )
    [ parse ] map { 50 0 } [ turn1 ] reduce second ;

! Part 2 -------------------------------------------------------

:: n-zeros ( move dial new-dial -- n )
    move abs 100 /i :> zs
    {
        { [ dial 0 = ] [ 0 ] }
        { [ move neg? new-dial dial > and ] [ 1 ] }
        { [ move 0 > new-dial dial < and ] [ 1 ] }
        { [ new-dial 0 = ] [ 1 ] }
        [ 0 ]
    } cond
    zs + ;

:: turn2 ( state move -- next-state )
    state first2        :> ( dial zeros )
    dial move + 100 rem :> new-dial

    new-dial
    move dial new-dial n-zeros zeros +
    2array ;

: part2 ( lines -- n )
    [ parse ] map { 50 0 } [ turn2 ] reduce second ;

! Tests --------------------------------------------------------

{ 3 } [ "day1/day1_test.txt" utf8 file-lines part1 ] unit-test
{ 1021 } [ "day1/day1.txt" utf8 file-lines part1 ] unit-test

{ 6 } [ "day1/day1_test.txt" utf8 file-lines part2 ] unit-test
{ 5933 } [ "day1/day1.txt" utf8 file-lines part2 ] unit-test
