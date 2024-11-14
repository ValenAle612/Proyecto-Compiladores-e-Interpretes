package Symbol_Table.Nodes.Statement;

import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends StatementNode {

    private LocalVarNode variable;
    private List<CaseNode> cases;
    private StatementNode defaultCase;

    protected static int num_switch_label = 0;

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

    @Override
    public void generate() {
        variable.generate();

        String end_switch_label = new_switch_label();
        List<String> case_labels = new ArrayList<>();

        for(CaseNode caseNode : cases){
            String case_label = new_switch_label();
            case_labels.add(case_label);

            SymbolTable.generate("DUP");
            caseNode.getLiteral().generate();
            SymbolTable.generate("EQ");
            SymbolTable.generate("BF "+case_label);
        }

        if(defaultCase != null)
            defaultCase.generate();

        SymbolTable.generate("JUMP "+end_switch_label);

        for(int i = 0; i < cases.size(); i++){
            SymbolTable.generate(case_labels.get(i) + ": NOP");
            cases.get(i).generate();
            SymbolTable.generate("JUMP "+end_switch_label);
        }

        SymbolTable.generate(end_switch_label+": NOP");

    }

    public String new_switch_label(){
        return "switch_label"+(num_switch_label++);
    }

}
