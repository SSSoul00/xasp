package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.dto.ConsumeDTO;
import com.kclm.xsap.dto.TeacherInfoDto;
import com.kclm.xsap.entity.ConsumeRecordEntity;
import com.kclm.xsap.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConsumeRecordDao extends BaseMapper<ConsumeRecordEntity> {

    @Select("select te.id,case is_deleted when 0 then name when 1 then CONCAT(name, '[已退出]') END as name\n" +
            "from xsap_dev7.t_consume_record as cr\n" +
            "         join xsap_dev7.t_schedule_record sr on cr.schedule_id = sr.id\n" +
            "         join xsap_dev7.t_employee te on te.id = sr.teacher_id\n" +
            "where YEAR(cr.create_time) = #{year}\n" +
            "group by teacher_id;")
    List<TeacherInfoDto> selectTeacherByYear(Integer year);

    @Select("select te.id,case is_deleted when 0 then name when 1 then CONCAT(name, '[已退出]') END as name\n" +
            "from xsap_dev7.t_consume_record as cr\n" +
            "         join xsap_dev7.t_schedule_record sr on cr.schedule_id = sr.id\n" +
            "         join xsap_dev7.t_employee te on te.id = sr.teacher_id\n" +
            "where YEAR(cr.create_time) between #{beginYear} and #{endYear}\n" +
            "group by teacher_id;")
    List<TeacherInfoDto> selectTeacherBetweenYear(Integer beginYear, Integer endYear);

    @Select("select sum(cr.card_count_change) as costCount, sum(cr.money_cost) as costMoney\n" +
            "from xsap_dev7.t_consume_record as cr\n" +
            "where cr.schedule_id in (select id\n" +
            "                         from xsap_dev7.t_schedule_record\n" +
            "                         where teacher_id = #{teacherId}\n" +
            "                           AND YEAR(create_time) = #{year})\n" +
            "  and YEAR(cr.create_time) = #{year} and MONTH(cr.create_time) = #{month};")
    ConsumeDTO classCostMonth(Integer teacherId, Integer month, Integer year);

    @Select("select sum(cr.card_count_change) as costCount, sum(cr.money_cost) as costMoney\n" +
            "from xsap_dev7.t_consume_record as cr\n" +
            "where cr.schedule_id in (select id\n" +
            "                         from xsap_dev7.t_schedule_record\n" +
            "                         where teacher_id = #{teacherId}\n" +
            "                           AND YEAR(create_time) = #{year})\n" +
            "  and YEAR(cr.create_time) = #{year}\n" +
            "  and QUARTER(cr.create_time) = #{quarter};")
    ConsumeDTO classCostQuarter(Integer teacherId, Integer quarter, Integer year);

    @Select("select sum(cr.card_count_change) as costCount, sum(cr.money_cost) as costMoney\n" +
            "from xsap_dev7.t_consume_record as cr\n" +
            "where cr.schedule_id in (select id\n" +
            "                         from xsap_dev7.t_schedule_record\n" +
            "                         where teacher_id = #{teacherId}\n" +
            "                           AND YEAR(create_time) = #{year})\n" +
            "  and YEAR(cr.create_time) = #{year};")
    ConsumeDTO classCostYear(Integer teacherId, Integer year);
}
