package com.kclm.xsap.dao;

import com.kclm.xsap.dto.ConsumeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConsumeRecordDaoTest {
    @Autowired
    private ConsumeRecordDao consumeRecordDao;

    @Test
    void selectTeacherByYear() {
        System.out.println(consumeRecordDao.selectTeacherByYear(2022));
    }

    @Test
    void classCostMonth(){
        ConsumeDTO consumeDTO = consumeRecordDao.classCostMonth(1, 1, 2021);
        System.out.println(consumeDTO);
    }

    @Test
    void classCostQuarter(){
        ConsumeDTO consumeDTO = consumeRecordDao.classCostQuarter(1, 1, 2023);
        System.out.println(consumeDTO);
    }

    @Test
    void classCostYear(){
        ConsumeDTO consumeDTO = consumeRecordDao.classCostYear(1, 2023);
        System.out.println(consumeDTO);
    }
}