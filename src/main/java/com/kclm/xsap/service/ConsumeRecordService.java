package com.kclm.xsap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kclm.xsap.dto.ConsumeDTO;
import com.kclm.xsap.dto.TeacherInfoDto;
import com.kclm.xsap.entity.ConsumeRecordEntity;
import com.kclm.xsap.entity.EmployeeEntity;

import java.util.List;

public interface ConsumeRecordService extends IService<ConsumeRecordEntity> {
    List<TeacherInfoDto> selectTeacherByYear(Integer year);
    List<TeacherInfoDto> selectTeacherBetweenYear(Integer beginYear,Integer endYear);
    ConsumeDTO classCostMonth(Integer teacherId,Integer month,Integer year);
    ConsumeDTO classCostQuarter(Integer teacherId, Integer quarter, Integer year);
    ConsumeDTO classCostYear(Integer teacherId, Integer year);
}
