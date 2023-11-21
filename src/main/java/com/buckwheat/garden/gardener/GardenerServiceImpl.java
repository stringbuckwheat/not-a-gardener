package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.GardenerDetail;
import com.buckwheat.garden.gardener.gardener.Login;
import com.buckwheat.garden.data.entity.Gardener;
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
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public GardenerDetail getOne(Long id) {
        return gardenerRepository.findGardenerDetailByGardenerId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean identify(Long id, Login login) {
        Gardener gardener = gardenerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return encoder.matches(login.getPassword(), gardener.getPassword());
    }

    @Override
    @Transactional
    public void updatePassword(Long id, Login login) {
        Gardener gardener = gardenerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);
    }

    @Override
    @Transactional
    public GardenerDetail update(GardenerDetail gardenerDetail) {
        Gardener gardener = gardenerRepository.findById(gardenerDetail.getId()).orElseThrow(NoSuchElementException::new);
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
        gardenerRepository.deleteById(gardenerId);
    }
}
