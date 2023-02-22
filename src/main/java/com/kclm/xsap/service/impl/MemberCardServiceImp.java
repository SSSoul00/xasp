package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.MemberCardDao;
import com.kclm.xsap.entity.MemberCardEntity;
import com.kclm.xsap.service.MemberCardService;
import com.kclm.xsap.vo.indexStatistics.IndexPieChartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberCardServiceImp extends ServiceImpl<MemberCardDao, MemberCardEntity> implements MemberCardService {
    @Autowired
    private MemberCardDao memberCardDao;
    @Override
    public List<IndexPieChartVo> selectMemberCardCount() {
        return memberCardDao.selectMemberCardCount();
    }
}
