package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} that converts, whenever possible, postfix increment/decrement
 * to prefix increment/decrement
 *
 * @author Yossi Gil
 * @since 2015-7-17
 */
public final class PostfixToPrefix extends Wring.ReplaceCurrentNode<PostfixExpression> implements Kind.ReorganizeExpression {
  @Override boolean scopeIncludes(@SuppressWarnings("unused") final PostfixExpression __) {
    return true;
  }
  @Override PrefixExpression replacement(final PostfixExpression e) {
    return Subject.operand(e.getOperand()).to(pre2post(e.getOperator()));
  }
  private static PrefixExpression.Operator pre2post(final PostfixExpression.Operator o) {
    return o == PostfixExpression.Operator.DECREMENT ? PrefixExpression.Operator.DECREMENT : PrefixExpression.Operator.INCREMENT;
  }
  @Override protected boolean eligible(final PostfixExpression e) {
    return !(e.getParent() instanceof Expression) //
        && AncestorSearch.forType(ASTNode.VARIABLE_DECLARATION_STATEMENT).from(e) == null //
        && AncestorSearch.forType(ASTNode.SINGLE_VARIABLE_DECLARATION).from(e) == null //
        && AncestorSearch.forType(ASTNode.VARIABLE_DECLARATION_EXPRESSION).from(e) == null;
  }
  @Override String description(final PostfixExpression e) {
    return "Convert post-" + description(e.getOperator()) + " of " + e.getOperand() + " to pre-" + description(e.getOperator());
  }
  private static String description(final Operator o) {
    return o == PostfixExpression.Operator.DECREMENT ? "decrement" : "increment";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}