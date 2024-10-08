package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

import java.util.List;

public class MethodAccessNode extends AccessNode {

    protected List<ExpressionNode> current_parameters;
    protected Token token;

    public MethodAccessNode(List<ExpressionNode> current_parameters, Token token){
        this.current_parameters = current_parameters;
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return null;
    }

}
