package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.CourseCardEntity;
import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.entity.ScheduleRecordEntity;
import com.kclm.xsap.service.CourseCardService;
import com.kclm.xsap.service.CourseService;
import com.kclm.xsap.service.ScheduleRecordService;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.CourseLimitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    private ScheduleRecordService scheduleRecordService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseCardService courseCardService;

    @RequestMapping("/getOneCourse.do")
    @ResponseBody
    public R getOneCourse(Long id){
        CourseEntity course = courseService.getById(id);
        CourseLimitVo courseLimitVo = new CourseLimitVo().setLimitSex(course.getLimitSex()).setLimitAge(course.getLimitAge()).setDuration(course.getDuration());
        return R.ok().setData(courseLimitVo);
    }

    @RequestMapping("/toSearch.do")
    @ResponseBody
    public R toSearch(){
        List<CourseEntity> courseEntities = courseService.list();
        return R.ok().put("value",courseEntities);
    }

    @RequestMapping("/courseList.do")
    @ResponseBody
    public List getCourseList() {
        List<CourseEntity> list = courseService.list(new LambdaQueryWrapper<CourseEntity>().orderByDesc(CourseEntity::getCreateTime));
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
    @Transactional
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
    public R deleteOne(Long id) {
        List<ScheduleRecordEntity> scheduleRecord = scheduleRecordService.list(new LambdaQueryWrapper<ScheduleRecordEntity>().eq(id != null, ScheduleRecordEntity::getCourseId, id));
        if (scheduleRecord.size() > 0) {
            return R.error(500, "删除失败!此课程已经生成了预约、上课、排课等记录，不可被删除！");
        }
        //删除课程相关绑定卡记录
        courseCardService.remove(new LambdaQueryWrapper<CourseCardEntity>().eq(CourseCardEntity::getCourseId,id));
        //删除课程
        courseService.removeById(id);
        return R.ok("删除成功");
    }
}
