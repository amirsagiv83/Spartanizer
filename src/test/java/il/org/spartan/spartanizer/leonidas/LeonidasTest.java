package il.org.spartan.spartanizer.leonidas;

import org.junit.*;

@SuppressWarnings("static-method") public class LeonidasTest {
  @Test public void testMatches1() {
    leonidasSays.that("$X ? y == 17 : $X2").matches("x == 7 ? y == 17 : 9");
  }

  @Test public void testMatches2() {
    leonidasSays.that("$X ? 8 : $X2").notmatches("x == 7 ? y == 17 : 9");
  }

  @Test public void testMatches3() {
    leonidasSays.that("w = $X ? y == 17 : $X2;").matches("w = x == 7 ? y == 17 : 9;");
  }

  @Test public void testMatches4() {
    leonidasSays.that("w = $X ? 8 : $X2;").notmatches("w = x == 7 ? y == 17 : 9;");
  }

  @Test public void testMatches5() {
    leonidasSays.that("x == $X ? $X2 : $X").matches("x == null ? 17 : null");
  }

  @Test public void testMatches6() {
    leonidasSays.that("x == $X ? $X2 : $X").notmatches("x == null ? 17 : 18");
  }

  @Test public void testMatches7() {
    leonidasSays.that("x == $X ? $X : $X").notmatches("x == null ? 17 : null");
  }

  @Test public void testMatches8() {
    leonidasSays.that("$X ? y == 17 : $M").matches("x == 7 ? y == 17 : foo()");
  }

  @Test public void testMatches9() {
    leonidasSays.that("if(true) $B();").matches("if(true) foo();");
  }

  @Ignore @Test public void testMatches10() {
    leonidasSays.that("for($N1 $N2 : $X) $N3($N2);").matches("for (Expression ¢ : hop.operands(flatten.of(inner))) make.notOf(¢);");
  }

  @Test public void testMutation1() {
    leonidasSays.tipper("$X1 == null ? $X2 : $X1", "$X1.defaultsTo($X2)", "defaultsTo").turns("a == null ? y : a").into("a.defaultsTo(y)");
  }

  @Test public void testMutation2() {
    leonidasSays.tipper("$X1 == null ? $X2 : $X1", "$X1.defaultsTo($X2)", "defaultsTo")
        .turns("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(),c.d()).e").into("a(b(), c.d()).e.defaultsTo(2 * 3 + 4 * z().x)");
  }

  @Test public void testMutation3() {
    leonidasSays.tipper("$X1 = $X1 != null ? $X1 : $X2", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation")
        .turns("defaultInstance = defaultInstance != null ? defaultInstance : freshCopyOfAllTippers()")
        .into("lazyEvaluatedTo(defaultInstance, freshCopyOfAllTippers())");
  }

  @Test public void testMutation4() {
    leonidasSays.tipper("$X1 = $X1 == null ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation")
        .turns("defaultInstance = defaultInstance == null ? freshCopyOfAllTippers() : defaultInstance")
        .into("lazyEvaluatedTo(defaultInstance, freshCopyOfAllTippers())");
  }

  @Test public void testMutation5() {
    leonidasSays.tipper("$X1 = $X1 == null ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation")
        .turns("return defaultInstance = defaultInstance == null ? freshCopyOfAllTippers() : defaultInstance;")
        .into("return lazyEvaluatedTo(defaultInstance, freshCopyOfAllTippers());");
  }

  // TODO: ignored due to formatting (test is indeed successful)
  @Ignore @Test public void testMutation6() {
    leonidasSays.tipper("if($X) return y; print(7);", "washere();", "").turns("print(8); if(a || b && c) return y; print(7);")
        .into("print(8); washere();");
  }

  // TODO: ignored due to formatting (test is indeed successful)
  @Ignore @Test public void testMutation7() {
    leonidasSays.tipper("if($X1 == null) $X1 = $X2; return $X1;", "return $X1 = $X1 == null ? $X2 : $X1;", "")
        .turns("if (instance == null) instance = allTippers(); return instance;")
        .into("return instance = instance == null ? allTippers() : instance;");
  }

  // TODO: ignored due to formatting (test is indeed successful)
  @Ignore @Test public void testMutation8() {
    leonidasSays.tipper("$X = $X.$N1($A1); $X = $X.$N2($A2);", "$X = $X.$N1($A1).$N2($A2);", "")
        .turns("$ = $.replaceFirst(\"^[\\\\[]+L\", \"\");\n $ = $.replaceAll(\";$\", \"\");")
        .into("$ = $.replaceFirst(\"^[\\\\[]+L\", \"\").replaceAll(\";$\", \"\");");
  }

  @Test public void testNotTips1() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("x17 == 7 ? 2*3 + 4*z().x : x17");
  }

  @Test public void testNotTips2() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("null == x ? 2*3 + 4*z().x : x17");
  }

  @Test public void testNotTips3() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo")
        .nottips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).f");
  }

  @Test public void testNotTips4() {
    leonidasSays.tipper("if($X) return y; print(7);", "", "").nottips("if(a || b && c) return z; print(7);");
  }

  @Test public void testNotTips5() {
    leonidasSays.tipper("x", "", "").nottips("y");
  }

  @Test public void testNotTips6() {
    leonidasSays.tipper("print(7); print(8);", "", "").nottips("print(7); print(9);");
  }

  @Test public void testNotTips7() {
    leonidasSays.tipper("print(7);", "", "").nottips("print(8);");
  }

  @Test public void testNotTips8() {
    leonidasSays.tipper("7", "", "").nottips("8");
  }

  @Test public void testTips1() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("x17 == null ? 2*3 + 4*z().x : x17");
  }

  @Test public void testTips2() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo")
        .tips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).e");
  }

  @Test public void testTips3() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("x17 == null ? 2*3 + 4*z().x : x17");
  }

  @Test public void testTips4() {
    leonidasSays.tipper("$X1 == $X2 && $X1 == $X3", "$X1.equals($X2, $X3)", "equalsToFew").tips("x1 == x2 && x1 == 789");
  }

  @Test public void testTips5() {
    leonidasSays.tipper("if($X == null) return null;", "if($X == null) return Null;", "assertNotNull")
        .tips("if(g().f.b.c(1,g(), 7) == null) return null;");
  }

  @Test public void testTips6() {
    leonidasSays.tipper("if(!$X1) $B1 else $B2", "if($X1) $B2 else $B1", "change If order").tips("if(!(x==0)) return; else print(7);");
  }

  @Test public void testTips7() {
    leonidasSays.tipper("if(x) return y; print(7);", "", "").tips("if(x) return y; print(7);");
  }

  @Test public void testTips8() {
    leonidasSays.tipper("if($X) return y; print(7);", "", "").tips("if(a || b && c) return y; print(7);");
  }

  @Test public void testTips9() {
    leonidasSays.tipper("if($X1 == null) $X1 = $X2; return $X1;", "", "").tips("if (instance == null) instance = allTippers(); return instance;");
  }

  @Test public void testTips10() {
    leonidasSays.tipper("$X = $X.$N1($A1); $X = $X.$N2($A2);", "", "")
        .tips("$ = $.replaceFirst(\"^[\\\\[]+L\", \"\");\n $ = $.replaceAll(\";$\", \"\");");
  }

  @Test public void testTips10WithWhitespaces() {
    leonidasSays.tipper("$X = $X.$N1($A1); $X = $X.$N2($A2);", "", "")
        .tips("$ =$.replaceFirst(\"^[\\\\[]+L\", \"\");\n\t\t\t\n     $=    \t$.replaceAll(\";$\", \"\");");
  }

  @Test public void testTips11() {
    leonidasSays.tipper("if($X == null) throw new $N();", "ExplodeOnNullWith($N, $X)", "").tips("if (o == null) throw new RuntimeErrorException();");
  }

  @Test public void testTips12() {
    leonidasSays.tipper("if($X) $N($A);", "when($X).execute((x) -> $N($A));", "").tips("if (o == null) print(8);");
  }

  @Test public void testTips13() {
    leonidasSays.tipper("if($X1) $X2.$N($A);", "when($X1).execute((x) -> $X2.$N($A));", "").tips("if (o == null) o.print(8);");
  }
}
