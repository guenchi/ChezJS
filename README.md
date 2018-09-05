# ChezJS
JavaScript compile to Native Code (with Chez as backend)


```
(define print
    (lambda (lst)
        (display (eval (car lst)))
        (newline)
        (if (not (null? (cdr lst)))
            (print (cdr lst)))))
```

```
(print (chezjs "var i = 89; var j = 100; function f(x, y){ x + y;} f(i, j);"))

=>

#<void>
#<void>
#<void>
189
```

```
(print (chezjs "var a = 2; var b = 9; function f(x, y){ x * y;} f(a, b);"))

=>

#<void>
#<void>
#<void>
18
```

```
(print (chezjs "var m = 110; var n = 21; function f(x, y){ x - y;} f(m, n);"))

=>

#<void>
#<void>
#<void>
89
```
