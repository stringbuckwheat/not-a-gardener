package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.dao.MemberDao;
import com.buckwheat.garden.data.entity.Member;
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
    private final MemberDao memberDao;

    /**
     *
     * @param username memberNo가 저장되어 있다
     * @return member 객체를 포함한 UserPrincipal -> UsernamePasswordAuthenticationToken에 넣는다
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberDao.getMemberByMemberNo(Integer.parseInt(username))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        return new UserPrincipal(member);
    }
}
