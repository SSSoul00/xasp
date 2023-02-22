package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.RechargeRecordDao;
import com.kclm.xsap.entity.RechargeRecordEntity;
import com.kclm.xsap.service.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
public class RechargeRecordServiceImpl extends ServiceImpl<RechargeRecordDao, RechargeRecordEntity> implements RechargeRecordService {
    @Autowired
    private RechargeRecordDao rechargeRecordDao;
    @Override
    public List<Year> selectAllYear() {
        List<Year> years = rechargeRecordDao.selectAllYear();
        return years;
    }

    @Override
    public Integer selectMoneyByMonth(Integer month, Integer year) {
        return rechargeRecordDao.selectMoneyByMonth(month,year);
    }

    @Override
    public Integer selectMoneyByQuarter(Integer quarter, Integer year) {
        return rechargeRecordDao.selectMoneyByQuarter(quarter,year);
    }

    @Override
    public Integer selectMoneyByYear(Integer year) {
        return rechargeRecordDao.selectMoneyByYear(year);
    }

    @Override
    public Integer selectMoneyByDay(LocalDate date) {
        return rechargeRecordDao.selectMoneyByDay(date);
    }
}
