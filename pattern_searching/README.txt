Written by Tony Shu(ID: 1356141) and Liuying Cui(ID: 1344370)

Grammar:

E -> T
E -> TE
E -> T|E

T -> F
T -> F*
T -> F+
T -> F?

F -> .
F -> a (literal)
F -> \a
F -> (E)
F -> [ALTER]

although [] has the lowest precedence, we think its nature is basically the same as matching a literal,
this is why we set [] as a factor in our grammar.

Usage:
the command line argument to REcompiler must be enclosing within double-quote characters.
(i.e. "a*b*c*d*")

Limitation:
Our grammar does not allow concatenation before the alternation mark(i.e. '|'). so expression like "abc|d" would not work properly
(it will be looking for 'ab' followed by 'c' OR 'd', instead of 'abc' OR 'd'). Because concatenation is not a term in our grammar.
However, as you may have observed, this can be solved by simply adding a brackets to the preceding phrase before '|'
( i.e. "(abc)|d").
This is why we did not fix this error.

Working examples:
We have tested a quite complex expression and it seems work fine.
the expression we tested is :  "a((b*c?d+)|[?.\xyz])\+.e"
this expression consists of all required symbols in the specification. we have drawn a FSM diagram based on the output of our compiler, we believe it is correct
(although nobody would really use this wried expression)

we have also tested a few fairly simple expressions with BrownCorpus.txt, it seems work fine
the expression we tested are :
"(Brown)|text"
"th(is)|at"

Thanks for the help during this paper,
Cheers,
Tony & Liuying