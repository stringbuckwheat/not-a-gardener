package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.projection.Username;

import java.util.List;
import java.util.Optional;

public interface GardenerDao {
    Optional<Gardener> readByUsername(String username);
    Gardener getGardenerForLogin(String username);
    Optional<Gardener> getGardenerByGardenerId(Long id); //
    GardenerDetail getGardenerDetailByGardenerId(Long id);
    Gardener getGardenerById(Long id);
    List<Username> getUsernameByEmail(String email);
    Optional<Gardener> getGardenerByUsernameAndProvider(String email, String provider);
    Gardener save(Gardener gardener);
    void deleteBy(Long id);
}