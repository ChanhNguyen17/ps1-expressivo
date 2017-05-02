//The IntegerExpression grammar
@skip whitespace{
    root ::= sum;
    sum ::= primitive ('+' primitive)*;
    primitive ::= (number|variable) | '(' sum ')';
}
whitespace ::= [ \t\r\n];
number ::= [0-9]+;
variable ::= [a-z]+;