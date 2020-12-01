package visitor

import token.*
import java.util.*
import kotlin.collections.ArrayList

class ParserVisitor : TokenVisitor {
    private companion object {
        private val operationPriority: Map<ArithmeticOperationToken, Int> = mapOf(
            Pair(PlusToken, 0),
            Pair(MinusToken, 0),
            Pair(DivToken, 1),
            Pair(MulToken, 1)
        )
    }

    private val stack = Stack<Token>()
    private val result = ArrayList<Token>()

    fun getExpression(): List<Token> = result

    override fun visit(token: Token) {
        when (token) {
            is ArithmeticOperationToken -> processVisit(token)
            is NumberToken -> processVisit(token)
            is ParenthesisToken -> processVisit(token)
        }
    }

    override fun visit(tokens: List<Token>) {
        tokens.forEach { it.accept(this) }
        while (!stack.empty()) {
            val lastToken = stack.peek()
            if (lastToken is ArithmeticOperationToken) {
                result.add(lastToken)
                stack.pop()
            } else {
                throw IllegalStateException(
                    "Only operations allowed in the end of transformation\n" +
                            "No matching closing bracket\n" +
                            "Stack is ${stack.toList()}"
                )
            }
        }
    }

    private fun processVisit(token: NumberToken) {
        result.add(token)
    }

    private fun processVisit(token: ParenthesisToken) {
        when (token) {
            is LeftParenthesisToken -> stack.push(token)
            is RightParenthesisToken -> {
                loop@ while (!stack.empty()) {
                    val lastToken = stack.peek()
                    when (lastToken) {
                        is LeftParenthesisToken -> {
                            stack.pop()
                            break@loop
                        }
                        is ArithmeticOperationToken -> {
                            result.add(lastToken)
                            stack.pop()
                        }
                        is RightParenthesisToken, is NumberToken ->
                            throw IllegalStateException("Wrong stack state: ${stack.toList()}")
                    }
                }
            }
        }
    }

    private fun processVisit(token: ArithmeticOperationToken) {
        while (!stack.empty()) {
            val lastToken = stack.peek()
            if (lastToken is ArithmeticOperationToken) {
                val curTokenPriority = operationPriority[token]
                val lastTokenPriority = operationPriority[lastToken]
                if (curTokenPriority == null || lastTokenPriority == null) {
                    throw IllegalArgumentException("Bad operations: $token $lastToken")
                }
                if (curTokenPriority <= lastTokenPriority) {
                    result.add(lastToken)
                    stack.pop()
                } else {
                    break
                }
            } else {
                break
            }
        }
        stack.push(token)
    }
}