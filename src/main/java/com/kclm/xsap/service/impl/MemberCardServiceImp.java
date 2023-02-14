package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.MemberCardDao;
import com.kclm.xsap.entity.MemberCardEntity;
import com.kclm.xsap.service.MemberCardService;
import org.springframework.stereotype.Service;

@Service
public class MemberCardServiceImp extends ServiceImpl<MemberCardDao, MemberCardEntity> implements MemberCardService {
}
