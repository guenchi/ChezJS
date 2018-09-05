# ChezJS
JavaScript compile to Native Code (with Chez as backend)


```
(define x "var i = 89; var j = 100; function f (x , y){ x + y;} f(i, j);")
(eval (apply parser (car `,(p4 (reverse `,(p2 `,(p1 x)))))))
(eval (apply parser (cadr `,(p4 (reverse `,(p2 `,(p1 x)))))))
(eval (parser 'function 'f #\( 'x 'y #\) #\{ '(+ x y) #\}))
(display (eval (apply parser (cadddr `,(p4 `,(reverse `,(p2 `,(p1 x))))))))
(newline)
```

`=> 189`


==================PARSE   PROGRESS================

`"var i = 89; var j = 100; function f(x, y){ x + y;} f(i, j);"`

*p1*  -to list-

```
(#\v #\a #\r #\space #\i #\space #\= #\space #\8 #\9 #\;
 #\space #\v #\a #\r #\space #\j #\space #\= #\space #\1 #\0
 #\0 #\; #\space #\f #\u #\n #\c #\t #\i #\o #\n #\space #\f
 #\space #\( #\x #\space #\, #\space #\y #\) #\{ #\space #\x
 #\space #\+ #\space #\y #\; #\} #\space #\f #\( #\i #\,
 #\space #\j #\) #\;)
 ```
 
*p1*  -add space / delete , / delete entre-

```
(#\v #\a #\r #\space #\i #\space #\space #\= #\space #\space
 #\8 #\9 #\space #\; #\space #\space #\v #\a #\r #\space #\j
 #\space #\space #\= #\space #\space #\1 #\0 #\0 #\space #\;
 #\space #\space #\f #\u #\n #\c #\t #\i #\o #\n #\space #\f
 #\space #\space #\( #\space #\x #\space #\space #\space #\y
 #\space #\) #\space #\space #\{ #\space #\space #\x #\space
 #\space #\+ #\space #\space #\y #\space #\; #\space #\space
 #\} #\space #\space #\f #\space #\( #\space #\i #\space
 #\space #\j #\space #\) #\space #\space #\; #\space)
```
*p1* -to string-
 
`"var i  =  89 ;  var j  =  100 ;  function f  ( x   y )  {  x  +  y ;  }  f ( i  j )  ; "`
 
*p1* -split by space-

`("var" "i" "=" "89" ";" "var" "j" "=" "100" ";" "function" "f" "(" "x" "y" ")" "{" "x" "+" "y" ";" "}" "f" "(" "i" "j" ")" ";")`

*p2* -rebuild symbol and number-

`(var i #\= 89 #\; var j #\= 100 #\; function f #\( x y #\) #\{ x #\+ y #\; #\} f #\( i j #\) #\;)`

*reverse*

`(#\; #\) j i #\( f #\} #\; y #\+ x #\{ #\) y x #\( f function #\; 100 #\= j var #\; 89 #\= i var)`

*p4* -parse to list-

```
((var i #\= 89)
 (var j #\= 100)
 (function f #\( x y #\) #\{ 
     (x #\+ y)
 #\})
 (f #\( i j #\)))
```

*p5* -deconstruction list-   todo

```
(var i #\= 89)
(var j #\= 100)
(function f #\( x y #\) #\{ #\})
(x #\+ y)
(f #\( i j #\))
```

parser -match to Scheme-

```
(define i 89)
(define j 100)
(define (f x y) (+ x y))
(f i j)
```

Eval / Compile with Chez Scheme
