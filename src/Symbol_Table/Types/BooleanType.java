package Symbol_Table.Types;

import Symbol_Table.SemanticException;

public class BooleanType extends PrimitiveType {

    public BooleanType(){ }

    @Override
    public String getCurrentType(){
        return "boolean";
    }

    @Override
    public boolean is_subtype_of(MethodType type) throws SemanticException {
        return type.verify_if_is_subtype(this);
    }

    public boolean verify_if_is_subtype(BooleanType booleanType){
        return true;
    }

}
