package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.repository.GardenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GardenerDaoImpl implements GardenerDao {
    private final GardenerRepository gardenerRepository;

    @Override
    public Optional<Gardener> getGardenerByUsername(String username){
        return gardenerRepository.findByUsername(username);
    }

    @Override
    public Gardener getGardenerForLogin(String username) {
        return gardenerRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("아이디 오류"));
    }

    @Override
    public List<Gardener> getGardenerByEmail(String email){
        return gardenerRepository.findByEmailAndProviderIsNull(email);
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
}
