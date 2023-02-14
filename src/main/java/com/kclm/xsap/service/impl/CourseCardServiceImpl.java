package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.CourseCardDao;
import com.kclm.xsap.entity.CourseCardEntity;
import com.kclm.xsap.service.CourseCardService;
import org.springframework.stereotype.Service;

@Service
public class CourseCardServiceImpl extends ServiceImpl<CourseCardDao, CourseCardEntity> implements CourseCardService {
}
