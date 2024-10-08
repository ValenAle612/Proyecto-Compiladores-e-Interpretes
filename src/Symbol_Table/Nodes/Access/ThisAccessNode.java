package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class ThisAccessNode extends AccessNode{

    protected Token token;

    public ThisAccessNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return null;
    }

}