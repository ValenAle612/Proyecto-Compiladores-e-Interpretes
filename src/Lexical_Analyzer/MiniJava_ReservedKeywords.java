package Lexical_Analyzer;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MiniJava_ReservedKeywords {

    private Map<String, TokenId> dictionary = new Hashtable<String, TokenId>();

    public MiniJava_ReservedKeywords(){
        putKeywords();
    }

    public TokenId getTokenId(String token){
        return this.dictionary.get(token);
    }

    private void putKeywords(){
        dictionary.put("class", TokenId.kw_class);
        dictionary.put("extends", TokenId.kw_extends);
        dictionary.put("public", TokenId.kw_public);
        dictionary.put("static", TokenId.kw_static);
        dictionary.put("void", TokenId.kw_void);

        dictionary.put("boolean", TokenId.kw_boolean);
        dictionary.put("char", TokenId.kw_char);
        dictionary.put("int", TokenId.kw_int);

        dictionary.put("if", TokenId.kw_if);
        dictionary.put("else", TokenId.kw_else);
        dictionary.put("while", TokenId.kw_while);
        dictionary.put("return", TokenId.kw_return);
        dictionary.put("var", TokenId.kw_var);

        dictionary.put("switch", TokenId.kw_switch);
        dictionary.put("case", TokenId.kw_case);
        dictionary.put("brake", TokenId.kw_break);

        dictionary.put("this", TokenId.kw_this);
        dictionary.put("new", TokenId.kw_new);
        dictionary.put("null", TokenId.kw_null);
        dictionary.put("true", TokenId.kw_true);
        dictionary.put("false", TokenId.kw_false);
    }

}
