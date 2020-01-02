package com.nonpool.processor;

import com.nonpool.annotation.Factory;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.JCTree;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.nonpool.annotation.Factory")
public class FactoryProcessor extends AbstractProcessor {

    private Filer filer;
    private JavacTrees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.trees = JavacTrees.instance(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Factory.class);

        Set<JCTree.JCClassDecl> jCClassDeclSet = set.stream()
                .map(element -> (JCTree.JCClassDecl) trees.getTree(element))
                .collect(Collectors.toSet());

        for (JCTree.JCClassDecl jcClassDecl : jCClassDeclSet) {

            String fullName = jcClassDecl.sym.fullname.toString();
            String className = jcClassDecl.sym.name.toString();

            try (Writer writer = filer.createSourceFile(generatedClassFullName(fullName)).openWriter()) {
                writer.write(String.format("package %s; public class %s {" +
                        "    public static final %s %s = new %s();" +
                        "}", getPackageName(fullName), className + "Factory", fullName, className.toLowerCase(), fullName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private String generatedClassFullName(String fullName) {
        return fullName + "Factory";
    }

    private String getPackageName(String fullName) {
        if (!fullName.contains(".")) {
            return fullName;
        }
        return fullName.substring(0, fullName.lastIndexOf("."));
    }
}
