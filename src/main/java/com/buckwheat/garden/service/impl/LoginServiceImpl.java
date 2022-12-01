package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dao.LoginDao;
import com.buckwheat.garden.data.dto.JwtAuthToken;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.PasswordAuthenticationToken;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.entity.MemberEntity;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import com.buckwheat.garden.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager passwordAuthAuthenticationManager;

    @Autowired
    private JwtAuthTokenProvider tokenProvider;

    // DTO <-> Entity <-> DTO
    @Override
    public String getIdByInputId(String id) {
        log.debug("id: " + id);

        Optional<MemberEntity> registerEntity = loginDao.selectIdByInputId(id);

        if(registerEntity.isEmpty()){
            return null;
        }

        return registerEntity.get().getId();
    }

    @Override
    public RegisterDto addMember(RegisterDto paramRegisterDto) {
        log.debug("addMember(): " + paramRegisterDto);

        // DTO에 암호화된 비밀번호 저장하기
        paramRegisterDto.encryptPassword(encoder.encode(paramRegisterDto.getPw()));

        // DB에 저장
        MemberEntity memberEntity = loginDao.addMember(paramRegisterDto.toEntity());
        log.debug("DB save: " + memberEntity);

        // TODO 회원 가입 시 자동 로그인

        return null;
    }

    @Override
    public String login(MemberDto memberDto) {
        log.debug("parameter check: " + memberDto);

        // PasswordAuthAuthenticationToken(UsernamePasswordAuthenticationToken를 상속받은 클래스)의
        // (Object principal, Object credentials) 생성자로 조상 생성자 초기화
        // principal: 사용자 본인 / credentials: 자격 증명
        PasswordAuthenticationToken token = new PasswordAuthenticationToken(memberDto.getId(), memberDto.getPw());

        // 위 객체로 인증하러 가서 PasswordAuthenticationToken 받아옴
        Authentication authentication = passwordAuthAuthenticationManager.authenticate(token);
        log.debug("authenticationManager.authenticate(token): " + authentication);

        // SecurityContextHolder: Spring Security가 인증한 내용들을 저장하는 공감
        // 내부에 SecurityContext가 있고, 이를 현재 스레드와 연결해주는 역할
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 아래 createToken 메소드로 반환받은 token을 보내 JwtAuthToken.token을 반환
        return createToken((PasswordAuthenticationToken) authentication);
    }

    public String createToken(PasswordAuthenticationToken token){
        log.debug("token: " + token);

        // LocalDateTime.now(): 지금 // cf) LocalDate.now(): 오늘
        // .plusMinutes(180): 유효 시간 3시간
        // .atZone(ZoneId.systemDefault()):
        // .toInstant(): Date.from()의 파라미터는 Instant
        //      -> Year 2038 problem을 해결하기 위해서 나온 타입
        // Date.from(): LocalDateTime -> java.util.Date
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());

        // Payload: 토큰에 담을 정보가 들어가는 공간
        // claim: 그 정보의 한 조각 (name, value 쌍으로 구성)
        Map<String, String> claims = new HashMap<>();

        // String이 아니야??
        claims.put("id", token.getId());
        claims.put("name", token.getName());
        // claims.put("role", token.getRole());

        // key를 넣은 JwtAuthToken을 반환
        // String id, Map<String, String> claims, Date expiredDate
        JwtAuthToken jwtAuthToken = tokenProvider.createAuthToken(
                token.getPrincipal().toString(),
                // token.getAuthorities().iterator().next().getAuthority(),
                claims,
                expiredDate
        );

        return jwtAuthToken.getToken();
    }
}
