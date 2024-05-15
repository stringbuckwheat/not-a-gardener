package xyz.notagardener.common;


import xyz.notagardener.chemical.Chemical;

public interface ServiceCallBack<T> {
    T execute(Chemical chemical);
}
