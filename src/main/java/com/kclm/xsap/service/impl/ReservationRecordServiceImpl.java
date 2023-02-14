package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.ReservationRecordDao;
import com.kclm.xsap.entity.ReservationRecordEntity;
import com.kclm.xsap.service.ReservationRecordService;
import org.springframework.stereotype.Service;

@Service
public class ReservationRecordServiceImpl extends ServiceImpl<ReservationRecordDao, ReservationRecordEntity> implements ReservationRecordService {
}
