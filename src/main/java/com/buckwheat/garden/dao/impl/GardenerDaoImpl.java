package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.projection.Username;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.error.code.ExceptionCode;
import com.buckwheat.garden.repository.GardenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GardenerDaoImpl implements GardenerDao {
    private final GardenerRepository gardenerRepository;

    @Override
    public Optional<Gardener> readByUsername(String username){
        return gardenerRepository.findByProviderIsNullAndUsername(username);
    }

    @Override
    public Gardener getGardenerForLogin(String username) {
        return gardenerRepository.findByProviderIsNullAndUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));
    }

    @Override
    public List<Username> getUsernameByEmail(String email){
        return gardenerRepository.findByProviderIsNullAndEmail(email);
    }

    @Override
    public Optional<Gardener> getGardenerByGardenerId(Long id) {
        return gardenerRepository.findById(id);
    }

    @Override
    public Optional<Gardener> getGardenerByUsernameAndProvider(String email, String provider){
        return gardenerRepository.findByUsernameAndProvider(email, provider);
    }

    @Override
    public Gardener save(Gardener gardener){
        return gardenerRepository.save(gardener);
    }

    @Override
    public void deleteBy(Long id) {
        gardenerRepository.deleteById(id);
    }

    @Override
    public Gardener getGardenerById(Long id) {
        return gardenerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public GardenerDetail getGardenerDetailByGardenerId(Long id) {
        return gardenerRepository.findGardenerDetailByGardenerId(id);
    }
}
