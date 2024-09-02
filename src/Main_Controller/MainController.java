package Main_Controller;

import Lexical_Analyzer.*;
import Source_Manager.SourceManager;
import Source_Manager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainController {

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

                    Token token = new Token(TokenId.initializing_token, "", 0);

                    do{

                        try{
                            token = lexicalAnalyzer.nextToken();
                            System.out.println("(" + token.getTokenType() + ", " + token.getLexeme() + ", " + token.getLineNumber() + ")\n");//(tokenId, lexeme, line number)
                        } catch ( LexicalException e ){
                            errors = true;
                            System.out.println( e.getMessage() );
                        }

                    }while( token.getTokenType() != TokenId.EOF );

                    if(!errors)
                        System.out.println( "[SinErrores]" );

                }catch ( FileNotFoundException e ){
                    System.out.println("ERROR: file not found");
                }catch ( IOException e ){
                    e.printStackTrace();
                }

            }
        }catch( ArrayIndexOutOfBoundsException e ){
            System.out.println(" ERROR: file name not specified or incorrect number of parameters ");
        }
    }

}
