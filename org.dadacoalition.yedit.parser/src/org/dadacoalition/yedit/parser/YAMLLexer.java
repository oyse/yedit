// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g 2009-09-01 21:19:08

package org.dadacoalition.yedit.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class YAMLLexer extends Lexer {
    public static final int NEWLINE=5;
    public static final int SINGLEQUOTE=10;
    public static final int DOCUMENT_START=4;
    public static final int DOCUMENT_END=6;
    public static final int WHITESPACE=9;
    public static final int NORMAL_CHARS=11;
    public static final int DOUBLEQUOTE=7;
    public static final int EOF=-1;
    public static final int STRING=8;

    // delegates
    // delegators

    public YAMLLexer() {;} 
    public YAMLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public YAMLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g"; }

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:31:3: ( ( NORMAL_CHARS )+ )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:31:5: ( NORMAL_CHARS )+
            {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:31:5: ( NORMAL_CHARS )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' '||(LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='\\'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:31:5: NORMAL_CHARS
            	    {
            	    mNORMAL_CHARS(); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:34:3: ( '\\n' )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:34:5: '\\n'
            {
            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "DOCUMENT_START"
    public final void mDOCUMENT_START() throws RecognitionException {
        try {
            int _type = DOCUMENT_START;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:37:3: ( '---' )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:37:5: '---'
            {
            match("---"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOCUMENT_START"

    // $ANTLR start "DOCUMENT_END"
    public final void mDOCUMENT_END() throws RecognitionException {
        try {
            int _type = DOCUMENT_END;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:40:3: ( '...' )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:40:5: '...'
            {
            match("..."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOCUMENT_END"

    // $ANTLR start "DOUBLEQUOTE"
    public final void mDOUBLEQUOTE() throws RecognitionException {
        try {
            int _type = DOUBLEQUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:43:3: ( '\"' )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:43:5: '\"'
            {
            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLEQUOTE"

    // $ANTLR start "SINGLEQUOTE"
    public final void mSINGLEQUOTE() throws RecognitionException {
        try {
            int _type = SINGLEQUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:46:3: ( '\\'' )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:46:5: '\\''
            {
            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINGLEQUOTE"

    // $ANTLR start "NORMAL_CHARS"
    public final void mNORMAL_CHARS() throws RecognitionException {
        try {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:3: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '\\\\\\'' | '\\\\\"' | WHITESPACE )
            int alt2=6;
            switch ( input.LA(1) ) {
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                {
                alt2=1;
                }
                break;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                {
                alt2=2;
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                {
                alt2=3;
                }
                break;
            case '\\':
                {
                int LA2_4 = input.LA(2);

                if ( (LA2_4=='\'') ) {
                    alt2=4;
                }
                else if ( (LA2_4=='\"') ) {
                    alt2=5;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 4, input);

                    throw nvae;
                }
                }
                break;
            case '\t':
            case ' ':
                {
                alt2=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:5: 'a' .. 'z'
                    {
                    matchRange('a','z'); 

                    }
                    break;
                case 2 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:14: 'A' .. 'Z'
                    {
                    matchRange('A','Z'); 

                    }
                    break;
                case 3 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:23: '0' .. '9'
                    {
                    matchRange('0','9'); 

                    }
                    break;
                case 4 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:34: '\\\\\\''
                    {
                    match("\\'"); 


                    }
                    break;
                case 5 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:43: '\\\\\"'
                    {
                    match("\\\""); 


                    }
                    break;
                case 6 :
                    // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:50:51: WHITESPACE
                    {
                    mWHITESPACE(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "NORMAL_CHARS"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:53:3: ( ' ' | '\\t' )
            // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    public void mTokens() throws RecognitionException {
        // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:8: ( STRING | NEWLINE | DOCUMENT_START | DOCUMENT_END | DOUBLEQUOTE | SINGLEQUOTE )
        int alt3=6;
        switch ( input.LA(1) ) {
        case '\t':
        case ' ':
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '\\':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt3=1;
            }
            break;
        case '\n':
            {
            alt3=2;
            }
            break;
        case '-':
            {
            alt3=3;
            }
            break;
        case '.':
            {
            alt3=4;
            }
            break;
        case '\"':
            {
            alt3=5;
            }
            break;
        case '\'':
            {
            alt3=6;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 3, 0, input);

            throw nvae;
        }

        switch (alt3) {
            case 1 :
                // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:10: STRING
                {
                mSTRING(); 

                }
                break;
            case 2 :
                // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:17: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 3 :
                // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:25: DOCUMENT_START
                {
                mDOCUMENT_START(); 

                }
                break;
            case 4 :
                // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:40: DOCUMENT_END
                {
                mDOCUMENT_END(); 

                }
                break;
            case 5 :
                // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:53: DOUBLEQUOTE
                {
                mDOUBLEQUOTE(); 

                }
                break;
            case 6 :
                // /home/oysteto/projects/yedit/org.dadacoalition.yedit.parser/src/org/dadacoalition/yedit/parser/YAML.g:1:65: SINGLEQUOTE
                {
                mSINGLEQUOTE(); 

                }
                break;

        }

    }


 

}