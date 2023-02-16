package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.CourseCardEntity;
import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.service.CourseCardService;
import com.kclm.xsap.service.CourseService;
import com.kclm.xsap.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseCardService courseCardService;

    @RequestMapping("/courseList.do")
    @ResponseBody
    public List getCourseList() {
        List<CourseEntity> list = courseService.list();
        return list;
    }

    @RequestMapping("/x_course_list.do")
    public String toCourseList() {
        return "course/x_course_list";
    }

    @RequestMapping("/x_course_list_add.do")
    public String toCourseListAdd() {
        return "course/x_course_list_add";
    }

    @RequestMapping("/courseAdd.do")
    @ResponseBody
    public R courseAdd(CourseEntity courseEntity, @RequestParam("cardListStr") List<Long> cardList, Integer limitAgeRadio, Integer limitCountsRadio) {
        System.out.println(courseEntity);
        //添加课程
        courseEntity.setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now())
                .setLimitAge(limitAgeRadio == -1 ? -1 : courseEntity.getLimitAge()).setLimitCounts(limitCountsRadio == -1 ? -1 : courseEntity.getLimitCounts());
        courseService.save(courseEntity);
        //绑定会员卡
        List<CourseCardEntity> courseCardEntities = cardList.stream().map(item -> {
            CourseCardEntity courseCardEntity = new CourseCardEntity().setCourseId(courseEntity.getId()).setCardId(item);
            return courseCardEntity;
        }).collect(Collectors.toList());
        courseCardService.saveBatch(courseCardEntities);
        return R.ok("添加课程成功");
    }

    @RequestMapping("x_course_list_edit.do")
    public String toCourseListEdit(Long id, Model model) {
        CourseEntity course = courseService.getById(id);
        model.addAttribute("courseInfo", course);
        List<Long> list = courseCardService.list(new LambdaQueryWrapper<CourseCardEntity>().eq(id != null, CourseCardEntity::getCourseId, id))
                .stream().map(item -> {
                    return item.getCardId();
                }).collect(Collectors.toList());
        model.addAttribute("cardCarry", list);
        return "course/x_course_list_edit";
    }

    @RequestMapping("courseEdit.do")
    @ResponseBody
    public R courseEdit(CourseEntity courseEntity, @RequestParam("cardListStr") List<Long> cardList, Integer limitAgeRadio, Integer limitCountsRadio) {
        courseEntity.setLastModifyTime(LocalDateTime.now()).setLimitAge(limitAgeRadio == -1 ? -1 : courseEntity.getLimitAge())
                .setLimitCounts(limitCountsRadio == -1 ? -1 : courseEntity.getLimitCounts());
        courseService.updateById(courseEntity);
        courseCardService.remove(new LambdaQueryWrapper<CourseCardEntity>().eq(courseEntity != null, CourseCardEntity::getCourseId, courseEntity.getId()));
        List<CourseCardEntity> courseCardEntities = cardList.stream().map(item -> {
            CourseCardEntity courseCardEntity = new CourseCardEntity().setCourseId(courseEntity.getId()).setCardId(item);
            return courseCardEntity;
        }).collect(Collectors.toList());
        courseCardService.saveBatch(courseCardEntities);
        return R.ok("更新成功");
    }

    @RequestMapping("deleteOne.do")
    @ResponseBody
    public R deleteOne() {

        return null;
    }
}
