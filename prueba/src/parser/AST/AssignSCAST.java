package parser.AST;

import org.antlr.v4.runtime.Token;

public class AssignSCAST extends SingleCommandAST {
Token identifier;
ExpressionAST expression;

    public AssignSCAST(Token identifier, ExpressionAST expression) {
        this.identifier = identifier;
        this.expresion = expression;
    }
}
