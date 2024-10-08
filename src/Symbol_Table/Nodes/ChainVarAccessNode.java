package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.ChainedNode;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

import java.util.List;

public class ChainVarAccessNode extends ChainedNode {

    protected Token token;
    protected ChainedNode chainedNode;

    public ChainVarAccessNode(Token token, ChainedNode chainedNode){
        this.token = token;
        this.chainedNode = chainedNode;
    }

    public Type verify() throws SemanticException {
        return null;
    }

}
