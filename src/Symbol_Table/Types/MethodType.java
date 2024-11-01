package Symbol_Table.Types;

import Symbol_Table.SemanticException;

public abstract class MethodType {
    public abstract String getCurrentType();

    public void check_existence() throws SemanticException { }

    public boolean same_type(Type type){
        return this.getClass() == type.getClass();
    }

    public boolean same_type(ClassType type){
        return false;
    }

    public abstract boolean is_subtype_of(MethodType type) throws SemanticException;

    protected boolean verify_if_is_subtype(CharType type){
        return false;
    }

    protected boolean verify_if_is_subtype(BooleanType type){
        return false;
    }

    protected boolean verify_if_is_subtype(VoidType type){
        return false;
    }

    protected boolean verify_if_is_subtype(IntType type){
        return false;
    }

    protected boolean verify_if_is_subtype(ClassType type) throws SemanticException {
        return false;
    }

}
