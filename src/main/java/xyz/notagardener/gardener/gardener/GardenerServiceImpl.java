package xyz.notagardener.gardener.gardener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.authentication.dto.Login;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenerServiceImpl implements GardenerService {
    private final BCryptPasswordEncoder encoder;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public GardenerDetail getOne(Long id) {
        return gardenerRepository.findGardenerDetailByGardenerId(id)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean identify(Long id, Login login) {
        Gardener gardener = gardenerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));

        return encoder.matches(login.getPassword(), gardener.getPassword());
    }

    @Override
    @Transactional
    public void updatePassword(Long id, Login login) {
        Gardener gardener = gardenerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.WRONG_ACCOUNT.getCode()));

        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);
    }

    @Override
    @Transactional
    public GardenerDetail update(GardenerDetail gardenerDetail) {
        if(!gardenerDetail.isValid()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        Gardener gardener = gardenerRepository.findById(gardenerDetail.getId())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));
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
