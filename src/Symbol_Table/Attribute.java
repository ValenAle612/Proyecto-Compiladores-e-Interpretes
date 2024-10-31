package Symbol_Table;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.Types.ConcreteType;
import Symbol_Table.Types.Type;

public class Attribute {

    private Token attribute_token;
    private TokenId visibility;
    private TokenId static_attribute;
    private ConcreteType attribute_type;
    private Token class_that_contains_the_attribute;

    public Attribute(Token attribute_token, TokenId visibility, TokenId static_attribute, ConcreteType attribute_type){
        this.attribute_token = attribute_token;
        this.visibility = visibility;
        this.static_attribute = static_attribute;
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

    public void setStatic_attribute(TokenId static_attribute){
        this.static_attribute = static_attribute;
    }

    public TokenId getStatic_attribute(){
        return static_attribute;
    }

    public void setAttribute_type(ConcreteType attribute_type){
        this.attribute_type = attribute_type;
    }

    public ConcreteType getAttribute_type(){
        return attribute_type;
    }

    public void is_well_stated() throws SemanticException{
        attribute_type.check_existence();
    }

    public void setClass_that_contains_the_attribute(Token classThatContainsTheAttribute){
        this.class_that_contains_the_attribute = classThatContainsTheAttribute;
    }

    public Token getClass_that_contains_the_attribute(){
        return class_that_contains_the_attribute;
    }

}
