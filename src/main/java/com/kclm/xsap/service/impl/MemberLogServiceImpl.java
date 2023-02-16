package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.MemberLogDao;
import com.kclm.xsap.entity.MemberLogEntity;
import com.kclm.xsap.service.MemberLogService;
import org.springframework.stereotype.Service;

@Service
public class MemberLogServiceImpl extends ServiceImpl<MemberLogDao, MemberLogEntity> implements MemberLogService {
}
