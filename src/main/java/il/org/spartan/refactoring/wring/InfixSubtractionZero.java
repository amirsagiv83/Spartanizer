package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Is.*;
import static il.org.spartan.refactoring.utils.Plant.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>X-0</code> by <code>X</code> and <code>0-X</code> by
 * <code>-X<code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression e) {
    return "Remove subtraction of 0 in " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != MINUS ? null : go(e);
  }

  private static ASTNode go(final InfixExpression e) {
    return e.hasExtendedOperands() ? plant(go(operands(e))).into(parent(e))
        : isLiteralZero(left(e)) ? plant(minus(right(e))).into(parent(e)) //
            : isLiteralZero(right(e)) ? plant(left(e)).into(parent(e)) //
                : null;
  }

  private static Expression go(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>(es);
    if (isLiteralZero(first($))) {
      $.remove(0);
      $.set(0, minus(first($)));
    } else
      for (int i = 1, size = $.size(); i < size; ++i)
        if (isLiteralZero($.get(i))) {
          $.remove(i);
          break;
        }
    return subject.operands($).to(MINUS);
  }
}