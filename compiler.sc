(import 
    (match match)
    (core string))

(define parser
    (lambda ls
        (match ls
            ((var ,i #\= ,x #\;)`(define ,i ,x))
            ((let ,i #\= ,x #\;)`(define ,i ,x))
            ((const ,i #\= ,x #\;)`(define ,i ,x))
            ((function  #\( ,x #\) #\{ ,e #\})`(lambda (,x) ,e))
            ((function  #\( ,x #\, ,y #\) #\{ ,e #\})`(lambda (,x ,y) ,e))
            ((function ,f #\( ,x #\) #\{ ,e #\})`(define (,f ,x) ,e))
            ((function ,f #\( ,x #\, ,y #\) #\{ ,e #\})`(define (,f ,x ,y) ,e))
            ((,f #\( ,x #\) #\;)`(,f ,x))
            ((,f #\( ,x #\, ,y #\) #\;)`(,f ,x ,y)))))
           
            
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
                            (cond
                                ((or-equal? x #\( #\) #\[ #\] #\{ #\} #\, #\; #\: #\+ #\- #\* #\/ #\= #\> #\<)
                                    (cons #\space (cons x (cons #\space (loop y)))))
                                (else (cons x (loop y)))))))) #\space)))


(define p2
    (lambda (lst)
        (define f
            (lambda (str)
                (let ((x (string-ref str 0)))
                    (cond
                        ((or-equal? x #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9 #\0) (string->number str))
                        ((> (string-length str) 1)(string->symbol str))
                        ((or-equal? x #\( #\) #\[ #\] #\{ #\} #\, #\; #\: #\+ #\- #\* #\/ #\= #\> #\<)
                            (string-ref str 0))
                        (else (string->symbol str))))))
        (if (null? lst)
            '()
            (cons (f (car lst)) (p2 (cdr lst))))))



      
(eval (apply parser `,(p2 `,(p1 "var i= 89;"))))
(eval (apply parser `,(p2 `,(p1 "var j =100;"))))
(eval (parser 'function 'f #\( 'x #\, 'y #\) #\{ '(+ x y) #\}))
(display (eval (apply parser `,(p2 `,(p1 "f(i,j);")))))
