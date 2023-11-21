package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.plant.Plant;

@FunctionalInterface
public interface ServiceCallBack<T> {
    T execute(Plant plant);
}
