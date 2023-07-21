package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.token.ActiveGardener;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<ActiveGardener, Long> {
}
