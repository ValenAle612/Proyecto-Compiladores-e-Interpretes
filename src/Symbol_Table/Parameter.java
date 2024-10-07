package Symbol_Table;

import Lexical_Analyzer.Token;

public class Parameter {

    private Token parameter_token;
    private ConcreteType parameter_type;

    public Parameter(Token parameter_token, ConcreteType parameter_type){
        this.parameter_token = parameter_token;
        this.parameter_type = parameter_type;
    }

    public void setParameter_token(Token parameter_token){
        this.parameter_token = parameter_token;
    }

    public Token getParameter_token(){
        return parameter_token;
    }

    public void setParameter_type(ConcreteType parameter_type){
        this.parameter_type = parameter_type;
    }

    public ConcreteType getParameter_type(){
        return this.parameter_type;
    }

    public void is_well_stated() throws SemanticException {
        parameter_type.check_existence();
    }

}
