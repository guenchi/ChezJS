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
