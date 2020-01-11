package com.nonpool.application;

import com.nonpool.annotation.Monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySort {

    @Monitor
    public List<Integer> sort(List<Integer> list) {
        Collections.sort(list);
        return new ArrayList<>(list);
    }

    public List<Integer> $sort_monitor(List<Integer> list) {
        return sort(list);
    }
}
