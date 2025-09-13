package parser.AST;

import java.util.LinkedList;

public class CommandAST extends AST {
    SingleCommandAST singleCommand;
    public linkedList<SingleCommandAST> singleCommandList;

    public CommandAST(SingleCommandAST sigleCommand) {
        this.singleCommand = sigleCommand;
        this.singleCommandList = new LinkedList<SingleCommandAST>();
    }
}
