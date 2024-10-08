package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.IntType;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class IntNode extends OperandNode {

    protected Token token;

    public IntNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return new IntType();
    }

}
