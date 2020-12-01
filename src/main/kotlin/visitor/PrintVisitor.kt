package visitor

import printer.Printer
import token.Token

class PrintVisitor(private val printer: Printer) : TokenVisitor {
    override fun visit(token: Token) {
        printer.write(token.toString())
    }

    override fun visit(tokens: List<Token>) {
        tokens.forEachIndexed { _, token ->
            token.accept(this)
            printer.write(" ")
        }
        printer.writeln()
    }
}