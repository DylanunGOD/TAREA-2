package parser.AST;




public class ProgramAST extends AST {
SingleCommandAST singleCommand;
public ProgramAST(SingleCommandAST singleCommand) {
    this.singleCommand = singleCommand;
}
}
