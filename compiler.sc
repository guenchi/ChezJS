;  MIT License

;  Copyright guenchi (c) 2018 - 2019 
     
;  Permission is hereby granted, free of charge, to any person obtaining a copy
;  of this software and associated documentation files (the "Software"), to deal
;  in the Software without restriction, including without limitation the rights
;  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
;  copies of the Software, and to permit persons to whom the Software is
;  furnished to do so, subject to the following conditions:
     
;  The above copyright notice and this permission notice shall be included in all
;  copies or substantial portions of the Software.
     
;  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
;  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
;  SOFTWARE.






(library (chezjs compiler)
  (export
    p1
    p2
    p3
    p4
    p5
    p6
    chezjs
  )
  (import
    (chezscheme)
    (match match)
    (core string)
    (core condition))



 
  (define p1
    (lambda (str)
      (split*
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
              ((or/equal? x #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9 #\0) (string->number str))
              ((> (string-length str) 1)(string->symbol str))
              ((or/equal? x #\( #\) #\[ #\] #\{ #\} #\. #\; #\: #\+ #\- #\* #\/ #\= #\> #\<)
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
                              (or/equal? (list-ref lst n) #\} #\{ #\;))
                          (loop (list-tail lst n) (cons  (p4 (p3 lst 1 (- n 1))) k))
                          (l (+ 1 n)))))
                   ((equal? x #\})
                    (let l ((n 0)(m 0))
                      (if (equal? (list-ref lst n) #\{)
                          (if (or (= m len)
                                  (or/equal? (list-ref lst m) #\} #\;))
                              (loop (list-tail lst m)
                                (cons (append (p4 (p3 lst n (- m 1)))
                                  (list (p4 (p3 lst 1 (- n 1)))) (cons x '())) k))
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
                   
 
 
  (define p5
    (lambda (lst)
      (define Var
        (lambda (x)
          (match x
            (,v (guard (symbol? v)) v)
            (,n (guard (number? n)) n)
            ((,(Expr -> e)) e))))
      (define Expr
        (lambda (x)
          (match x
            ((,(Var -> x) #\+ ,(Var -> y)) `(+ ,x ,y))
            ((,(Var -> x) #\- ,(Var -> y)) `(- ,x ,y))
            ((,(Var -> x) #\* ,(Var -> y)) `(* ,x ,y))
            ((,(Var -> x) #\/ ,(Var -> y)) `(/ ,x ,y))
            ((,i #\= ,x)`(set! ,i ,x)))))
      (define Exprs
        (lambda (x)
          (match x
            ((,(Expr -> e)) e)
            ((,(Expr -> e1) ,(Expr -> e2) ...) 
              `(begin ,e1 ,e2 ...)))))
      (define Arg
        (lambda (x)
          (match x
            (,v (guard (symbol? v)) v)
            ((,(Func -> f)) f))))
      (define Test
        (lambda (x)
          (match x
            (ture #t)
            (false #f)
            ((,(Var -> v1) #\< ,(Var -> v2))
              (< v1 v2))
            ((,(Var -> v1) #\> ,(Var -> v2))
              (< v1 v2))
            ((,(Var -> v1) #\= ,(Var -> v2))
              (equal? v1 v2)))))
      (define Cond
        (lambda (x)
          (match x
            ((if #\( ,(Test -> t) #\) ,(Expr -> e))
              `(if ,t ,e))
            ((if #\( ,(Test -> t)  #\) #\{ ,(Exprs -> e) #\})
              `(if ,t ,e))
            ((if #\( ,(Test -> t)  #\) ,(Expr -> e1) else ,(Expr -> e2))
              `(if ,t ,e1 ,e2))
            ((if #\( ,(Test -> t)  #\) #\{ ,(Exprs -> e1) #\} else #\{ ,(Exprs -> e2) #\})
              `(if ,t ,e1 ,e2)))))
      (define Func
        (lambda (x)
          (match x
            ((function ,f #\( ,(Arg -> a) ... #\) #\{ ,(Exprs -> e) #\})
              `(define (,f ,a ...) ,e))
            ((function  #\( ,(Arg -> a) ... #\) #\{ ,(Exprs -> e) #\})
              `(lambda (,a ...) ,e))
            ((,(Arg -> a) #\= #\> ,(Expr -> e))
              `(lambda (,a) ,e))
            ((,(Arg -> a) #\= #\> #\{ ,(Exprs -> e) #\})
              `(lambda (,a) ,e))
            ((#\( ,(Arg -> a ...) #\) #\= #\> ,(Expr -> e))
              `(lambda (,a ...) ,e))
            ((#\( ,(Arg -> a ...) #\) #\= #\> #\{ ,(Exprs -> e) #\})
              `(lambda (,a ...) ,e)))))
      (match lst
        ((,i #\= ,x) `(set! ,i ,x))
        ((print #\( ,x #\)) `(display ,x))
        ((var ,i #\= ,x) `(define ,i ,x))
        ((let ,i #\= ,x) `(define ,i ,x))
        ((const ,i #\= ,x) `(define ,i ,x))
        ((,f #\( ,x ... #\)) `(,f ,x ...))
        (,(Func -> f) f)
        (,(Expr -> e) e))))


  (define p6
    (lambda (lst)
      (let ((x (car lst))(y (cdr lst)))
        (if (null? y)
          (cons (p5 x) '())
          (cons (p5 x)(p6 y))))))


  (define chezjs
    (lambda (str)
      (p6 (p4 (reverse (p2 (p1 str)))))))
                                    
                                    
 )
