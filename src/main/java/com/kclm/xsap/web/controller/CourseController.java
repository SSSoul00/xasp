package com.kclm.xsap.web.controller;

import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.service.CourseService;
import com.kclm.xsap.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("/courseList.do")
    @ResponseBody
    public List getCourseList() {
        List<CourseEntity> list = courseService.list();
        return list;
    }


}
