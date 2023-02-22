package com.kclm.xsap.dao;

import com.kclm.xsap.vo.indexStatistics.IndexAddAndStreamInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RechargeRecordDaoTest {
    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    @Test
    void selectAllYear() {
        rechargeRecordDao.selectAllYear().forEach(item->{
            System.out.println(item);
        });
    }
    @Test
    void selectMoneyByMonth(){
        System.out.println(rechargeRecordDao.selectMoneyByMonth(12, 2021));
    }

    @Test
    void selectMoneyByQuarter(){
        System.out.println(rechargeRecordDao.selectMoneyByQuarter(1,2022));
    }

    @Test
    void selectMoneyByYear(){
        System.out.println(rechargeRecordDao.selectMoneyByYear(2021));
    }
}