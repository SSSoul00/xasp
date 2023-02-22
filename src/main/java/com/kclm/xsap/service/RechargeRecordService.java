package com.kclm.xsap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kclm.xsap.entity.RechargeRecordEntity;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface RechargeRecordService extends IService<RechargeRecordEntity> {
    List<Year> selectAllYear();

    Integer selectMoneyByMonth(Integer month, Integer year);

    Integer selectMoneyByQuarter(Integer quarter,Integer year);

    Integer selectMoneyByYear(Integer year);

    Integer selectMoneyByDay(LocalDate date);
}
