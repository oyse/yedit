// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g 2009-09-01 21:19:08

package org.dadacoalition.yedit.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class YAMLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DOCUMENT_START", "NEWLINE", "DOCUMENT_END", "DOUBLEQUOTE", "STRING", "WHITESPACE", "SINGLEQUOTE", "NORMAL_CHARS"
    };
    public static final int NEWLINE=5;
    public static final int SINGLEQUOTE=10;
    public static final int DOCUMENT_START=4;
    public static final int WHITESPACE=9;
    public static final int DOCUMENT_END=6;
    public static final int NORMAL_CHARS=11;
    public static final int EOF=-1;
    public static final int DOUBLEQUOTE=7;
    public static final int STRING=8;

    // delegates
    // delegators


        public YAMLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public YAMLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return YAMLParser.tokenNames; }
    public String getGrammarFileName() { return "/home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g"; }



    // $ANTLR start "file"
    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:16:1: file : ( document )* ;
    public final void file() throws RecognitionException {
        try {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:17:3: ( ( document )* )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:17:5: ( document )*
            {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:17:5: ( document )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==DOCUMENT_START) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:17:5: document
            	    {
            	    pushFollow(FOLLOW_document_in_file39);
            	    document();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "file"


    // $ANTLR start "document"
    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:19:1: document : DOCUMENT_START NEWLINE ( node NEWLINE )? DOCUMENT_END ( NEWLINE )? ;
    public final void document() throws RecognitionException {
        try {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:20:3: ( DOCUMENT_START NEWLINE ( node NEWLINE )? DOCUMENT_END ( NEWLINE )? )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:20:5: DOCUMENT_START NEWLINE ( node NEWLINE )? DOCUMENT_END ( NEWLINE )?
            {
            match(input,DOCUMENT_START,FOLLOW_DOCUMENT_START_in_document52); 
            match(input,NEWLINE,FOLLOW_NEWLINE_in_document54); 
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:20:28: ( node NEWLINE )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==DOUBLEQUOTE||LA2_0==SINGLEQUOTE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:20:30: node NEWLINE
                    {
                    pushFollow(FOLLOW_node_in_document58);
                    node();

                    state._fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_document60); 

                    }
                    break;

            }

            match(input,DOCUMENT_END,FOLLOW_DOCUMENT_END_in_document64); 
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:20:58: ( NEWLINE )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==NEWLINE) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:20:58: NEWLINE
                    {
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_document66); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "document"


    // $ANTLR start "node"
    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:22:1: node : scalar ;
    public final void node() throws RecognitionException {
        try {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:23:3: ( scalar )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:23:5: scalar
            {
            pushFollow(FOLLOW_scalar_in_node79);
            scalar();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "node"


    // $ANTLR start "scalar"
    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:25:1: scalar : ( DOUBLEQUOTE ( STRING | WHITESPACE )* DOUBLEQUOTE | SINGLEQUOTE ( STRING | WHITESPACE )* SINGLEQUOTE );
    public final void scalar() throws RecognitionException {
        try {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:26:3: ( DOUBLEQUOTE ( STRING | WHITESPACE )* DOUBLEQUOTE | SINGLEQUOTE ( STRING | WHITESPACE )* SINGLEQUOTE )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==DOUBLEQUOTE) ) {
                alt6=1;
            }
            else if ( (LA6_0==SINGLEQUOTE) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:26:5: DOUBLEQUOTE ( STRING | WHITESPACE )* DOUBLEQUOTE
                    {
                    match(input,DOUBLEQUOTE,FOLLOW_DOUBLEQUOTE_in_scalar94); 
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:26:17: ( STRING | WHITESPACE )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>=STRING && LA4_0<=WHITESPACE)) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:
                    	    {
                    	    if ( (input.LA(1)>=STRING && input.LA(1)<=WHITESPACE) ) {
                    	        input.consume();
                    	        state.errorRecovery=false;
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    match(input,DOUBLEQUOTE,FOLLOW_DOUBLEQUOTE_in_scalar107); 

                    }
                    break;
                case 2 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:27:5: SINGLEQUOTE ( STRING | WHITESPACE )* SINGLEQUOTE
                    {
                    match(input,SINGLEQUOTE,FOLLOW_SINGLEQUOTE_in_scalar113); 
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:27:17: ( STRING | WHITESPACE )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>=STRING && LA5_0<=WHITESPACE)) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:
                    	    {
                    	    if ( (input.LA(1)>=STRING && input.LA(1)<=WHITESPACE) ) {
                    	        input.consume();
                    	        state.errorRecovery=false;
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match(input,SINGLEQUOTE,FOLLOW_SINGLEQUOTE_in_scalar126); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "scalar"

    // Delegated rules


 

    public static final BitSet FOLLOW_document_in_file39 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_DOCUMENT_START_in_document52 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_document54 = new BitSet(new long[]{0x00000000000004C0L});
    public static final BitSet FOLLOW_node_in_document58 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_document60 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_DOCUMENT_END_in_document64 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_NEWLINE_in_document66 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_scalar_in_node79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLEQUOTE_in_scalar94 = new BitSet(new long[]{0x0000000000000380L});
    public static final BitSet FOLLOW_set_in_scalar96 = new BitSet(new long[]{0x0000000000000380L});
    public static final BitSet FOLLOW_DOUBLEQUOTE_in_scalar107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SINGLEQUOTE_in_scalar113 = new BitSet(new long[]{0x0000000000000700L});
    public static final BitSet FOLLOW_set_in_scalar115 = new BitSet(new long[]{0x0000000000000700L});
    public static final BitSet FOLLOW_SINGLEQUOTE_in_scalar126 = new BitSet(new long[]{0x0000000000000002L});

}