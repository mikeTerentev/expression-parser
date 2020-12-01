package token

class Tokenizer {
    private val tokens = ArrayList<Token>()
    private var state: State = StartState()

    fun tokenize(s: String): List<Token> {
        state = StartState()
        s.forEach { state.process(it) }
        state.eof()
        return tokens
    }

    private abstract inner class State {
        abstract fun process(character: Char)
        abstract fun eof()
    }

    private inner class NumericState : State() {
        private var number = 0;

        override fun process(character: Char) {
            when (character) {
                in '0'..'9' -> {
                    number = number * 10 + (character - '0')
                }
                else -> {
                    this@Tokenizer.tokens.add(NumberToken(number))
                    this@Tokenizer.state = StartState()
                    this@Tokenizer.state.process(character)
                }
            }
        }

        override fun eof() {
            this@Tokenizer.tokens.add(NumberToken(number))
            this@Tokenizer.state = EndState()
        }
    }

    private inner class StartState : State() {
        override fun process(character: Char) {
            when (character) {
                '+' -> this@Tokenizer.tokens.add(PlusToken)
                '-' -> this@Tokenizer.tokens.add(MinusToken)
                '*' -> this@Tokenizer.tokens.add(MulToken)
                '/' -> this@Tokenizer.tokens.add(DivToken)
                '(' -> this@Tokenizer.tokens.add(LeftParenthesisToken)
                ')' -> this@Tokenizer.tokens.add(RightParenthesisToken)
                in '0'..'9' -> {
                    this@Tokenizer.state = NumericState()
                    this@Tokenizer.state.process(character)
                }
                else -> {
                    require(character.isWhitespace()) { "Unexpected character $character in the input" }
                }
            }
        }

        override fun eof() {
            this@Tokenizer.state = EndState()
        }
    }

    private inner class EndState : State() {
        override fun process(character: Char) {
            throw UnsupportedOperationException("character $character in EOF state")
        }

        override fun eof() {}
    }
}