package com.nonpool.processor;

import com.nonpool.annotation.Getter;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nonpool.processor.ListConverter.toToolsList;
import static java.util.stream.Collectors.toList;

@SupportedAnnotationTypes("com.nonpool.annotation.Getter")
public class GetterProcessor extends AbstractProcessor {

    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Getter.class);

        Set<JCTree.JCClassDecl> jCClassDeclSet = set.stream()
                .map(element -> trees.getTree(element))
                .filter(tree -> tree.getKind() == Tree.Kind.CLASS)
                .map(tree -> (JCTree.JCClassDecl) tree)
                .collect(Collectors.toSet());

        for (JCTree.JCClassDecl jcClassDecl : jCClassDeclSet) {
            java.util.List<JCTree> collect =
                    jcClassDecl.defs.stream()
                            .filter(treeDef -> treeDef.getKind() == Tree.Kind.VARIABLE)
                            .map(treeDef -> (JCTree.JCVariableDecl) treeDef)
                            .map(this::makeGetterMethodDecl)
                            .map(jCMethodDecl -> (JCTree) jCMethodDecl)
                            .collect(toList());

            List<JCTree> jcMethodDecls = toToolsList(collect);
            messager.printMessage(Diagnostic.Kind.NOTE, jcMethodDecls.toString());
            jcClassDecl.defs = jcClassDecl.defs.prependList(jcMethodDecls);
        }

        java.util.List<JCTree.JCVariableDecl> jcVariableDeclList = set.stream()
                .map(element -> trees.getTree(element))
                .filter(tree -> tree.getKind() == Tree.Kind.VARIABLE)
                .map(tree -> (JCTree.JCVariableDecl) tree)
                .collect(toList());

        if (!jcVariableDeclList.isEmpty()) {
            java.util.List<JCTree> clolect1 = jcVariableDeclList.stream()
                    .map(this::makeGetterMethodDecl)
                    .collect(toList());

            List<JCTree> jcMethodDecls = toToolsList(clolect1);
            JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl) trees.getTree(jcVariableDeclList.get(0).sym.owner);
            jCClassDecl.defs = jCClassDecl.defs.appendList(jcMethodDecls);
        }

        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {

        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()), jcVariableDecl.vartype, List.nil(), List.nil(), List.nil(), body, null);
    }

    private Name getNewMethodName(Name name) {
        String s = name.toString();
        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
