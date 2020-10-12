package com.kclm.xsap.dto.convert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.kclm.xsap.dto.ClassRecordDTO;
import com.kclm.xsap.entity.TClassRecord;
import com.kclm.xsap.entity.TCourse;
import com.kclm.xsap.entity.TMember;
import com.kclm.xsap.entity.TMemberCard;
import com.kclm.xsap.entity.TScheduleRecord;

/**
 * 
 * @author harima
 * @since JDK11.0
 * @CreateDate 2020年9月18日 下午3:31:02 
 * @description 此类用来描述了上课记录DTO类型转换
 *
 */
@Mapper(componentModel = "spring")
public interface ClassRecordConvert {

	/**
	 * 
	 * @param classRecord 对应上课记录实体类
	 * @param member	对应会员实体类
	 * @param course	对应课程表实体类
	 * @param schedule	对应排课计划实体类
	 * @param card	对应会员卡实体类
	 * @param teacherName	教师名
	 * @param involveMoney	涉及的消费金额
	 * @return ClassRecordDTO。上课记录要展示的信息
	 */
	@Mappings({
		@Mapping(source = "card.name",target = "cardName"),
		@Mapping(source = "course.name",target = "courseName"),
		@Mapping(source = "schedule.orderNums",target = "classNumbers"),
		@Mapping(source = "classRecord.note",target = "classNote"),
		@Mapping(source = "classRecord.id",target = "classRecordId"),
		@Mapping(source = "course.id",target = "courseId"),
		@Mapping(source = "member.id",target = "memberId"),
		@Mapping(source = "schedule.id",target = "scheduleId"),
		@Mapping(source = "card.id",target = "cardId"),
		@Mapping(source = "schedule",target = "classTime"),
		@Mapping(source = "classRecord.createTime",target = "operateTime")
	})
	ClassRecordDTO entity2Dto(TClassRecord classRecord,TMember member,TCourse course,TScheduleRecord schedule,TMemberCard card,String teacherName,BigDecimal involveMoney);
	
	default LocalDateTime scheduleToClassTime(TScheduleRecord schedule) {
		return LocalDateTime.of(schedule.getStartDate(),schedule.getClassTime());
	}
}
