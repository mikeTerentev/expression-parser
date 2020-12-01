package token

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class TokenizerTest {
    private var tokenizer = Tokenizer();

    @Before
    fun prepareTokenizer() {
        tokenizer = Tokenizer()
    }

    @Test
    fun `2 + 2`() {
        val s = "2 + 2"
        val tokens = tokenizer.tokenize(s)
        assertEquals(listOf(NumberToken(2), PlusToken, NumberToken(2)), tokens)
    }

    @Test
    fun `11 + 5 * 2`() {
        val s = "11+5*2"
        val tokens = tokenizer.tokenize(s)
        assertEquals(
            listOf(
                NumberToken(11),
                PlusToken,
                NumberToken(5),
                MulToken,
                NumberToken(2)
            ),
            tokens
        )
    }

    @Test
    fun `12 div (11- 1)`() {
        val s = "12 / (11- 1)"
        val tokens = tokenizer.tokenize(s)
        assertEquals(
            listOf(
                NumberToken(12),
                DivToken,
                LeftParenthesisToken,
                NumberToken(11),
                MinusToken,
                NumberToken(1),
                RightParenthesisToken
            ),
            tokens
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun incorrectTest() {
        val s = "1 / asdada  j"
        tokenizer.tokenize(s)
    }
}