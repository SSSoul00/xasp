package com.kclm.xsap.dto.convert;

import com.kclm.xsap.entity.*;
import com.kclm.xsap.service.CourseService;
import com.kclm.xsap.vo.ReserveVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface ReserveVoConvert {
    @Mappings({
            @Mapping(source = "memberCard.name",target = "cardName"),
            @Mapping(source = "scheduleRecord.classTime",target = "classTime"),
            @Mapping(source = "reservationRecord.comment",target = "comment"),
            @Mapping(source = "reservationRecord.createTime",target = "createTime"),
            @Mapping(source = "reservationRecord.lastModifyTime",target = "lastModifyTime"),
            @Mapping(source = "memberEntity.name",target = "memberName"),
            @Mapping(source = "reservationRecord.createTime",target = "operateTime"),
            @Mapping(source = "reservationRecord.operator",target = "operator"),
            @Mapping(source = "memberEntity.phone",target = "phone"),
            @Mapping(source = "reservationRecord.id",target = "reserveId"),
            @Mapping(source = "reservationRecord.note",target = "reserveNote"),
            @Mapping(source = "reservationRecord.reserveNums",target = "reserveNumbers"),
            @Mapping(source = "reservationRecord.status",target = "reserveStatus"),
            @Mapping(source = "scheduleRecord.startDate",target = "startDate"),
            @Mapping(source = "course.timesCost",target = "timesCost"),
            @Mapping(source = "classRecord.checkStatus",target = "classCheck")
    })
    ReserveVo entity2Vo(ReservationRecordEntity reservationRecord, ScheduleRecordEntity scheduleRecord, MemberEntity memberEntity
            , MemberCardEntity memberCard, CourseEntity course,ClassRecordEntity classRecord);
}
