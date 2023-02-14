package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.ClassRecordDao;
import com.kclm.xsap.entity.ClassRecordEntity;
import com.kclm.xsap.service.ClassRecordService;
import org.springframework.stereotype.Service;

@Service
public class ClassRecordServiceImpl extends ServiceImpl<ClassRecordDao, ClassRecordEntity> implements ClassRecordService {
}
