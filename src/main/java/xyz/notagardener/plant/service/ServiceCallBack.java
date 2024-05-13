package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.plant.Plant;

@FunctionalInterface
public interface ServiceCallBack<T> {
    T execute(Plant plant);
}
