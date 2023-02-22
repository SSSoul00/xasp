package com.kclm.xsap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kclm.xsap.entity.ScheduleRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;

public interface ScheduleRecordService extends IService<ScheduleRecordEntity> {
    Integer classCountMonth(Integer month, Integer year);

    Integer classCountQuarter(Integer quarter, Integer year);

    public Integer classCountYear(Integer year);
}
