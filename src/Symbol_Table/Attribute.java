package Symbol_Table;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;

public class Attribute {

    private Token attribute_token;
    private TokenId visibility;
    private Type attribute_type;

    public Attribute(Token attribute_token, TokenId visibility, Type attribute_type){
        this.attribute_token = attribute_token;
        this.visibility = visibility;
        this.attribute_type = attribute_type;
    }

    public void setAttribute_token(Token attribute_token){
        this.attribute_token = attribute_token;
    }

    public Token getAttribute_token(){
        return attribute_token;
    }

    public void setVisibility(TokenId visibility){
        this.visibility = visibility;
    }

    public TokenId getVisibility(){
        return visibility;
    }

    public void setAttribute_type(Type attribute_type){
        this.attribute_type = attribute_type;
    }

    public Type getAttribute_type(){
        return attribute_type;
    }

    public void is_well_stated() throws SemanticException {
        attribute_type.check_existence();
    }


}
