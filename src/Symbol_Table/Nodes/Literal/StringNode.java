package Symbol_Table.Nodes.Literal;

import Symbol_Table.ClassType;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;
import Symbol_Table.Nodes.Expression.OperandNode;
import Lexical_Analyzer.Token;

public class StringNode extends OperandNode {

    protected Token token;

    public StringNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return new ClassType(token);
    }

}
