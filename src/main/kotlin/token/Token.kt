package token

import visitor.TokenVisitor

sealed class Token {
    open fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }
}

sealed class ArithmeticOperationToken : Token()

sealed class ParenthesisToken : Token()

//   Arithmetic block

object PlusToken : ArithmeticOperationToken() {
    override fun toString(): String {
        return "PLUS"
    }
}

object MinusToken : ArithmeticOperationToken() {
    override fun toString(): String {
        return "MINUS"
    }
}

object MulToken : ArithmeticOperationToken() {
    override fun toString(): String {
        return "MUL"
    }
}

object DivToken : ArithmeticOperationToken() {
    override fun toString(): String {
        return "DIV"
    }
}

data class NumberToken(val n: Int) : Token() {
    override fun toString(): String {
        return "NUMBER($n)"
    }
}

// Parenthesis block

object LeftParenthesisToken : ParenthesisToken() {
    override fun toString(): String {
        return "LEFT"
    }
}

object RightParenthesisToken : ParenthesisToken() {
    override fun toString(): String {
        return "RIGHT"
    }
}