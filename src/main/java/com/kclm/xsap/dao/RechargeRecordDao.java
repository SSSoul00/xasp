package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.entity.RechargeRecordEntity;
import com.kclm.xsap.vo.indexStatistics.IndexAddAndStreamInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Mapper
public interface RechargeRecordDao extends BaseMapper<RechargeRecordEntity> {
    @Select("SELECT DATE_FORMAT(RR.create_time,'%Y') as year\n" +
            "FROM xsap_dev7.t_recharge_record AS RR\n" +
            "GROUP BY year;")
    List<Year> selectAllYear();

    @Select("SELECT IFNULL(SUM(RR.received_money),0) as data\n" +
            "FROM xsap_dev7.t_recharge_record AS RR\n" +
            "where month(RR.create_time) = #{month}\n" +
            "  and YEAR(RR.create_time) = #{year};")
    Integer selectMoneyByMonth(Integer month, Integer year);

    @Select("SELECT IFNULL(SUM(RR.received_money),0) as data\n" +
            "FROM xsap_dev7.t_recharge_record AS RR\n" +
            "where quarter(RR.create_time) = #{quarter}\n" +
            "  and YEAR(RR.create_time) = #{year};")
    Integer selectMoneyByQuarter(Integer quarter, Integer year);

    @Select("SELECT IFNULL(SUM(RR.received_money), 0) as data\n" +
            "FROM xsap_dev7.t_recharge_record AS RR\n" +
            "where YEAR(RR.create_time) = #{year}")
    Integer selectMoneyByYear(Integer year);

    @Select("select IFNULL(SUM(received_money),0)\n" +
            "from xsap_dev7.t_recharge_record rr\n" +
            "where DATE(create_time) = #{date};")
    Integer selectMoneyByDay(LocalDate date);
}
