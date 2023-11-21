package com.buckwheat.garden.chemical;

import com.buckwheat.garden.data.entity.Chemical;

public interface ServiceCallBack<T> {
    T execute(Chemical chemical);
}
