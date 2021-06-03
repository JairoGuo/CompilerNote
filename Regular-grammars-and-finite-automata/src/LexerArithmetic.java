import java.util.ArrayList;
import java.util.List;

/**
 * @author Jairo
 * @date 2021/6/3 12:36
 */


public class LexerArithmetic {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        LexerArithmetic lexer = new LexerArithmetic();

        String script;
        List<Token> tokenReader;


        script = "2 + 3 * 5;";
        System.out.println("parse :" + script);
        tokenReader = lexer.tokenize(script);
        dump(tokenReader);


        script = "age + 3 * 5;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        dump(tokenReader);

    }

    /**
     * 打印所有的Token
     *
     * @param tokenReader the token reader
     */
    public static void dump(List<Token> tokenReader) {
        System.out.println("[text]\t[type]");
//        Token token;
        for (Token token : tokenReader) {
            System.out.println(token.getText() + "\t\t" + token.getType());
        }

    }


    //下面几个变量是在解析过程中用到的临时变量,如果要优化的话，可以塞到方法里隐藏起来

    //临时保存token的文本
    private StringBuffer tokenText = null;

    //保存解析出来的Token
    private List<Token> tokens = null;

    //当前正在解析的Token
    private SimpleToken token = null;

    //是否是字母
    private boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    //是否是数字
    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }


    /**
     * 解析字符串，形成Token。
     * 这是一个有限状态自动机，在不同的状态中迁移。
     *
     * @param code 代码串
     * @return the list
     */
    public List<Token> tokenize(String code) {
        tokens = new ArrayList<>();
        tokenText = new StringBuffer();
        token = new SimpleToken();
        byte[] bytes = code.getBytes();

        char ch;

        DfaState state = DfaState.Initial;

        for (byte aByte : bytes) {
            ch = (char) aByte;
            switch (state) {
                case Initial:
                    //重新确定后续状态
                    state = initToken(ch);
                    break;
                case Identifier:
                    if (isAlpha(ch) || isDigit(ch)) {
                        //保持标识符状态
                        tokenText.append(ch);
                    } else {
                        //退出标识符状态，并保存Token
                        state = initToken(ch);
                    }
                    break;
                case IntLiteral:
                    if (isDigit(ch)) {
                        //继续保持在数字字面量状态
                        tokenText.append(ch);
                    } else {
                        //退出当前状态，并保存Token
                        state = initToken(ch);
                    }
                    break;

                case Assignment:
                case Plus:
                case Minus:
                case Star:
                case Slash:
                    state = initToken(ch);          //退出当前状态，并保存Token
                    break;

                default:

            }

        }


        return tokens;
    }

    /**
     * 有限状态机进入初始状态。
     * 这个初始状态其实并不做停留，它马上进入其他状态。
     * 开始解析的时候，进入初始状态；某个Token解析完毕，也进入初始状态，在这里把Token记下来，然后建立一个新的Token。
     */
    private DfaState initToken(char ch) {

        if (tokenText.length() > 0) {
            token.text = tokenText.toString();
            tokens.add(token);
            tokenText = new StringBuffer();
            token = new SimpleToken();
        }

        DfaState newState = DfaState.Initial;
        //第一个字符是字母
        if (isAlpha(ch)) {

            newState = DfaState.Identifier;
            token.type = TokenType.Identifier;
            tokenText.append(ch);
            //第一个字符是数字
        } else if (isDigit(ch)) {
            newState = DfaState.IntLiteral;
            token.type = TokenType.IntLiteral;
            tokenText.append(ch);
        } else if (ch == '+') {
            newState = DfaState.Plus;
            token.type = TokenType.Plus;
            tokenText.append(ch);
        } else if (ch == '-') {
            newState = DfaState.Minus;
            token.type = TokenType.Minus;
            tokenText.append(ch);
        } else if (ch == '*') {
            newState = DfaState.Star;
            token.type = TokenType.Star;
            tokenText.append(ch);
        } else if (ch == '/') {
            newState = DfaState.Slash;
            token.type = TokenType.Slash;
            tokenText.append(ch);
        } else if (ch == ';') {
            newState = DfaState.SemiColon;
            token.type = TokenType.SemiColon;
            tokenText.append(ch);
        } else if (ch == '(') {
            newState = DfaState.LeftParen;
            token.type = TokenType.LeftParen;
            tokenText.append(ch);
        } else if (ch == ')') {
            newState = DfaState.RightParen;
            token.type = TokenType.RightParen;
            tokenText.append(ch);
        } else if (ch == '=') {
            newState = DfaState.Assignment;
            token.type = TokenType.Assignment;
            tokenText.append(ch);
        }

        return newState;
    }

    private static final class SimpleToken implements Token {
        //Token类型
        private TokenType type = null;

        //文本值
        private String text = null;

        @Override
        public TokenType getType() {
            return type;
        }

        @Override
        public String getText() {
            return text;
        }
    }

    /**
     * 确定又穷状态自动机状态
     *
     * @author Jairo
     * @date 2021/6/1 12:13
     */
    private enum DfaState {
        /**
         * Initial dfa state.
         */
        Initial,
        /**
         * Id dfa state.
         */
        Identifier,
        /**
         * Int literal dfa state.
         */
        IntLiteral,

        // =
        Assignment,
        Plus, Minus, Star, Slash,
        GT,
        SemiColon,
        LeftParen,
        RightParen,

    }

}
