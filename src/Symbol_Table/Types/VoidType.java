package Symbol_Table.Types;

import Symbol_Table.SemanticException;

public class VoidType extends Type {

    public VoidType(){ }

    @Override
    public String getCurrentType() {
        return "void";
    }

    @Override
    public boolean is_subtype_of(MethodType type) throws SemanticException {
        return type.verify_if_is_subtype(this);
    }

    public boolean verify_if_is_subtype(VoidType voidType){
        return true;
    }

    public boolean verify_type(VoidType voidType){
        return true;
    }

}
