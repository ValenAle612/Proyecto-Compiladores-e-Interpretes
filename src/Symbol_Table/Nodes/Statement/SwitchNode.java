package Symbol_Table.Nodes.Statement;

import Symbol_Table.SemanticException;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends StatementNode {

    private LocalVarNode variable;
    private List<CaseNode> cases;
    private StatementNode defaultCase;

    public SwitchNode(LocalVarNode variable) {
        this.variable = variable;
        this.cases = new ArrayList<>();
    }

    public void setCases(List<CaseNode> cases) {
        this.cases = cases;
    }

    public void setDefaultCase(StatementNode defaultCase) {
        this.defaultCase = defaultCase;
    }

    @Override
    public void verify() throws SemanticException {
        for (CaseNode caseNode : cases) {
            caseNode.verify();
        }

        if (defaultCase != null) {
            defaultCase.verify();
        }
    }
}
