package Symbol_Table;

import Lexical_Analyzer.Token;
import Syntax_Analyzer.SyntaxException;

import java.util.HashMap;
import java.util.Map;

public abstract class Class {

    private Token class_token;
    private Token inherit_class_token;
    private Map<String, Attribute> attributes;
    private Map<String, Method> methods;
    private HashMap<String, Token> extended_class;
    private boolean is_consolidated;
    private boolean no_circular_inheritance;

    public abstract Token getToken();

    public abstract void save_attribute(Attribute attribute) throws SemanticException;

    public abstract void save_method(Method method) throws SemanticException;

    public abstract void is_well_stated() throws SemanticException, SyntaxException;

    public abstract HashMap<String, Token> getExtended_classes();

    public abstract void save_extends(String lexeme, Token token) throws SemanticException;

    public abstract boolean is_concrete_class();

}
