package com.kclm.xsap.dao;

import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleRecordDaoTest {
    @Autowired
    private ScheduleRecordDao scheduleRecordDao;
    @Autowired
    private MemberDao memberDao;

    @Test
    void classCountMonth() {
        Integer integer = scheduleRecordDao.classCountMonth(2, 2023);
        System.out.println(integer);
    }

    @Test
    void classCountQuarter() {
        Integer integer = scheduleRecordDao.classCountQuarter(4, 2021);
        System.out.println(integer);
    }

    @Test
    void classCountYear() {
        Integer integer = scheduleRecordDao.classCountYear(2021);
        System.out.println(integer);
    }

    @Test
    void test01(){
        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = localDate.minusMonths(1);
        System.out.println(localDate);
        System.out.println(localDate1);
    }

    @Test
    void test02(){
        LocalDate localDate = LocalDate.now();
        LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println(localDate);
        System.out.println(firstDay);
    }

    @Test
    void test03(){
        LocalDate localDate = LocalDate.of(2021,12,30);
        System.out.println(memberDao.addCountDay(localDate));
    }
}