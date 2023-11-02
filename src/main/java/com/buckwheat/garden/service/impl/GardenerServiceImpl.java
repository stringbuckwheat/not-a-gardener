package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.dto.gardener.Login;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.service.GardenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenerServiceImpl implements GardenerService {
    private final BCryptPasswordEncoder encoder;
    private final GardenerDao gardenerDao;

    @Override
    public GardenerDetail getOne(Long id) {
        return gardenerDao.getGardenerDetailByGardenerId(id);
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
    @Transactional
    public GardenerDetail update(GardenerDetail gardenerDetail) {
        Gardener gardener = gardenerDao.getGardenerByGardenerId(gardenerDetail.getId())
                .orElseThrow(NoSuchElementException::new);
        gardener.updateEmailAndName(gardenerDetail.getEmail(), gardenerDetail.getName());

        return GardenerDetail.builder()
                .id(gardener.getGardenerId())
                .username(gardener.getUsername())
                .email(gardener.getEmail())
                .name(gardener.getName())
                .createDate(gardener.getCreateDate())
                .provider(gardener.getProvider())
                .build();
    }

    @Override
    public void delete(Long gardenerId) {
        gardenerDao.deleteBy(gardenerId);
    }
}
