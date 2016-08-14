package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A class for analyzing a method.
 *
 * @author Yossi Gil
 * @since 2015-08-29
 */
public class MethodExplorer {
  final MethodDeclaration inner;

  /**
   * Instantiate this class
   *
   * @param inner
   *          JD
   */
  public MethodExplorer(final MethodDeclaration inner) {
    this.inner = inner;
  }
  /**
   * Computes the list of all local variable declarations found in a method.
   * {@link MethodDeclaration}. <p> This method correctly ignores declarations
   * made within nested types. It also correctly adds variables declared within
   * plain and extended for loops, just as local variables defined within a try
   * and catch clauses.
   *
   * @return a list of {@link SimpleName} from the given method.
   */
  public List<SimpleName> localVariables() {
    final List<SimpleName> $ = new ArrayList<>();
    inner.accept(new IgnoreNestedMethods() {
      @Override public boolean visit(final CatchClause c) {
        return add(c.getException());
      }
      @Override public boolean visit(final EnhancedForStatement s) {
        return add(s.getParameter());
      }
      @Override public boolean visit(final ForStatement s) {
        return add(expose.initializers(s));
      }
      private boolean add(final List<Expression> es) {
        for (final Expression e : es)
          if (e instanceof VariableDeclarationExpression)
            add((VariableDeclarationExpression) e);
        return true;
      }
      @Override public boolean visit(final TryStatement s) {
        return addVariableDeclarationExpression(resources(s));
      }
      @Override public boolean visit(final VariableDeclarationStatement s) {
        addFragments(expose.fragments(s));
        return true;
      }
      private boolean addVariableDeclarationExpression(final List<VariableDeclarationExpression> initializers) {
        for (final VariableDeclarationExpression o : initializers)
          add(o);
        return true;
      }
      /**
       * @param e
       */
      private void add(final VariableDeclarationExpression e) {
        addFragments(expose.fragments(e));
      }
      private boolean add(final SingleVariableDeclaration d) {
        $.add(d.getName());
        return true;
      }
      private void addFragments(final List<VariableDeclarationFragment> fs) {
        for (final VariableDeclarationFragment f : fs)
          $.add(f.getName());
      }
    });
    return $;
  }
  /**
   * Computes the list of all return statements found in a
   * {@link MethodDeclaration}. <p> This method correctly ignores return
   * statements found within nested types.
   *
   * @return a list of {@link ReturnStatement} from the given method.
   */
  public List<ReturnStatement> returnStatements() {
    final List<ReturnStatement> $ = new ArrayList<>();
    inner.accept(new IgnoreNestedMethods() {
      @Override public boolean visit(final ReturnStatement s) {
        $.add(s);
        return true;
      }
    });
    return $;
  }

  @SuppressWarnings("unused") abstract static class IgnoreNestedMethods extends ASTVisitor {
    @Override public final boolean visit(final AnnotationTypeDeclaration __) {
      return false;
    }
    @Override public final boolean visit(final AnonymousClassDeclaration __) {
      return false;
    }
    @Override public final boolean visit(final EnumDeclaration __) {
      return false;
    }
    @Override public final boolean visit(final TypeDeclaration __) {
      return false;
    }
  }
}