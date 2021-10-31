package com.taylorswiftcn.megumi.pathfinding.algorithm;

public interface PathCallback<T> {

    void success(T path);

    void fail();
}
