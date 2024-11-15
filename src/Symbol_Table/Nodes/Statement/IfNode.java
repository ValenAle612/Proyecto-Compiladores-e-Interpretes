package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.BooleanType;

public class IfNode extends StatementNode {

    protected Token token;
    protected ExpressionNode condition;
    protected StatementNode if_statement;
    protected StatementNode else_statement;

    protected static int num_if_label = 0;
    protected static int num_else_label = 0;

    public IfNode(Token token, ExpressionNode condition, StatementNode if_statement){
        this.token = token;
        this.condition = condition;
        this.if_statement = if_statement;
    }

    public void insertElseStatement(StatementNode else_statement){
        this.else_statement = else_statement;
    }

    @Override
    public void verify() throws SemanticException{
        if(condition.verify().same_type(new BooleanType())){
            if_statement.verify();
            if(else_statement != null)
                else_statement.verify();
        }else
            throw new SemanticException(token, "boolean expression was expected");
    }

    @Override
    public void generate() {
        condition.generate();

        String out_if_label = new_if_label();
        String else_label = new_else_label();

        if(else_statement == null){
            SymbolTable.generate("BF " + out_if_label + " ; jumps out of the 'if', if the condition is false");
            if_statement.generate();
            SymbolTable.generate(out_if_label + ": NOP ");
        }else{
            SymbolTable.generate("BF " + else_label + " ; jumps to the 'else', the condition is false");
            if_statement.generate();
            SymbolTable.generate("JUMP " + out_if_label + " ; skip the 'else'");
            SymbolTable.generate(else_label+":");
            else_statement.generate();
            SymbolTable.generate(out_if_label + ": NOP");
        }

    }

    private String new_if_label(){
        String new_label = "out_if_label" + num_if_label;
        num_if_label++;
        return new_label;
    }

    private String new_else_label(){
        String new_label = "else_label" + num_else_label;
        num_else_label++;
        return new_label;
    }
}
