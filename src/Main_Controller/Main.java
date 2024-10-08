package Main_Controller;

import Lexical_Analyzer.*;
import Source_Manager.SourceManager;
import Source_Manager.SourceManagerImpl;
import Symbol_Table.SemanticException;
import Syntax_Analyzer.SyntaxAnalyzer;
import Syntax_Analyzer.SyntaxException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main( String[] args ){
        try{
            String inputFile = args[0];
            boolean errors = false;

            if( args.length > 2 ){
                System.out.println("Incorrect number of parameters");
            }else{
                try{

                    SourceManager sourceManager = new SourceManagerImpl();
                    sourceManager.open(inputFile);
                    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
                    SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);

                    Token token = new Token(TokenId.initializer_token, "", 0);

                    if(!errors)
                        System.out.println( "Successful build\n \n[SinErrores]" );

                }catch ( FileNotFoundException e ){
                    System.out.println("ERROR: file not found");
                }catch ( IOException e ){
                    e.printStackTrace();
                } catch (LexicalException | SyntaxException | SemanticException e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch( ArrayIndexOutOfBoundsException e ){
            System.out.println(" ERROR: file name not specified or incorrect number of parameters ");
        }
    }

}
