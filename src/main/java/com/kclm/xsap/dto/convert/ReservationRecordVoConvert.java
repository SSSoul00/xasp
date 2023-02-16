package com.kclm.xsap.dto.convert;

import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.entity.ReservationRecordEntity;
import com.kclm.xsap.entity.ScheduleRecordEntity;
import com.kclm.xsap.vo.ReservationRecordVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring")
public interface ReservationRecordVoConvert {
    @Mappings({
            @Mapping(source = "record.id", target = "reserveId"),
            @Mapping(source = "record.status", target = "reserveStatus"),
            @Mapping(source = "record.createTime", target = "operateTime"),
            @Mapping(source = "record.classNote", target = "reserveNote"),
            @Mapping(source = "scheduleRecord.orderNums", target = "reserveNumbers"),
//            @Mapping(source = "record.createTime", target = "reserveTime"),
            @Mapping(source = "course.name", target = "courseName"),
    })
    ReservationRecordVo entity2Vo(ReservationRecordEntity record, CourseEntity course, ScheduleRecordEntity scheduleRecord, LocalDateTime reserveTime);
}
