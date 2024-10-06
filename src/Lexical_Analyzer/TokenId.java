package Lexical_Analyzer;

public enum TokenId {
        //Keywords
        kw_class, kw_extends, kw_public, kw_private, kw_static, kw_void,
        kw_boolean, kw_char, kw_int,
        kw_if, kw_else, kw_while, kw_return, kw_var,
        kw_switch, kw_case, kw_default,
        kw_this, kw_new, kw_null, kw_true, kw_false,

        //Operators
        op_greaterThan, op_lessThan, op_not,
        op_equals, op_greaterThanEqual, op_lessThanEqual, op_notEquals,
        op_add, op_substract, op_multiply, op_divide,
        op_and, op_or, op_mod,
        op_assignment, op_assignmentAdition, op_assignmentSubstraction,

        //End of File
        EOF,

        //Literals
        literal_integer, literal_char, literal_String,

        //Identifiers
        class_id, method_var_id,

        //Punctuation Symbols
        ps_openParenthesis, ps_closeParenthesis,
        ps_openBrace, ps_closeBrace,
        ps_semicolon, ps_comma, ps_dot, ps_colon,

        //just for initialize the token
        initializer_token

}
