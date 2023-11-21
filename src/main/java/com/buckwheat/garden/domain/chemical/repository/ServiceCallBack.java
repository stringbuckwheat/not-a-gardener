package com.buckwheat.garden.domain.chemical.repository;

import com.buckwheat.garden.domain.chemical.Chemical;

public interface ServiceCallBack<T> {
    T execute(Chemical chemical);
}
