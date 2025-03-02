/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Stack;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //  Expr = Number(n:double) + Variable(name:string) + Plus(left:Expr, right:Expr) + Multiply(left:Expr, right:Expr)
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        // make a stream of characters to feed to the lexer
        CharStream stream = new ANTLRInputStream(input);

        // make parser
        // create an instance of the lexer class that our grammar file generated, and pass it the character stream
        ExpressionLexer lexer = new ExpressionLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);

        // The result is a stream of terminals, which we can then feed to the parser
        ExpressionParser parser = new ExpressionParser(tokens);
        parser.reportErrorsAsExceptions();

        ParseTree tree = parser.root();

        MakeExpression exprMaker = new MakeExpression();
        new ParseTreeWalker().walk(exprMaker, tree);
        return exprMaker.getExpression();
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
}

/** Make a IntegerExpresion value from a parse tree. */
class MakeExpression implements ExpressionListener {
    private final Stack<Expression> stack = new Stack<>();
    // Invariant: stack contains the Expression value of each parse
    // subtree that has been fully-walked so far, but whose parent has not yet
    // been exited by the walk. The stack is ordered by recency of visit, so that
    // the top of the stack is the Expression for the most recently walked
    // subtree.
    //
    // At the start of the walk, the stack is empty, because no subtrees have
    // been fully walked.
    //
    // Whenever a node is exited by the walk, the Expression values of its
    // children are on top of the stack, in order with the last child on top. To
    // preserve the invariant, we must pop those child Expression values
    // from the stack, combine them with the appropriate Expression
    // producer, and push back an Expression value representing the entire
    // subtree under the node.
    //
    // At the end of the walk, after all subtrees have been walked and the the
    // root has been exited, only the entire tree satisfies the invariant's
    // "fully walked but parent not yet exited" property, so the top of the stack
    // is the Expression of the entire parse tree.

    /**
     * Returns the expression constructed by this listener object.
     * Requires that this listener has completely walked over an Expression
     * parse tree using ParseTreeWalker.
     * @return Expression for the parse tree that was walked
     */
    public Expression getExpression() {
        return stack.getFirst();
    }

    @Override public void exitRoot(ExpressionParser.RootContext context) {
        // do nothing, root has only one child so its value is
        // already on top of the stack
    }

    @Override public void exitSum(ExpressionParser.SumContext context) {
        // matched the primitive ('+' primitive)* rule
        List<ExpressionParser.ProductContext> addends = context.product();
        assert stack.size() >= addends.size();

        // the pattern above always has at least 1 child;
        // pop the last child
        assert !addends.isEmpty();
        Expression sum = stack.pop();

        // pop the older children, one by one, and add them on
        for (int i = 1; i < addends.size(); ++i) {
            sum = new Plus(stack.pop(), sum);
        }

        // the result is this subtree's Expression
        stack.push(sum);
    }

    @Override public void exitProduct(ExpressionParser.ProductContext context) {
        // matched the primitive ('+' primitive)* rule
        List<ExpressionParser.PrimitiveContext> addends = context.primitive();
        assert stack.size() >= addends.size();

        // the pattern above always has at least 1 child;
        // pop the last child
        assert !addends.isEmpty();
        Expression product = stack.pop();

        // pop the older children, one by one, and add them on
        for (int i = 1; i < addends.size(); ++i) {
            product = new Multiply(stack.pop(), product);
        }

        // the result is this subtree's Expression
        stack.push(product);
    }

    @Override public void exitPrimitive(ExpressionParser.PrimitiveContext context) {
        if (context.NUMBER() != null) {
            // matched the NUMBER alternative
            double n = Double.parseDouble(context.NUMBER().getText());
            Expression number = new Number(n);
            stack.push(number);
        }
        else if (context.VARIABLE() != null) {
            String var = context.VARIABLE().getText();
            Expression variable = new Variable(var);
            stack.push(variable);
        }
        else {
            // matched the '(' sum ')' alternative
            // do nothing, because sum's value is already on the stack
        }
    }

    // don't need these here, so just make them empty implementations
    @Override public void enterRoot(ExpressionParser.RootContext context) { }
    @Override public void enterSum(ExpressionParser.SumContext context) { }
    @Override public void enterPrimitive(ExpressionParser.PrimitiveContext context) { }
    @Override public void enterProduct(ExpressionParser.ProductContext ctx) {}

    @Override public void visitTerminal(TerminalNode terminal) { }
    @Override public void enterEveryRule(ParserRuleContext context) { }
    @Override public void exitEveryRule(ParserRuleContext context) { }
    @Override public void visitErrorNode(ErrorNode node) { }
}
