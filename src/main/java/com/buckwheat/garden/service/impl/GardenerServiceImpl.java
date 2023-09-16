package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.dto.gardener.Login;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.repository.RedisRepository;
import com.buckwheat.garden.service.GardenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenerServiceImpl implements GardenerService {
    private final BCryptPasswordEncoder encoder;
    private final GardenerDao gardenerDao;
    private final RedisRepository redisRepository;

    @Override
    public GardenerDetail getOne(Long id) {
        return GardenerDetail.from(gardenerDao.getGardenerById(id));
    }

    @Override
    public void updatePassword(Long id, Login login) {
        Gardener gardener = gardenerDao.getGardenerById(id);
        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);

        gardenerDao.save(gardener);
    }

    @Override
    public boolean identify(Long id, Login login) {
        Gardener gardener = gardenerDao.getGardenerById(id);
        return encoder.matches(login.getPassword(), gardener.getPassword());
    }

    @Override
    public void logOut(Long id) {
        redisRepository.deleteById(id);
    }

    @Override
    public GardenerDetail modify(GardenerDetail gardenerDetail) {
        Gardener gardener = gardenerDao.getGardenerByGardenerId(gardenerDetail.getId())
                .orElseThrow(NoSuchElementException::new);
        gardener.updateEmailAndName(gardenerDetail.getEmail(), gardenerDetail.getName());

        return GardenerDetail.from(gardenerDao.save(gardener));
    }

    @Override
    public void delete(Long gardenerId) {
        gardenerDao.deleteBy(gardenerId);
    }
}
