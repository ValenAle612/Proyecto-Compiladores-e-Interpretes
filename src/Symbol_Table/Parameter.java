package Symbol_Table;

import Lexical_Analyzer.Token;
import Symbol_Table.Types.ConcreteType;
import Symbol_Table.Types.Type;

public class Parameter implements VarEntry{

    private Token parameter_token;
    private ConcreteType parameter_type;
    private int offset;

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

    public void set_type(ConcreteType parameter_type){
        this.parameter_type = parameter_type;
    }

    public void is_well_stated() throws SemanticException {
        parameter_type.check_existence();
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

    @Override
    public boolean is_attribute() {
        return false;
    }

    @Override
    public Type get_type() {
        return parameter_type;
    }

    @Override
    public int getOffset() {
        return offset;
    }
}
