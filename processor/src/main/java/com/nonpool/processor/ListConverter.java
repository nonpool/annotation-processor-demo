package com.nonpool.processor;

import com.sun.tools.javac.util.ListBuffer;

import java.util.ArrayList;
import java.util.List;

public class ListConverter {
    public static <T> List<T> toJavaList(com.sun.tools.javac.util.List<T> toolsList) {
        if (toolsList == null) {
            return null;
        }
        return new ArrayList<>(toolsList);
    }

    public static <T> com.sun.tools.javac.util.List<T> toToolsList(List<T> list) {
        if (list == null) {
            return null;
        }

        com.sun.tools.javac.util.List<T> toolsList = com.sun.tools.javac.util.List.nil();
        ListBuffer<T> listBuffer = new ListBuffer<>();
        listBuffer.addAll(list);
        return toolsList.appendList(listBuffer);
    }


}
