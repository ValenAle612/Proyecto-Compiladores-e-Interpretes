package Symbol_Table.Nodes.Statement;

import Symbol_Table.Nodes.Access.VarAccessNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends StatementNode {

    private final VarAccessNode variable;
    private List<CaseNode> cases;
    private StatementNode defaultCase;

    protected static int num_switch_label;
    protected static int num_case_label;

    public SwitchNode(VarAccessNode variable) {
        this.variable = variable;
        this.cases = new ArrayList<>();
        num_switch_label = 0;
        num_case_label = 0;
    }

    public void setCases(List<CaseNode> cases) {
        this.cases = cases;
    }

    public void setDefaultCase(StatementNode defaultCase) {
        this.defaultCase = defaultCase;
    }

    public VarAccessNode getVariable(){
        return variable;
    }

    @Override
    public void verify() throws SemanticException {

        variable.setType(variable.verify());

        for (CaseNode caseNode : cases) {
            caseNode.setSwitch_condition_type(variable.getType());
            caseNode.verify();
        }

        if (defaultCase != null) {
            defaultCase.verify();
        }
    }

    @Override
    public void generate() {

        String end_switch_label = new_switch_label();
        List<String> case_labels = new ArrayList<>();

        for(int i = 0; i < cases.size(); i++)//creating case labels
            case_labels.add(new_case_label());

        int case_num = 0;
        String default_label = "default_label";

        for(CaseNode caseNode : cases){

            SymbolTable.generate(case_labels.get(case_num) + ":");

            SymbolTable.generate("LOAD "+variable.getLocalVarNode().getOffset());
            caseNode.getLiteral().generate();
            SymbolTable.generate("EQ");

            if(case_num+1 < case_labels.size())
                SymbolTable.generate("BF " + case_labels.get(case_num+1));
            else if(default_label !=null)
                SymbolTable.generate("BF " + default_label);

            cases.get(case_num).generate();
            SymbolTable.generate("JUMP "+end_switch_label);

            case_num++;
        }

        if(defaultCase != null) {
            SymbolTable.generate(default_label + ":");
            defaultCase.generate();
            SymbolTable.generate("JUMP " + end_switch_label);
        }

        SymbolTable.generate(end_switch_label+": NOP");

    }

    public String new_switch_label(){
        return "end_switch_label"+(num_switch_label++);
    }

    public String new_case_label(){
        return "case_label"+(num_case_label++);
    }

}
