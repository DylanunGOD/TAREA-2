package parser;

import generated.pruebaLexer;
import org.antlr.v4.runtime.Token;
import parser.AST.ExpressionAST;

import java.util.ArrayList;

public class pruebaParserMANUAL {
    private Token actualToken;
    private pruebaLexer theLexer;

    private ArrayList<String> errorList;
    private static final String[] tokenNames = pruebaLexer.tokenNames;

    public pruebaParserMANUAL(pruebaLexer theLexer) {
        this.theLexer = theLexer;
        this.actualToken = this.theLexer.nextToken();
        this.errorList = new ArrayList<>();

    }

    private void accept(int expectedToken) {
        if (this.actualToken.getType() != expectedToken) {
            reportError("Token inesperado", expectedToken);
            acceptIt();
        } else {
            this.actualToken = this.theLexer.nextToken();
        }
    }

    private void acceptIt() {
        this.actualToken = this.theLexer.nextToken();
    }


    private void reportError(String msg, int... expectedTokens) {
        String expected;
        if (expectedTokens.length == 0) {
            expected = "ninguno";
        } else {
            expected = java.util.Arrays.stream(expectedTokens)
                    .mapToObj(t -> {

                        if (t == Token.EOF) {
                            return "'EOF'";
                        }
                        return "'" + tokenNames[t] + "'";
                    })
                    .collect(java.util.stream.Collectors.joining(" o "));
        }

        String found = (actualToken.getType() == Token.EOF) ? "EOF" : "'" + tokenNames[actualToken.getType()] + "'";

        this.errorList.add(
                String.format("Error de Sintaxis: %s. Se esperaba %s pero se encontró %s. En (%d,%d)",
                        msg,
                        expected,
                        found,
                        this.actualToken.getLine(),
                        this.actualToken.getCharPositionInLine() + 1)
        );
    }

    //program  ::= singleCommand
    public void parseProgram() {
        parseCommand();
        accept(pruebaLexer.EOF);
    }


    //command  ::= singleCommand (; singleCommand)*
    private void parseCommand() {
        parseSingleCommand();
        while (this.actualToken.getType() == pruebaLexer.PyCOMA) {
            acceptIt();
            parseSingleCommand();
        }
    }


    //singleCommand ::=
    //Identifier  (:= expression | ( expression ))
    private void parseSingleCommand() {
        if (this.actualToken.getType() == pruebaLexer.ID) {
            acceptIt();
            if (this.actualToken.getType() == pruebaLexer.ASSIGN) {
                acceptIt();
                parseExpression();
            } else if (this.actualToken.getType() == pruebaLexer.PIZQ) {
                acceptIt();
                parseExpression();
                accept(pruebaLexer.PDER);

            } else {
                reportError("Se esperaba una asignacion o una llamada a funcion", pruebaLexer.ASSIGN, pruebaLexer.PIZQ);
            }

        } else if (this.actualToken.getType() == pruebaLexer.IF) {
            acceptIt();
            parseExpression();
            accept(pruebaLexer.THEN);
            parseSingleCommand();
            accept(pruebaLexer.ELSE);
            parseSingleCommand();
        } else if (this.actualToken.getType() == pruebaLexer.WHILE) {
            acceptIt();
            parseExpression();
            accept(pruebaLexer.DO);
            parseSingleCommand();
        } else if (this.actualToken.getType() == pruebaLexer.LET) {
            acceptIt();
            parseDeclaration();
            accept(pruebaLexer.IN);
            parseSingleCommand();
        } else if (this.actualToken.getType() == pruebaLexer.BEGIN) {
            acceptIt();
            parseCommand();
            accept(pruebaLexer.END);
        } else
            reportError("Comando invalido", pruebaLexer.ID, pruebaLexer.IF, pruebaLexer.WHILE, pruebaLexer.LET, pruebaLexer.BEGIN);
    }


    //declaration  ::= singleDeclaration (; singleDeclaration)*
    private void parseDeclaration() {
        parseSingleDeclaration();
        while (this.actualToken.getType() == pruebaLexer.PyCOMA) {
            acceptIt();
            parseSingleDeclaration();
        }
    }


    //singleDeclaration ::=
    //const Identifier ~ expression
    //    	   | var Identifier : typeDenoter
    private void parseSingleDeclaration() {
        if (this.actualToken.getType() == pruebaLexer.CONST) {
            acceptIt();
            accept(pruebaLexer.ID);
            accept(pruebaLexer.VIR);
            parseExpression();
        } else if (this.actualToken.getType() == pruebaLexer.VAR) {
            acceptIt();
            accept(pruebaLexer.ID);
            accept(pruebaLexer.DOSPUNT);
            typeDenoter();
        } else
            reportError("Se esperaba una declaracion 'const' o 'var'", pruebaLexer.CONST, pruebaLexer.VAR);

    }

    //typeDenoter ::= Identifier
    private void typeDenoter() {

        accept(pruebaLexer.ID);
    }

    //expression ::= primaryExpression (operator primaryExpression)*
    private void parseExpression() { //revisar el codigo
        parsePrimaryExpression();
        while (isOperator(this.actualToken.getType())) {
            parseOperator();
            parsePrimaryExpression();
        }
    }


    //primaryExpression ::= Literal | Identifier | ( expression )
    private void parsePrimaryExpression() {
        if (this.actualToken.getType() == pruebaLexer.LITERAL) {
            acceptIt();
        } else if (this.actualToken.getType() == pruebaLexer.ID) {
            acceptIt();
        } else if (this.actualToken.getType() == pruebaLexer.PIZQ) {
            acceptIt();
            parseExpression();
            accept(pruebaLexer.PDER);
        } else {
            reportError("Se esperaba un Literal, un Identificador o una expresion entre parentesis", pruebaLexer.LITERAL, pruebaLexer.ID, pruebaLexer.PIZQ);
        }
    }


    //operator ::= + | - | * | / | % | == | != | < | > | <= | >=
    private void parseOperator() {
        if (isOperator(this.actualToken.getType())) {
            acceptIt();
        } else {
            reportError("Se esperaba un operador",
                    pruebaLexer.SUM, pruebaLexer.SUB, pruebaLexer.MUL, pruebaLexer.DIV,
                    pruebaLexer.MOD, pruebaLexer.EQEQ, pruebaLexer.NOTEQ, pruebaLexer.MENOR,
                    pruebaLexer.MAYOR, pruebaLexer.MENOREQ, pruebaLexer.MAYOREQ);
        }
    }

    private boolean isOperator(int tokenType) {
        return tokenType == pruebaLexer.SUM ||
                tokenType == pruebaLexer.SUB ||
                tokenType == pruebaLexer.MUL ||
                tokenType == pruebaLexer.DIV ||
                tokenType == pruebaLexer.MOD ||
                tokenType == pruebaLexer.EQEQ ||
                tokenType == pruebaLexer.NOTEQ ||
                tokenType == pruebaLexer.MENOR ||
                tokenType == pruebaLexer.MAYOR ||
                tokenType == pruebaLexer.MENOREQ ||
                tokenType == pruebaLexer.MAYOREQ;
    }

    public ArrayList<String> getErrorList() {
        return this.errorList;
    }


    private ExpressionAST parseExpression() {
        ExpressionAST itsAST = null;

        return itsAST;
    }

    public ProgramAST parseProgram() {
        ProgramAST istAST = null;

        SingleCommandAST sc = parseSingleCommand();
        istAST = new ProgramAST(sc);
        return istAST;
    }


    //command  ::= singleCommand (; singleCommand)*
    private CommandAST parseCommand() {
        CommandAST istAST = null; //se repite en todos
        SingleCommandAST sc1 = parseSingleCommand();
        istAST = new CommandAST(sc1);
        while (this.actualToken.getType() == AlphaCompilerLexer.PyCOMA) {
            acceptIt();
            istAST.singleCommandList.add(parseSingleCommand());
        }

        return istAST;
    }


    //singleCommand ::=
    //Identifier  (:= expression | ( expression ))
    private SingleCommandAST parseSingleCommand() {
        SingleCommandAST istAST = null;

        if (this.actualToken.getType() == AlphaCompilerLexer.ID) {
            Token id = this.actualToken;
            acceptIt();
            if (this.actualToken.getType() == AlphaCompilerLexer.ASSIGN) {
                acceptIt();
                ExpressionAST expr = parseExpression();
                istAST = new AssignSCAST(id, expr);
            } else if (this.actualToken.getType() == AlphaCompilerLexer.PIZQ) {
                acceptIt();
                ExpressionAST expr = parseExpression();
                accept(AlphaCompilerLexer.PDER);
                istAST = new CallSCAST(id, expr);


            }
        }
        return istAST;
    }
}


