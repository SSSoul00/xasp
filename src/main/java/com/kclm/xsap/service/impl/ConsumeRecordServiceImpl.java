package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.ConsumeRecordDao;
import com.kclm.xsap.dto.ConsumeDTO;
import com.kclm.xsap.dto.TeacherInfoDto;
import com.kclm.xsap.entity.ConsumeRecordEntity;
import com.kclm.xsap.entity.EmployeeEntity;
import com.kclm.xsap.service.ConsumeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeRecordServiceImpl extends ServiceImpl<ConsumeRecordDao, ConsumeRecordEntity> implements ConsumeRecordService {
    @Autowired
    private ConsumeRecordDao consumeRecordDao;
    @Override
    public List<TeacherInfoDto> selectTeacherByYear(Integer year) {
        return consumeRecordDao.selectTeacherByYear(year);
    }

    @Override
    public List<TeacherInfoDto> selectTeacherBetweenYear(Integer beginYear, Integer endYear) {
        return consumeRecordDao.selectTeacherBetweenYear(beginYear,endYear);
    }

    @Override
    public ConsumeDTO classCostMonth(Integer teacherId, Integer month, Integer year) {
        return consumeRecordDao.classCostMonth(teacherId,month,year);
    }

    @Override
    public ConsumeDTO classCostQuarter(Integer teacherId, Integer quarter, Integer year) {
        return consumeRecordDao.classCostQuarter(teacherId,quarter,year);
    }

    @Override
    public ConsumeDTO classCostYear(Integer teacherId, Integer year) {
        return consumeRecordDao.classCostYear(teacherId,year);
    }
}
