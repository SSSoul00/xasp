package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.MemberDao;
import com.kclm.xsap.entity.MemberEntity;
import com.kclm.xsap.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Integer addCountMonth(Integer month, Integer year) {
        return memberDao.addCountMonth(month,year);
    }

    @Override
    public Integer streamCountMonth(Integer month, Integer year) {
        return memberDao.streamCountMonth(month, year) * (-1);
    }

    @Override
    public Integer addCountQuarter(Integer quarter, Integer year) {
        return memberDao.addCountQuarter(quarter, year);
    }

    @Override
    public Integer streamCountQuarter(Integer quarter, Integer year) {
        return memberDao.streamCountQuarter(quarter, year) * (-1);
    }

    @Override
    public Integer addCountYear(Integer year) {
        return memberDao.addCountYear(year);
    }

    @Override
    public Integer streamCountYear(Integer year) {
        return memberDao.streamCountYear(year) * (-1);
    }

    @Override
    public Integer addCountDay(LocalDate date) {
        return memberDao.addCountDay(date);
    }

    @Override
    public Integer streamCountDay(LocalDate date) {
        return memberDao.streamCountDay(date);
    }

    @Override
    public MemberEntity selectById(Long id) {
        return memberDao.selectById(id);
    }

    @Override
    public Integer totalMembers() {
        return memberDao.totalMembers();
    }
}
