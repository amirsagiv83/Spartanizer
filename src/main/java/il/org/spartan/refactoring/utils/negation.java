package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public enum negation {
  ;
  public static int level(final InfixExpression e) {
    return out(e.getOperator(), TIMES, DIVIDE) ? 0 : level(extract.operands(e));
  }

  public static int level(final List<Expression> es) {
    int $ = 0;
    for (final Expression e : es)
      $ += negation.level(e);
    return $;
  }

  public static int level(final Expression ¢) {
    return iz.is(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
        : iz.is(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
            : iz.is(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
                : iz.is(¢, NUMBER_LITERAL) ? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
                    : 0;
  }

  public static Expression peel(final Expression $) {
    return iz.is($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
        : iz.is($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
            : iz.is($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
                : iz.is($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
                    : $;
  }

  public static Expression peel(final NumberLiteral $) {
    return !$.getToken().startsWith("-") && !$.getToken().startsWith("+") ? $ : $.getAST().newNumberLiteral($.getToken().substring(1));
  }

  public static Expression peel(final PrefixExpression $) {
    return out($.getOperator(), MINUS1, PLUS1) ? $ : peel($.getOperand());
  }

  public static Expression peel(final InfixExpression e) {
    return out(e.getOperator(), TIMES, DIVIDE) ? e : subject.operands(peel(extract.operands(e))).to(e.getOperator());
  }

  private static List<Expression> peel(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression e : es)
      $.add(peel(e));
    return $;
  }

  private static int level(final PrefixExpression ¢) {
    return az.bit(¢.getOperator() == MINUS1) + level(¢.getOperand());
  }
}