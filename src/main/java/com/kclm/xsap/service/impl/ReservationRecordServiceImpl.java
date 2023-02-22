package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.ReservationRecordDao;
import com.kclm.xsap.entity.ReservationRecordEntity;
import com.kclm.xsap.service.ReservationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationRecordServiceImpl extends ServiceImpl<ReservationRecordDao, ReservationRecordEntity> implements ReservationRecordService {
    @Autowired
    private ReservationRecordDao reservationRecordDao;
    @Override
    public List<Integer> activeMembers(LocalDate startDate, LocalDate endDate) {
        return reservationRecordDao.activeMembers(startDate,endDate);
    }

    @Override
    public Integer totalReservations(LocalDate startDate, LocalDate endDate) {
        return reservationRecordDao.totalReservations(startDate,endDate);
    }
}
