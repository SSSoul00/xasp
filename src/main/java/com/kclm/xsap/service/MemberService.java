package com.kclm.xsap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kclm.xsap.entity.MemberEntity;

import java.time.LocalDate;

public interface MemberService extends IService<MemberEntity> {
    Integer addCountMonth(Integer month,Integer year);
    Integer streamCountMonth(Integer month,Integer year);
    Integer addCountQuarter(Integer quarter,Integer year);
    Integer streamCountQuarter(Integer quarter,Integer year);
    Integer addCountYear(Integer year);
    Integer streamCountYear(Integer year);
    Integer addCountDay(LocalDate date);
    Integer streamCountDay(LocalDate date);
    MemberEntity selectById(Long id);
    Integer totalMembers();
}
