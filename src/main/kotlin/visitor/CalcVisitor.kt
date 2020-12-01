package visitor

import token.*
import java.util.*
import kotlin.properties.Delegates

class CalcVisitor : TokenVisitor {
    private var result by Delegates.notNull<Int>()

    private companion object {
        private val operations: Map<ArithmeticOperationToken, (Int, Int) -> Int> = mapOf(
            Pair(PlusToken, Int::plus),
            Pair(MinusToken, Int::minus),
            Pair(MulToken, Int::times),
            Pair(DivToken, Int::div)
        )
    }
    private val stack = Stack<Int>()

    private fun visitParenthesis(token: ParenthesisToken) {
        throw IllegalArgumentException("Parenthesis are restricted")
    }

    private fun visitNumber(token: NumberToken) {
        stack.add(token.n)
    }

    private fun visitArithmeticOperation(token: ArithmeticOperationToken) {
        val first = stack.pop()
        val second = stack.pop()
        val operation = operations[token]
        if (operation == null) {
            throw IllegalArgumentException("Unrecognized operation")
        } else {
            stack.push(operation(second, first))
        }
    }

    override fun visit(token: Token) {
        when (token) {
            is ArithmeticOperationToken -> visitArithmeticOperation(token)
            is NumberToken -> visitNumber(token)
            is ParenthesisToken -> visitParenthesis(token)
        }
    }

    override fun visit(tokens: List<Token>) {
        tokens.forEach {
            it.accept(this)
        }
        check(stack.size == 1) { "RPN expression have more than 1 element on stack after evaluation" }
        result = stack.pop()
    }

    fun getCalcResult(): Int = result
}