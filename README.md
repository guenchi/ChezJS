# ChezJS
JavaScript compile to Native Code (with Chez as backend)

Compile to .so file:

```
$ chezjs test.js
compiling test.js.sc with output to test.js.so
$ scheme test.js.so
```

REPL:

```
$ chezjs
>const i = 89;
#<void>
>var k = 100;
#<void>
>function f(x, y){x + y;}
#<void>
>f(i, k);
189
>
```