# ChezJS
JavaScript compile to Native Code (with Chez as backend)


```
(define print
    (lambda (lst)
        (display (eval (car lst)))
        (newline)
        (if (not (null? (cdr lst)))
            (print (cdr lst)))))
            
(print (chezjs "var i = 89; var j = 100; function f(x, y){ x + y;} f(i, j);"))
(print (chezjs "let a = 2; const b = 9; function f(x, y){ x * y;} f(a, b);"))
(print (chezjs "var o = 89; var p = 100; const q = 98; function f(x, y){ x = q;x + y;} f(o, p);"))
```
=>
```
#<void>
#<void>
#<void>
189
#<void>
#<void>
#<void>
18
#<void>
#<void>
#<void>
198
```
