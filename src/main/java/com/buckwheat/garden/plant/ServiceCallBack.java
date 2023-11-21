package com.buckwheat.garden.plant;

import com.buckwheat.garden.data.entity.Plant;

@FunctionalInterface
public interface ServiceCallBack<T> {
    T execute(Plant plant);
}
