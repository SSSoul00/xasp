package com.kclm.xsap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kclm.xsap.entity.ReservationRecordEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRecordService extends IService<ReservationRecordEntity> {
    List<Integer> activeMembers(LocalDate startDate, LocalDate endDate);
    Integer totalReservations(LocalDate startDate,LocalDate endDate);
}
