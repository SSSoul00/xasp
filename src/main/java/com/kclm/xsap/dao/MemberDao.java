package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where YEAR(m.create_time) = #{year} and MONTH(m.create_time) = #{month};")
    Integer addCountMonth(Integer month, Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where YEAR(m.create_time) = #{year} and MONTH(m.last_modify_time) = #{month} and is_deleted = 1;")
    Integer streamCountMonth(Integer month, Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where YEAR(m.create_time) = #{year} and QUARTER(m.create_time) = #{quarter};")
    Integer addCountQuarter(Integer quarter, Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where YEAR(m.create_time) = #{year} and QUARTER(m.last_modify_time) = #{quarter} and is_deleted = 1;")
    Integer streamCountQuarter(Integer quarter, Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where YEAR(m.create_time) = #{year} ;")
    Integer addCountYear(Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where YEAR(m.last_modify_time) = #{year} and is_deleted = 1;")
    Integer streamCountYear(Integer year);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where DATE(m.create_time) = #{date};")
    Integer addCountDay(LocalDate date);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member as m\n" +
            "where DATE(m.last_modify_time) = #{date}\n" +
            "  and is_deleted = 1;")
    Integer streamCountDay(LocalDate date);

    @Select("select * from xsap_dev7.t_member\n" +
            "where id = #{id};")
    MemberEntity selectById(Long id);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_member m\n" +
            "where is_deleted = 0;")
    Integer totalMembers();
}
