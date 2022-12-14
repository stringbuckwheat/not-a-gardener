package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.RegisterDao;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
public class RegisterDaoImpl implements RegisterDao {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public Optional<Member> selectIdByInputId(String id) {
        return loginRepository.findById(id);
    }

    @Override
    public Member addMember(Member memberEntity) {
        return loginRepository.save(memberEntity);
    }
}
