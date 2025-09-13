package parser.AST;

import org.antlr.v4.runtime.Token;

public class CallAST extends AST {
    Token indentifier;
    ExpressionAST expression;

    public CallAST(Token indentifier, ExpressionAST expression) {
        this.indentifier = indentifier;
        this.expression = expression;
    }
}
