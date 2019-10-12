package com.xquant.parser.node;

public interface ForEachProcessor<T> {
    void foreach(Processor<T> processor);
}
