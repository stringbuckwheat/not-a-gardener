package com.buckwheat.garden.global.filter.oauth2;

import com.buckwheat.garden.domain.gardener.repository.RedisRepository;
import com.buckwheat.garden.domain.gardener.token.ActiveGardener;
import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.global.error.code.ExceptionCode;
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
