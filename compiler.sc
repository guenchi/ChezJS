(library (jscheme compiler)
    (export
        p1
        p2
        p3
        p4
        parser
    )
    (import
        (scheme)
        (match match)
        (core string))



    (define-syntax or-equal?
        (syntax-rules ()
            ((_ x e1)(or (equal? x e1)))
            ((_ x e1 e2 ...)(or (equal? x e1)(equal? x e2) ...))))

 
    (define p1
        (lambda (str)
            (split 
                (list->string
                    (let loop ((lst (string->list str)))
                        (if (null? lst)
                            '()
                            (let ((x (car lst))(y (cdr lst)))
                                (case x
                                    ((#\( #\) #\[ #\] #\{ #\} #\. #\; #\: #\+ #\- #\* #\/ #\= #\> #\<)
                                        (cons #\space (cons x (cons #\space (loop y)))))
                                    (#\, (cons #\space (loop y)))
                                    (#\xA (loop y))
                                    (else (cons x (loop y)))))))) #\space)))
 
 
 
    (define p2
        (lambda (lst)
            (define f
                (lambda (str)
                    (let ((x (string-ref str 0)))
                        (cond
                            ((or-equal? x #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9 #\0) (string->number str))
                            ((> (string-length str) 1)(string->symbol str))
                            ((or-equal? x #\( #\) #\[ #\] #\{ #\} #\. #\; #\: #\+ #\- #\* #\/ #\= #\> #\<)
                                x)
                            (else (string->symbol str))))))
            (if (null? lst)
                '()
                (cons (f (car lst)) (p2 (cdr lst))))))
 
 

    (define p3
        (lambda (lst i j)
            (let loop ((lst lst)(n i))
                (if (> n j)
                    '()
                    (cons (list-ref lst n) (loop lst (+ 1 n)))))))


    (define p4
        (lambda (lst)
            (let loop ((lst lst)(k '()))
            (if (not (null? lst))
                (let ((x (car lst))(y (cdr lst))(len (length lst)))
                    (cond 
                        ((char? x)
                            (cond
                                ((equal? x #\;)
                                    (let l ((n 1))
                                        (if (or (= n len)
                                                (or-equal? (list-ref lst n) #\} #\{ #\;))
                                            (loop (list-tail lst n) (cons  (p4 (p3 lst 1 (- n 1))) k))
                                            (l (+ 1 n)))))
                                ((equal? x #\})
                                    (let l ((n 0)(m 0))
                                        (if (equal? (list-ref lst n) #\{)
                                            (if (or (= m len)
                                                (or-equal? (list-ref lst m) #\} #\;))
                                                (loop (list-tail lst m)
                                                        (cons (append (p4 (p3 lst n (- m 1)))
                                                          (p4 (p3 lst 1 (- n 1))) (cons x '())) k))
                                                (l n (+ 1 m)))
                                            (l (+ 1 n)(+ 1 m)))))
                                (else (loop y (cons x k)))))
                        ((symbol? x)
                            (cond
                                ((equal? x 'else)
                                    (loop y (cons x k)))
                                ((equal? x 'return)
                                    (loop y (cons x k)))
                                (else (loop y (cons x k)))))
                        (else (loop y (cons x k)))))
                    k))))
                                     
 
 
    (define parser
        (lambda ls
            (match ls
                ((,i #\= ,x)`(set! ,i ,x))
                ((var ,i #\= ,x)`(define ,i ,x))
                ((let ,i #\= ,x)`(define ,i ,x))
                ((const ,i #\= ,x)`(define ,i ,x))
                ((function  #\( ,x ... #\) #\{ ,e #\})`(lambda (,x ...) ,e))
                ((function ,f #\( ,x ... #\) #\{ ,e #\})`(define (,f ,x ...) ,e))
                ((,f #\( ,x ... #\))`(,f ,x ...)))))
 
 )
