package il.ac.technion.cs.ssdl.spartan.refactoring.preferencesTab;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;

/**
 * TODO: Document
 * <p>
 * TODO: Change to compilerNotificator
 * 
 */
public class compilerNotificator extends CompilationParticipant {
	@Override public int aboutToBuild(@SuppressWarnings("unused") final IJavaProject project) {
		return 0;
	}
	@Override public boolean isActive(@SuppressWarnings("unused") final IJavaProject project) {
		return true;
	}
	@Override public void reconcile(final ReconcileContext context) {
		// context.putProblems("test", problems);
	}
}