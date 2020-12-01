package visitor

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import token.*

class CalcVisitorTest {
    private var calculatorVisitor = CalcVisitor()

    @Before
    fun prepareVisitor() {
        calculatorVisitor = CalcVisitor()
    }

    @Test
    fun single() {
        val num = 100;
        val tokens = listOf(NumberToken(num))
        calculatorVisitor.visit(tokens)
        assertEquals(num, calculatorVisitor.getCalcResult())
    }

    @Test(expected = IllegalStateException::class)
    fun testNonVisiting() {
        calculatorVisitor.getCalcResult()
    }

    @Test
    fun `2 + 7`() {
        val a = 2
        val b = 7
        val tokens = listOf(NumberToken(a), NumberToken(b), PlusToken)
        calculatorVisitor.visit(tokens)
        assertEquals(a + b, calculatorVisitor.getCalcResult())
    }

    @Test
    fun `2 - (3 + 5) div 2`() {
        val tokens = listOf(
            NumberToken(2),
            NumberToken(3),
            NumberToken(5),
            PlusToken,
            MinusToken,
            NumberToken(2),
            DivToken
        )
        calculatorVisitor.visit(tokens)
        assertEquals(calculatorVisitor.getCalcResult(), -3)
    }

}