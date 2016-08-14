package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.corext.dom.*;

import static il.org.spartan.idiomatic.*;

/**
 * Some useful utility functions used for binding manipulations.
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24
 */
@SuppressWarnings("restriction") public class BindingUtils {
  /**
   * @param u
   *          current compilation unit
   * @return current package
   */
  public static IPackageBinding getPackage(final CompilationUnit u) {
    return u.getPackage().resolveBinding();
  }
  /**
   * @param n
   *          an {@link ASTNode}
   * @return the type in which n is placed, or null if there is none
   */
  public static ITypeBinding getClass(final ASTNode n) {
    ASTNode p = n;
    while (p != null && !(p instanceof TypeDeclaration))
      p = p.getParent();
    return p == null ? null : ((TypeDeclaration) p).resolveBinding();
  }
  /**
   * Determines whether an invocation of a method is legal in a specific
   * context.
   *
   * @param b
   *          a method
   * @param n
   *          the context in which the method is invoked
   * @param u
   *          current {@link CompilationUnit}
   * @return <code><b>true</b></code> <i>iff</i> the method is visible from its
   *         context
   */
  public static boolean isVisible(final IMethodBinding b, final ASTNode n, final CompilationUnit u) {
    final int ms = b.getModifiers();
    if (Modifier.isPublic(ms))
      return true;
    final ITypeBinding mc = b.getDeclaringClass();
    if (Modifier.isProtected(ms) && mc.getPackage().equals(getPackage(u)))
      return true;
    final ITypeBinding nc = getClass(n);
    return nc != null && nc.equals(mc);
  }
  /**
   * Finds visible method in hierarchy.
   *
   * @param b
   *          base type
   * @param mn
   *          method name
   * @param bs
   *          method parameters
   * @param n
   *          original {@link ASTNode} containing the method invocation. Used in
   *          order to determine the context in which the method is being used
   * @param u
   *          current {@link CompilationUnit}
   * @return the method's binding if it is visible from context, else null
   */
  public static IMethodBinding getVisibleMethod(final ITypeBinding b, final String mn, final ITypeBinding[] bs, final ASTNode n, final CompilationUnit u) {
    if (b == null)
      return null;
    final IMethodBinding $ = Bindings.findMethodInHierarchy(b, mn, bs);
    return take($).when(isVisible($, n, u));
  }
  /**
   * Checks if expression is simple.
   *
   * @param e
   *          an expression
   * @return <code><b>true</b></code> <i>iff</i> the parameter is simple
   */
  public static boolean isSimple(final Expression e) {
    return e instanceof Name || e instanceof NumberLiteral || e instanceof BooleanLiteral || e instanceof CharacterLiteral || e instanceof NullLiteral || e instanceof StringLiteral
        || e instanceof ThisExpression || e instanceof TypeLiteral;
  }
}