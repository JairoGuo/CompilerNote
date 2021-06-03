/**
 * The interface Token.
 *
 * @author jairo
 */
public interface Token {

    /**
     * Token的类型
     *
     * @return TokenType type
     */
    public TokenType getType();

    /**
     * Token的文本值
     *
     * @return String text
     */
    public String getText();

}