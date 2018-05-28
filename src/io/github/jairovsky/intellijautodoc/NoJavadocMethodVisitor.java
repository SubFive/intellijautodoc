package io.github.jairovsky.intellijautodoc;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import org.fest.util.Lists;

import java.util.List;

/**
 * Visits java methodsToWrite that don't have javadoc comments.
 */
class NoJavadocMethodVisitor extends JavaRecursiveElementWalkingVisitor {

    private static final Logger logger = Logger.getInstance(NoJavadocMethodVisitor.class);

    private final PsiJavaFile file;
    private final List<PsiMethod> undocumentedMethods;

    NoJavadocMethodVisitor(PsiJavaFile file) {
        this.file = file;
        this.undocumentedMethods = Lists.newArrayList();
    }

    @Override
    public void visitMethod(PsiMethod method) {

        PsiDocComment comment = method.getDocComment();

        if (comment == null) {

            logger.debug("adding method {} to list of undocumented methodsToWrite", method.getName());

            undocumentedMethods.add(method);
        }
    }

    public void execute() {

        file.accept(this);
        new WriteMethodJavadocs(file.getProject(), undocumentedMethods).execute();
    }
}
