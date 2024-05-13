package xyz.notagardener.common.filter.oauth2;

import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.domain.gardener.repository.RedisRepository;
import xyz.notagardener.domain.gardener.token.ActiveGardener;
import xyz.notagardener.domain.gardener.token.UserPrincipal;
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
    private final RedisRepository redisRepository;

    /**
     * @param username gardenerId가 저장되어 있다
     * @return Gardener 객체를 포함한 UserPrincipal -> UsernamePasswordAuthenticationToken에 넣는다
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ActiveGardener gardener = redisRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_TOKEN_IN_REDIS.getCode()));
        
        return new UserPrincipal(gardener.getGardenerId(), gardener.getName());
    }
}
