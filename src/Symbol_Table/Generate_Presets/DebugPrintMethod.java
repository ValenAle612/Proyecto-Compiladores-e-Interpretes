package Symbol_Table.Generate_Presets;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.Method;
import Symbol_Table.Parameter;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.MethodType;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugPrintMethod extends Method {

    public DebugPrintMethod(HashMap<String, Parameter> parameters, ArrayList<Parameter> parameters_list, Token method_token,
                            TokenId static_method, MethodType methodType, Token associated_class){
        super(parameters, parameters_list, method_token, static_method, methodType, associated_class);
    }

    public void generate(){
        SymbolTable.generate("LOADFP");
        SymbolTable.generate("LOADSP");
        SymbolTable.generate("STOREFP");
        SymbolTable.generate("LOAD 3");
        SymbolTable.generate("IPRINT");
        SymbolTable.generate("STOREFP");
        SymbolTable.generate("RET "+1+" ; Returns deleting the parameter");
    }

}
