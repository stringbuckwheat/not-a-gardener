package xyz.notagardener.domain.chemical.repository;

import xyz.notagardener.domain.chemical.Chemical;

public interface ServiceCallBack<T> {
    T execute(Chemical chemical);
}
