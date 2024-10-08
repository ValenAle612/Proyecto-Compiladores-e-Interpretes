package Symbol_Table.Nodes.Expression.BE;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.BinaryExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class OrBENode extends BinaryExpressionNode {

    public OrBENode(Token token){
        super(token);
    }

    public Type verify() throws SemanticException {
        return null;
    }

}
