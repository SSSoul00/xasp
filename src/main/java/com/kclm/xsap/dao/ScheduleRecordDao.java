package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.entity.ScheduleRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ScheduleRecordDao extends BaseMapper<ScheduleRecordEntity> {
    @Select("select count(*)\n" +
            "from xsap_dev7.t_schedule_record as sr\n" +
            "where MONTH(sr.start_date) = #{month}\n" +
            "  and YEAR(sr.start_date) =  #{year};")
    Integer classCountMonth(Integer month,Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_schedule_record as sr\n" +
            "where quarter(sr.start_date) = #{quarter}\n" +
            "and YEAR(sr.start_date) = #{year};")
    Integer classCountQuarter(Integer quarter,Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_schedule_record as sr\n" +
            "where YEAR(sr.start_date) = #{year};\n")
    Integer classCountYear(Integer year);
}
