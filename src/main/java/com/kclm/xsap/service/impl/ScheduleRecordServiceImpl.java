package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.ScheduleRecordDao;
import com.kclm.xsap.entity.ScheduleRecordEntity;
import com.kclm.xsap.service.ScheduleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleRecordServiceImpl extends ServiceImpl<ScheduleRecordDao, ScheduleRecordEntity> implements ScheduleRecordService {
    @Autowired
    private ScheduleRecordDao scheduleRecordDao;

    @Override
    public Integer classCountMonth(Integer month, Integer year) {
        return scheduleRecordDao.classCountMonth(month,year);
    }

    @Override
    public Integer classCountQuarter(Integer quarter, Integer year) {
        return scheduleRecordDao.classCountQuarter(quarter,year);
    }

    @Override
    public Integer classCountYear(Integer year) {
        return scheduleRecordDao.classCountYear(year);
    }
}
