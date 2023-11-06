package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.dto.gardener.Login;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.repository.command.GardenerCommandRepository;
import com.buckwheat.garden.repository.query.GardenerQueryRepository;
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
    private final GardenerQueryRepository gardenerQueryRepository;
    private final GardenerCommandRepository gardenerCommandRepository;

    @Override
    @Transactional(readOnly = true)
    public GardenerDetail getOne(Long id) {
        return gardenerQueryRepository.findGardenerDetailByGardenerId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean identify(Long id, Login login) {
        Gardener gardener = gardenerQueryRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return encoder.matches(login.getPassword(), gardener.getPassword());
    }

    @Override
    @Transactional
    public void updatePassword(Long id, Login login) {
        Gardener gardener = gardenerCommandRepository.findById(id).orElseThrow(NoSuchElementException::new);
        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);
    }

    @Override
    @Transactional
    public GardenerDetail update(GardenerDetail gardenerDetail) {
        Gardener gardener = gardenerCommandRepository.findById(gardenerDetail.getId()).orElseThrow(NoSuchElementException::new);
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
        gardenerCommandRepository.deleteById(gardenerId);
    }
}
