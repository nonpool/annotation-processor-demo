package com.nonpool.processor.helloworld;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.nonpool.processor.helloworld.Anno")
public class AnnoProc extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> elems, RoundEnvironment renv) {
        System.out.println("我在编译的时候打印");
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}