package com.xquant.parser.node;

public interface Processor<T> {
    void process(T object);
}