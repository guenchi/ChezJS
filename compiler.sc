(library (jscheme compiler)
    (export
        p1
        p2
        p3
        p4
        p5
        p6
        p7
        p8
        p9
        p10
        p11
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
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x #\;)
                        (cons n k)
                        k)))))
    
    
    (define p4
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x #\=)
                        (cons n k)
                        k)))))
    
    (define p5
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x #\{)
                        (cons n k)
                        k)))))
    

    (define p6
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x #\})
                        (cons n k)
                        k)))))

    (define p7
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x #\()
                        (cons n k)
                        k)))))


    (define p8
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x #\))
                        (cons n k)
                        k)))))

    (define p9
        (lambda (lst)
            (let loop ((x (car lst))(y (cdr lst))(n 0))
                (let ((k 
                        (if (null? y)
                            '()
                            (loop (car y) (cdr y) (+ 1 n)))))
                    (if (equal? x 'function)
                        (cons n k)
                        k)))))
    
    (define p10
        (lambda (lst i j)
            (let loop ((lst lst)(n i))
                (if (> n j)
                    '()
                    (cons (list-ref lst n) (loop lst (+ 1 n)))))))
    
    
    (define p11
        (lambda (lst x)
            (let loop ((n 0)(x x))
                (cons (p10 lst n (car x))
                    (if (null? (cdr x))
                        '()
                        (loop (+ 1 (car x)) (cdr x)))))))
                                     
 
 
    (define parser
        (lambda ls
            (match ls
                ((,i #\= ,x #\;)`(set! ,i ,x))
                ((var ,i #\= ,x #\;)`(define ,i ,x))
                ((let ,i #\= ,x #\;)`(define ,i ,x))
                ((const ,i #\= ,x #\;)`(define ,i ,x))
                ((function  #\( ,x ... #\) #\{ ,e #\})`(lambda (,x ...) ,e))
                ((function ,f #\( ,x ... #\) #\{ ,e #\})`(define (,f ,x ...) ,e))
                ((,f #\( ,x ... #\) #\;)`(,f ,x ...)))))
 
 )
