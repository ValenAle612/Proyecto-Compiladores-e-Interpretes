package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.BooleanType;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class TrueNode extends BooleanNode {

    protected Token token;

    public TrueNode(Token token) {
        this.token = token;
    }

    public Type verify() throws SemanticException {
        return new BooleanType();
    }

}