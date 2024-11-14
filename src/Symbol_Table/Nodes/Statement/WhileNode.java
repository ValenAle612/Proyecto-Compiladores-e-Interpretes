package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.BooleanType;

public class WhileNode extends StatementNode {

    protected Token token;
    protected ExpressionNode condition;
    protected StatementNode while_statement;

    protected static int num_while_label = 0;
    protected static int num_while_end_label = 0;

    public WhileNode(Token token, ExpressionNode condition, StatementNode while_statement){
        this.token = token;
        this.condition = condition;
        this.while_statement = while_statement;
    }

    @Override
    public void verify() throws SemanticException{

        if(!condition.verify().same_type(new BooleanType()))
            throw new SemanticException(token, "expression condition of while is not boolean");

        while_statement.verify();

    }

    @Override
    public void generate() {
        String while_label = new_while_label();
        String while_end_label = new_while_end_label();

        SymbolTable.generate(while_label + ":");
        condition.generate();
        SymbolTable.generate("BF " + while_end_label);
        while_statement.generate();
        SymbolTable.generate("JUMP " + while_label);
        SymbolTable.generate(while_end_label+": NOP");
    }

    private String new_while_label(){
        String new_label = "label_while" + num_while_label;
        num_while_label++;
        return new_label;
    }

    private String new_while_end_label(){
        String new_label = "label_end_while" + num_while_end_label;
        num_while_end_label++;
        return new_label;
    }

}
