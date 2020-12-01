import printer.ConsolePrinter
import token.Tokenizer
import visitor.CalcVisitor
import visitor.PrintVisitor
import visitor.ParserVisitor

fun main() {
    val expression = readLine() ?: throw IllegalArgumentException("Input is null")
    try {
        val rawTokens = Tokenizer().tokenize(expression)
        val printer = PrintVisitor(ConsolePrinter())
        println("Raw tokens:")
        printer.visit(rawTokens)

        val parseVisitor = ParserVisitor()
        parseVisitor.visit(rawTokens)
        val RPNTokens = parseVisitor.getExpression()
        println("RPN:")
        printer.visit(RPNTokens)

        val calcVisitor = CalcVisitor()
        calcVisitor.visit(RPNTokens)
        println("Result : ${calcVisitor.getCalcResult()}")
    } catch (e: Throwable) {
        println(e.message)
    }
}