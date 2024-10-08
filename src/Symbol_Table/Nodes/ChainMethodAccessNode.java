package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.ChainedNode;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

import java.util.List;

public class ChainMethodAccessNode extends ChainedNode {

    protected Token token;
    protected List<ExpressionNode> expressionNodeList;
    protected ChainedNode chainedNode;

    public ChainMethodAccessNode(Token token, List<ExpressionNode> expressionNodeList, ChainedNode chainedNode){
        this.token = token;
        this.expressionNodeList = expressionNodeList;
        this.chainedNode = chainedNode;
    }

    public Type verify() throws SemanticException{
        return null;
    }

}
