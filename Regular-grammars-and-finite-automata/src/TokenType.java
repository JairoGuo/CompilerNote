/**
 * Token的类型
 *
 * @author jairo
 */

public enum TokenType {
    // 标识符
    Identifier,

    // >
    GT,
    // =
    Assignment,
    // >=
    GE,
    // int
    Int,
    // 整型字面量
    IntLiteral,
    // +
    Plus,
    // -
    Minus,
    // *
    Star,
    // /
    Slash,

    SemiColon, // ;
    LeftParen, // (
    RightParen,// )
}