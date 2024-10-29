package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.Types.BooleanType;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class FalseNode extends BooleanNode {

    protected Token token;

    public FalseNode(Token token) {
        this.token = token;
    }

    public Type verify() throws SemanticException {
        return new BooleanType();
    }

}
