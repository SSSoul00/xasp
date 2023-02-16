package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.MemberBindRecordDao;
import com.kclm.xsap.entity.MemberBindRecordEntity;
import com.kclm.xsap.service.MemberBindRecordService;
import org.springframework.stereotype.Service;

@Service
public class MemberBindRecordServiceImpl extends ServiceImpl<MemberBindRecordDao, MemberBindRecordEntity> implements MemberBindRecordService {
}
