# ChezJS
JavaScript compile to Native Code (with Chez as backend)


```
(define print
    (lambda (lst)
        (display (eval (car lst)))
        (newline)
        (if (not (null? (cdr lst)))
            (print (cdr lst)))))
            
(print (chezjs "var i = 89; var j = 100; function f (x , y){ x + y;} f(i, j);"))
```

```
#<void>
#<void>
#<void>
189
```


