package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.entity.ReservationRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface ReservationRecordDao extends BaseMapper<ReservationRecordEntity> {
    @Select("select *\n" +
            "from xsap_dev7.t_reservation_record\n" +
            "where DATE(create_time) between #{startDate} and #{endDate}\n" +
            "group by member_id;")
    List<Integer> activeMembers(LocalDate startDate,LocalDate endDate);

    @Select("select count(*)\n" +
            "from xsap_dev7.t_reservation_record\n" +
            "where DATE(create_time) between #{startDate} and #{endDate};")
    Integer totalReservations(LocalDate startDate,LocalDate endDate);
}
