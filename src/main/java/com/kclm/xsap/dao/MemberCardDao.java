package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.entity.MemberCardEntity;
import com.kclm.xsap.vo.indexStatistics.IndexPieChartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberCardDao extends BaseMapper<MemberCardEntity> {
    @Select("select count(*) as value,name\n" +
            "from xsap_dev7.t_member_bind_record mbr\n" +
            "left join xsap_dev7.t_member_card tmc on mbr.card_id = tmc.id where active_status = 1\n" +
            "group by card_id;\n")
    List<IndexPieChartVo> selectMemberCardCount();
}
