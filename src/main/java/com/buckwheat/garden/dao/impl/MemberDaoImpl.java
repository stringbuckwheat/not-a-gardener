package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.MemberDao;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDaoImpl implements MemberDao {
    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> getMemberByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    @Override
    public Member getMemberForLogin(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("아이디 오류"));
    }

    @Override
    public List<Member> getMemberByEmail(String email){
        return memberRepository.findByEmailAndProviderIsNull(email);
    }

    @Override
    public Optional<Member> getMemberByMemberId(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Optional<Member> getMemberByUsernameAndProvider(String email, String provider){
        return memberRepository.findByUsernameAndProvider(email, provider);
    }

    @Override
    public Member save(Member member){
        return memberRepository.save(member);
    }

    @Override
    public void deleteBy(Long id) {
        memberRepository.deleteById(id);
    }
}
