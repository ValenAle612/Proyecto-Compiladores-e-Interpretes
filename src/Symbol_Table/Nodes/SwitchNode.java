package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends StatementNode {

    protected Token token;
    protected ExpressionNode variable;
    protected List<CaseNode> case_nodes;
    protected DefaultNode defaultNode;

    public SwitchNode(Token token, ExpressionNode variable){
        this.token = token;
        this.variable = variable;
        this.case_nodes = new ArrayList<>();
        this.defaultNode = null;
    }

    public Token getToken(){
        return token;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public void insertCaseBlock(CaseNode caseNode){
        case_nodes.add(caseNode);
    }

    public void setDefaultNode(DefaultNode defaultNode){
        this.defaultNode = defaultNode;
    }

    public void verify() throws SemanticException {

    }


}
