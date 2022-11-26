package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dao.LoginDao;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.entity.MemberEntity;
import com.buckwheat.garden.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

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

        // 비밀번호 암호화
        // paramRegisterDto.encryptPassword(encoder.encode(paramRegisterDto.getPw()));

        // DB에 저장
        MemberEntity memberEntity = loginDao.addMember(paramRegisterDto.toEntity());
        log.debug("DB save: " + memberEntity);

        // 빌더 패턴으로 새 Dto 만들기
        RegisterDto registerDto = null;

        return registerDto;
    }
}
