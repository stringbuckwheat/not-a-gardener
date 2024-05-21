package xyz.notagardener.gardener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.authentication.service.AuthenticationService;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.HasSameUsernameException;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.Register;
import xyz.notagardener.gardener.dto.VerifyResponse;
import xyz.notagardener.gardener.repository.GardenerRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenerServiceImpl implements GardenerService {
    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder encoder;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public GardenerDetail getOne(Long id) {
        return gardenerRepository.findGardenerDetailByGardenerId(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_ACCOUNT));
    }

    @Override
    @Transactional(readOnly = true)
    public VerifyResponse identify(Long id, Login login) {
        Gardener gardener = gardenerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_ACCOUNT));
        boolean verified = encoder.matches(login.getPassword(), gardener.getPassword());

        return new VerifyResponse("비밀번호", verified, "");
    }

    @Override
    @Transactional(readOnly = true)
    public String hasSameUsername(String username) {
        Optional<Gardener> gardener = gardenerRepository.findByProviderIsNullAndUsername(username);

        if(!gardener.isEmpty()) {
            throw new HasSameUsernameException(ExceptionCode.HAS_SAME_USERNAME, username);
        }

        return username;
    }

    @Override
    @Transactional
    public Info add(Register register) {
        boolean hasSameId = hasSameUsername(register.getUsername()) != null;

        // DTO에 암호화된 비밀번호 저장한 뒤 엔티티로 변환
        Gardener gardener = gardenerRepository.save(
                register
                        .encryptPassword(encoder.encode(register.getPassword()))
                        .toEntity()
        );

        return authenticationService.setAuthentication(gardener);
    }

    @Override
    @Transactional
    public void updatePassword(Long id, Login login) {
        Gardener gardener = gardenerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_ACCOUNT));

        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);
    }

    @Override
    @Transactional
    public GardenerDetail update(GardenerDetail gardenerDetail) {
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
