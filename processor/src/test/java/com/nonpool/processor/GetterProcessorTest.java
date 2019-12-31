package com.nonpool.processor;


import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class GetterProcessorTest {
    @Test
    public void should_generate_getter_success_when_compile_given_a_normal_POJO() {
        Compilation compilation =
                javac()
                        .withProcessors(new GetterProcessor())
                        .compile(JavaFileObjects.forResource("Dummy.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}