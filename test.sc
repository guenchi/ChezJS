(import (jscheme compiler))

(eval (apply parser `,(p2 `,(p1 "var i= 89;"))))		                   
(eval (apply parser `,(p2 `,(p1 "var j =100;"))))		                         
(eval (parser 'function 'f #\( 'x #\, 'y #\) #\{ '(+ x y) #\}))		                            
(display (eval (apply parser `,(p2 `,(p1 "f(i,j);")))))
