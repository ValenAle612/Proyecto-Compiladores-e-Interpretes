package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.CharType;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class CharNode extends OperandNode {

    protected Token token;

    public CharNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException {
        return new CharType();
    }

}
