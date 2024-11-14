package Symbol_Table;

import Symbol_Table.Types.Type;

public interface VarEntry{

    public boolean is_attribute();

    public Type get_type();

    public int getOffset();

}
