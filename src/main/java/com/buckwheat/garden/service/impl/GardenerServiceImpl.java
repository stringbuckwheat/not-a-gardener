package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.data.entity.Gardener;
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

    @Override
    public GardenerDto.Detail getOne(Long id) {
        return GardenerDto.Detail.from(gardenerDao.getGardenerById(id));
    }

    @Override
    public void updatePassword(Long id, GardenerDto.Login login) {
        Gardener gardener = gardenerDao.getGardenerById(id);
        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);

        gardenerDao.save(gardener);
    }

    @Override
    public boolean identify(Long id, GardenerDto.Login login) {
        Gardener gardener = gardenerDao.getGardenerById(id);
        return encoder.matches(login.getPassword(), gardener.getPassword());
    }

    @Override
    public GardenerDto.Detail modify(GardenerDto.Detail gardenerDetail) {
        Gardener gardener = gardenerDao.getGardenerByGardenerId(gardenerDetail.getId())
                .orElseThrow(NoSuchElementException::new);
        gardener.updateEmailAndName(gardenerDetail.getEmail(), gardenerDetail.getName());

        return GardenerDto.Detail.from(gardenerDao.save(gardener));
    }

    @Override
    public void delete(Long gardenerId) {
        gardenerDao.deleteBy(gardenerId);
    }
}
