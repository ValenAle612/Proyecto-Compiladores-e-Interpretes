package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.Types.NullType;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class NullNode extends OperandNode{

    protected Token token;

    public NullNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return new NullType();
    }

}
