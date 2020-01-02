package com.nonpool.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class FactoryProcessorTest {
    @Test
    public void should_generate_factory_success_when_compile_given_a_class_with_no_parameter_constructor() {
        Compilation compilation = javac().withProcessors(new FactoryProcessor())
                .compile(JavaFileObjects.forResource("Handle.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("com.nonpool.application.HandleFactory")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("HandleFactory.java"));

    }
}