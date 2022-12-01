package com.buckwheat.garden.data.dao.impl;

import com.buckwheat.garden.data.dao.LoginDao;
import com.buckwheat.garden.data.entity.MemberEntity;
import com.buckwheat.garden.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
public class LoginDaoImpl implements LoginDao {
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public Optional<MemberEntity> selectIdByInputId(String id) {
        return loginRepository.findById(id);
    }

    @Override
    public MemberEntity addMember(MemberEntity memberEntity) {
        return loginRepository.save(memberEntity);
    }

    @Override
    public Optional<MemberEntity> getMemberByIdAndPw(MemberEntity memberEntity) {
        return loginRepository.findById(memberEntity.getId());
    }
}
