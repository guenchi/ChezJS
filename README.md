# ChezJS
JavaScript compile to Native Code (with Chez as backend)


JavaScript compile to Native Code (with Chez as backend)


```
(define print
    (lambda (lst)
        (display (eval (car lst)))
        (newline)
        (if (not (null? (cdr lst)))
            (print (cdr lst)))))
            
(print (chezjs "var i = 89;
                var j = 100;
                function f(x, y){
                    x + y;
                }
                f(i, j);"))
=>
#<void>
#<void>
#<void>
189

(print (chezjs "var o = 89;
                var p = 100;
                const q = 98;
                function f(x, y){
                    x = q;
                    x + y;
                } 
                f(o, p);"))
=>
#<void>
#<void>
#<void>
#<void>
#<void>
198

(print (chezjs "var x = 2;
                var y = 8;
                var o = 99;
                var p = 100;
                function f(x, y){
                    y = x;
                    x + y;
                }
                f(o, p);"))
#<void>
#<void>
#<void>
#<void>
#<void>
198                  
```


```
$ chezjs test.js
compiling test.js.sc with output to test.js.so
$ scheme test.js.so
```