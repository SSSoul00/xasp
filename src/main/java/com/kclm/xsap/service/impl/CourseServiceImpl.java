package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.CourseDao;
import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity>implements CourseService {
}
