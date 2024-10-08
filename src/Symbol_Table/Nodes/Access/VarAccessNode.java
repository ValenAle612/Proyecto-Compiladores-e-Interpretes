package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class VarAccessNode extends AccessNode{

    protected Token token;

    public VarAccessNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return null;
    }

}