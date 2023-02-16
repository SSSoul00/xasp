package com.kclm.xsap.dto.convert;

import com.kclm.xsap.entity.ClassRecordEntity;
import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.entity.ScheduleRecordEntity;
import com.kclm.xsap.vo.ClassRecordVoo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ClassRecordConvertt {
    @Mappings({
            @Mapping(source = "classRecord.id", target = "classRecordId"),
            @Mapping(source = "course.name", target = "courseName"),
            @Mapping(source = "scheduleRecord.startDate", target = "scheduleStartDate"),
            @Mapping(source = "scheduleRecord.classTime", target = "scheduleStartTime"),
            @Mapping(source = "classTime",target = "classTime")
    })
    ClassRecordVoo entity2Vo(ClassRecordEntity classRecord, ScheduleRecordEntity scheduleRecord
            , Integer classNumbers, CourseEntity course, String teacherName, LocalDateTime classTime);
}
