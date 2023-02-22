package com.kclm.xsap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kclm.xsap.entity.MemberCardEntity;
import com.kclm.xsap.vo.indexStatistics.IndexPieChartVo;

import java.util.List;

public interface MemberCardService extends IService<MemberCardEntity> {
    List<IndexPieChartVo> selectMemberCardCount();
}
