package xyz.notagardener.authentication.service;

import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.authentication.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final ActiveGardenerRepository activeGardenerRepository;

    /**
     * @param username gardenerId가 저장되어 있다
     * @return Gardener 객체를 포함한 UserPrincipal -> UsernamePasswordAuthenticationToken에 넣는다
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ActiveGardener gardener = activeGardenerRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_TOKEN_IN_REDIS.getCode()));
        
        return new UserPrincipal(gardener.getGardenerId(), gardener.getName());
    }
}
