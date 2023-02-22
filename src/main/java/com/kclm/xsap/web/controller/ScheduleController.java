package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.consts.OperateType;
import com.kclm.xsap.dto.convert.ReserveVoConvert;
import com.kclm.xsap.entity.*;
import com.kclm.xsap.service.*;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private MemberBindRecordService memberBindRecordService;
    @Value("${reservation.gap_minute}")
    private Integer RESERVATION_GAP_MINUTE;
    @Autowired
    private ConsumeRecordService consumeRecordService;
    @Autowired
    private ClassRecordService classRecordService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ReserveVoConvert reserveVoConvert;
    @Autowired
    private ReservationRecordService reservationRecordService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private CourseCardService courseCardService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ScheduleRecordService scheduleRecordService;
    @Autowired
    private MemberLogService memberLogService;

    @RequestMapping("/toSearch.do")
    @ResponseBody
    public R toSearch() {
        LocalDate endTime = LocalDate.now();
        LocalDate startTime = endTime.minusMonths(1);
        List<ScheduleRecordEntity> scheduleRecordEntities = scheduleRecordService.list(new LambdaQueryWrapper<ScheduleRecordEntity>()
                .between(ScheduleRecordEntity::getStartDate, startTime, endTime));
        List<ScheduleForConsumeSearchVo> scheduleForConsumeSearchVos = scheduleRecordEntities.stream().map(item -> {
            String courseName = courseService.getById(item.getCourseId()).getName();
            String teacherName = employeeService.getById(item.getTeacherId()).getName();
            ScheduleForConsumeSearchVo scheduleForConsumeSearchVo = new ScheduleForConsumeSearchVo();
            scheduleForConsumeSearchVo.setScheduleId(item.getId()).setCourseName(courseName).setTeacherName(teacherName)
                    .setClassDateTime(LocalDateTime.of(item.getStartDate(), item.getClassTime()));
            return scheduleForConsumeSearchVo;
        }).collect(Collectors.toList());
        return R.ok().put("value",scheduleForConsumeSearchVos);
    }

    @RequestMapping("/consumeEnsureAll.do")
    @ResponseBody
    public R consumeEnsureAll(Long scheduleId, String operator) {
        //判断课程是否结束，未结束无法扣费
        ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(scheduleId);
        LocalDateTime startTime = LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime());
        CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
        LocalDateTime endTime = startTime.plusMinutes(course.getDuration());
        if (LocalDateTime.now().isBefore(endTime)) {
            return R.error("课程还未结束，无法扣费！");
        }
        //扣除所有还未扣费的课程
        //获取该排课所有未确认课程记录
        List<ClassRecordEntity> classRecord = classRecordService.list(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getScheduleId, scheduleId).eq(ClassRecordEntity::getCheckStatus, 0));
        for (ClassRecordEntity item : classRecord) {
            //查询应扣金额和应扣次数进行扣除
            MemberBindRecordEntity bindCard = memberBindRecordService.getById(item.getBindCardId());
            BigDecimal receivedMoney = bindCard.getReceivedMoney();
            BigDecimal validCount = new BigDecimal(bindCard.getValidCount().toString());
            BigDecimal amountPayableForOne = receivedMoney.divide(validCount, 2, BigDecimal.ROUND_HALF_UP);
            ReservationRecordEntity reservationRecord = reservationRecordService.getOne(new LambdaQueryWrapper<ReservationRecordEntity>().eq(ReservationRecordEntity::getScheduleId, item.getScheduleId())
                    .eq(ReservationRecordEntity::getMemberId, item.getMemberId()));
            Integer costTimes = reservationRecord.getReserveNums() * course.getTimesCost();
            BigDecimal costTimesBigDecimal = new BigDecimal(costTimes.toString());
            BigDecimal amountPayable = amountPayableForOne.multiply(costTimesBigDecimal);

            bindCard.setValidCount(bindCard.getValidCount() - costTimes)
                    .setReceivedMoney(bindCard.getReceivedMoney().subtract(amountPayable)).setLastModifyTime(LocalDateTime.now())
                    .setVersion(bindCard.getVersion() + 1);
            memberBindRecordService.updateById(bindCard);
            //修改课程记录
            item.setCheckStatus(1).setLastModifyTime(LocalDateTime.now()).setVersion(item.getVersion() + 1);
            classRecordService.updateById(item);
            //添加扣费操作记录
            MemberLogEntity m = new MemberLogEntity().setType(OperateType.CLASS_DEDUCTION_OPERATION.getMsg()).setNote("一键扣费").setOperator(operator)
                    .setCreateTime(LocalDateTime.now()).setMemberBindId(bindCard.getId()).setInvolveMoney(amountPayable).setCardCountChange(costTimes);
            memberLogService.save(m);
            //添加扣费记录
            ConsumeRecordEntity c = new ConsumeRecordEntity().setOperateType(OperateType.CLASS_DEDUCTION_OPERATION.getMsg()).setOperator(operator)
                    .setCreateTime(LocalDateTime.now()).setLogId(m.getId()).setNote("一键扣费").setCardCountChange(costTimes).setMemberBindId(bindCard.getId())
                    .setMoneyCost(amountPayable).setScheduleId(scheduleId);
            consumeRecordService.save(c);
        }
        return R.ok("一键扣费成功！");
    }

    @RequestMapping("/consumeEnsure.do")
    @ResponseBody
    @Transactional
    public R consumeEnsure(ConsumeFormVo consumeFormVo) {
        //判断课程是否结束，未结束无法扣费
        ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(consumeFormVo.getScheduleId());
        LocalDateTime startTime = LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime());
        CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
        LocalDateTime endTime = startTime.plusMinutes(course.getDuration());
        if (LocalDateTime.now().isBefore(endTime)) {
            return R.error("课程还未结束，无法扣费！");
        }
        //扣费
        MemberBindRecordEntity bindCard = memberBindRecordService.getById(consumeFormVo.getCardBindId());
        Integer cardDayChange = consumeFormVo.getCardDayChange();
        bindCard.setValidCount(bindCard.getValidCount() - consumeFormVo.getCardCountChange()).setValidDay(bindCard.getValidDay() - (cardDayChange == null ? 0 : cardDayChange))
                .setReceivedMoney(bindCard.getReceivedMoney().subtract(consumeFormVo.getAmountOfConsumption())).setLastModifyTime(LocalDateTime.now())
                .setVersion(bindCard.getVersion() + 1);
        memberBindRecordService.updateById(bindCard);

        //将上课记录修改，确认已上课
        ClassRecordEntity classRecord = classRecordService.getById(consumeFormVo.getClassId());
        classRecord.setCheckStatus(1).setLastModifyTime(LocalDateTime.now()).setVersion(classRecord.getVersion() + 1);
        classRecordService.updateById(classRecord);
        //添加扣费操作记录
        MemberLogEntity m1 = new MemberLogEntity().setMemberBindId(consumeFormVo.getCardBindId()).setType(OperateType.CLASS_DEDUCTION_OPERATION.getMsg())
                .setOperator(consumeFormVo.getOperator()).setInvolveMoney(consumeFormVo.getAmountOfConsumption()).setCardCountChange(consumeFormVo.getCardCountChange())
                .setCardDayChange(consumeFormVo.getCardDayChange()).setCreateTime(LocalDateTime.now()).setNote("指定扣费");
        memberLogService.save(m1);
        //添加消费记录
        ConsumeRecordEntity c1 = new ConsumeRecordEntity().setCreateTime(LocalDateTime.now()).setLogId(m1.getId()).setCardCountChange(consumeFormVo.getCardCountChange())
                .setCardDayChange(consumeFormVo.getCardDayChange()).setMemberBindId(consumeFormVo.getCardBindId()).setMoneyCost(consumeFormVo.getAmountOfConsumption())
                .setOperateType(OperateType.CLASS_DEDUCTION_OPERATION.getMsg()).setScheduleId(consumeFormVo.getScheduleId()).setNote("指定扣费");
        consumeRecordService.save(c1);
        return R.ok("扣费成功！");
    }

    @RequestMapping("/queryAmountsPayable.do")
    @ResponseBody
    public R queryAmountsPayable(Long bindCardId) {
        MemberBindRecordEntity bindCard = memberBindRecordService.getById(bindCardId);
        BigDecimal receivedMoney = bindCard.getReceivedMoney();
        BigDecimal validCount = new BigDecimal(bindCard.getValidCount().toString());
        BigDecimal amountPayable = receivedMoney.divide(validCount, 2, BigDecimal.ROUND_HALF_UP);
        return R.ok().setData(amountPayable);
    }

    @RequestMapping("/deleteOne.do")
    @ResponseBody
    public R deleteOne(Long id) {
        //查找该排课是否被预约，是否有过上课记录
        List<ReservationRecordEntity> reservationRecord = reservationRecordService.list(new LambdaQueryWrapper<ReservationRecordEntity>().eq(ReservationRecordEntity::getScheduleId, id)
                .eq(ReservationRecordEntity::getStatus, 1));
        List<ClassRecordEntity> classRecords = classRecordService.list(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getScheduleId, id).eq(ClassRecordEntity::getCheckStatus, 1));
        if (reservationRecord.size() > 0 || classRecords.size() > 0) {
            return R.error("该排课存在预约或上课记录，无法删除");
        }

        scheduleRecordService.removeById(id);
        return R.ok("删除成功");
    }

    @RequestMapping("/scheduleCopy.do")
    @ResponseBody
    public R scheduleCopy(String sourceDateStr, String targetDateStr) {
        LocalDate sourceDate = LocalDate.parse(sourceDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate targetDate = LocalDate.parse(targetDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<ScheduleRecordEntity> scheduleRecords = scheduleRecordService.list(new LambdaQueryWrapper<ScheduleRecordEntity>().eq(ScheduleRecordEntity::getStartDate, sourceDate));
        if (scheduleRecords.size() == 0) {
            return R.error("源日期没有课程");
        }
        List<ScheduleRecordEntity> scheduleRecordEntities = scheduleRecords.stream().map(item -> {
            ScheduleRecordEntity scheduleRecord = new ScheduleRecordEntity().setCreateTime(LocalDateTime.now()).setTeacherId(item.getTeacherId())
                    .setStartDate(targetDate).setCourseId(item.getCourseId()).setClassTime(item.getClassTime()).setLimitAge(item.getLimitAge())
                    .setLimitSex(item.getLimitSex());
            return scheduleRecord;
        }).collect(Collectors.toList());
        scheduleRecordService.saveBatch(scheduleRecordEntities);
        return R.ok("复制课表成功");
    }

    @RequestMapping("/scheduleAdd.do")
    @ResponseBody
    public R scheduleAdd(ScheduleRecordEntity scheduleRecord) {
        //判断该老师当天是否有排课冲突,冲突则提示
        LocalDateTime startTime = LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime());
        LocalDateTime endTime = startTime.plusMinutes(scheduleRecord.getCourseDuration());
        List<ScheduleRecordEntity> scheduleRecords = scheduleRecordService.list(new LambdaQueryWrapper<ScheduleRecordEntity>().eq(ScheduleRecordEntity::getTeacherId, scheduleRecord.getTeacherId())
                .eq(ScheduleRecordEntity::getStartDate, scheduleRecord.getStartDate()));
        for (ScheduleRecordEntity record : scheduleRecords) {
            Long duration2 = courseService.getById(record.getCourseId()).getDuration();
            LocalDateTime startTime2 = LocalDateTime.of(record.getStartDate(), record.getClassTime());
            LocalDateTime endTime2 = startTime2.plusMinutes(duration2);
            if (startTime.isEqual(startTime2) || startTime.isEqual(endTime2) || endTime.equals(startTime2)) {
                return R.error("排课时间冲突");
            }
            if (startTime.isAfter(startTime2)) {
                if (startTime.isBefore(endTime2.plusMinutes(RESERVATION_GAP_MINUTE))) {
                    return R.error("与上一节排课时间冲突");
                }
            } else {
                if (endTime.isAfter(startTime2.minusMinutes(RESERVATION_GAP_MINUTE))) {
                    return R.error("与下一节排课时间冲突");
                }
            }

        }
        //没有冲突
        CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
        scheduleRecord.setCreateTime(LocalDateTime.now()).setLimitAge(course.getLimitAge()).setLimitSex(course.getLimitSex());
        scheduleRecordService.save(scheduleRecord);
        return R.ok("新增排课成功");
    }

    @RequestMapping("/classRecord.do")
    @ResponseBody
    public R classRecord(Long id) {
        ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(id);
        List<ClassRecordEntity> classRecords = classRecordService.list(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getScheduleId, id));
        List<ClassRecordVo> classRecordVos = classRecords.stream().map(item -> {
            MemberEntity member = memberService.getById(item.getMemberId());
            CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
            ClassRecordVo classRecordVo = new ClassRecordVo().setClassRecordId(item.getId()).setStartDate(scheduleRecord.getStartDate())
                    .setTimesCost(course.getTimesCost()).setOperateTime(item.getCreateTime()).setMemberSex(member.getSex())
                    .setMemberPhone(member.getPhone()).setMemberName(member.getName()).setMemberId(item.getMemberId())
                    .setMemberBirthday(member.getBirthday()).setCreateTimes(item.getCreateTime()).setCheckStatus(item.getCheckStatus()).setCardName(item.getCardName())
                    .setCardId(item.getBindCardId()).setClassTime(scheduleRecord.getClassTime()).setStartDate(scheduleRecord.getStartDate());
            if (item.getReserveCheck() == 1) {
                ReservationRecordEntity reservationRecord = reservationRecordService.getOne(new LambdaQueryWrapper<ReservationRecordEntity>()
                        .eq(ReservationRecordEntity::getScheduleId, scheduleRecord.getId()).eq(ReservationRecordEntity::getMemberId, item.getMemberId()));
                classRecordVo.setReserveNums(reservationRecord.getReserveNums());
            } else {
                classRecordVo.setReserveNums(1);
            }
            return classRecordVo;
        }).collect(Collectors.toList());
        return R.ok().setData(classRecordVos);
    }

    @RequestMapping("/reserveRecord.do")
    @ResponseBody
    public R reserveRecord(Long id) {
        List<ReservationRecordEntity> reservationRecord = reservationRecordService.list(new LambdaQueryWrapper<ReservationRecordEntity>().eq(ReservationRecordEntity::getScheduleId, id));
        List<ReserveVo> reserveVos = reservationRecord.stream().map(item -> {
            ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(item.getScheduleId());
            MemberEntity member = memberService.getById(item.getMemberId());
            MemberBindRecordEntity bindCard = memberBindRecordService.getOne(new LambdaQueryWrapper<MemberBindRecordEntity>().eq(MemberBindRecordEntity::getMemberId, item.getMemberId()).eq(MemberBindRecordEntity::getId, item.getCardId()));
            MemberCardEntity memberCard = memberCardService.getById(bindCard.getCardId());
            CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
            ClassRecordEntity classRecord = classRecordService.getOne(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getMemberId, item.getMemberId()).eq(ClassRecordEntity::getScheduleId, item.getScheduleId()).eq(ClassRecordEntity::getReserveCheck,1));
            ReserveVo reserveVo = reserveVoConvert.entity2Vo(item, scheduleRecord, member, memberCard, course, classRecord);
            return reserveVo;
        }).collect(Collectors.toList());
        return R.ok().setData(reserveVos);
    }

    @RequestMapping("/reservedList.do")
    @ResponseBody
    public R reservedList(Long id) {

        List<ReservationRecordEntity> reservationRecord = reservationRecordService.list(new LambdaQueryWrapper<ReservationRecordEntity>()
                .eq(ReservationRecordEntity::getScheduleId, id).eq(ReservationRecordEntity::getStatus, 1));
        List<ReserveVo> reserveVos = reservationRecord.stream().map(item -> {
            ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(item.getScheduleId());
            MemberEntity member = memberService.getById(item.getMemberId());
            MemberBindRecordEntity bindCard = memberBindRecordService.getOne(new LambdaQueryWrapper<MemberBindRecordEntity>().eq(MemberBindRecordEntity::getMemberId, item.getMemberId()).eq(MemberBindRecordEntity::getId, item.getCardId()));
            MemberCardEntity memberCard = memberCardService.getById(bindCard.getCardId());
            CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
            ClassRecordEntity classRecord = classRecordService.getOne(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getMemberId, item.getMemberId()).eq(ClassRecordEntity::getScheduleId, item.getScheduleId()).eq(ClassRecordEntity::getReserveCheck,1));
            ReserveVo reserveVo = reserveVoConvert.entity2Vo(item, scheduleRecord, member, memberCard, course, classRecord);
            return reserveVo;
        }).collect(Collectors.toList());
        return R.ok().setData(reserveVos);

    }

    @RequestMapping("/scheduleDetail.do")
    @ResponseBody
    public R scheduleDetail(Long id) {
        ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(id);
        CourseEntity scheduleCourse = courseService.getById(scheduleRecord.getCourseId());
        String teacherName = employeeService.getTeacherNameById(scheduleRecord.getTeacherId());
        List<CourseCardEntity> courseCardEntities = courseCardService.list(new LambdaQueryWrapper<CourseCardEntity>().eq(CourseCardEntity::getCourseId, scheduleRecord.getCourseId()));
        List<String> cardNames = courseCardEntities.stream().map(item -> {
            MemberCardEntity memberCard = memberCardService.getById(item.getCardId());
            return "[" + memberCard.getName() + "] ";
        }).collect(Collectors.toList());
        String cardName = String.join(" ", cardNames);
        LocalDateTime startTime = LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime());
        LocalDateTime endTime = startTime.plusMinutes(scheduleCourse.getDuration());
        ScheduleDetailsVo scheduleDetailsVo = new ScheduleDetailsVo().setDuration(scheduleCourse.getDuration()).setCourseName(scheduleCourse.getName())
                .setTimesCost(scheduleCourse.getTimesCost()).setTeacherName(teacherName).setSupportCards(cardNames)
                .setStartTime(startTime).setOrderNums(scheduleRecord.getOrderNums())
                .setLimitSex(scheduleCourse.getLimitSex()).setEndTime(endTime).setLimitAge(scheduleCourse.getLimitAge())
                .setClassNumbers(scheduleCourse.getContains());
        return R.ok().setData(scheduleDetailsVo);
    }

    @RequestMapping("/x_course_schedule_detail.do")
    public String toCourseScheduleDetail(Long id, Model model) {
        model.addAttribute("ID", id);
        return "course/x_course_schedule_detail";
    }

    @RequestMapping("/x_course_schedule.do")
    public String toCourseSchedule() {
        return "course/x_course_schedule";
    }

    @RequestMapping("/scheduleList.do")
    @ResponseBody
    public List scheduleList(Long start, Long end) {
        if (start != null && end != null) {
            LocalDate startDate = LocalDateTime.ofEpochSecond(start, 0, ZoneOffset.ofHours(8)).toLocalDate();
            LocalDate endDate = LocalDateTime.ofEpochSecond(end, 0, ZoneOffset.ofHours(8)).toLocalDate();

            List<ScheduleRecordEntity> scheduleRecord = scheduleRecordService.list(new LambdaQueryWrapper<ScheduleRecordEntity>()
                    .gt(ScheduleRecordEntity::getStartDate, startDate).lt(ScheduleRecordEntity::getStartDate, endDate));
            List<ScheduleVo> scheduleVos = scheduleRecord.stream().map(item -> {
                ScheduleVo scheduleVo = new ScheduleVo();
                CourseEntity scheduleCourse = courseService.getById(item.getCourseId());
                String teacherName = employeeService.getTeacherNameById(item.getTeacherId());
//                EmployeeEntity scheduleTeacher = employeeService.getById(item.getTeacherId());
                LocalDateTime startTime = LocalDateTime.of(item.getStartDate(), item.getClassTime());
                LocalDateTime endTime = startTime.plusMinutes(scheduleCourse.getDuration());
                String total = scheduleCourse.getName() + "[" + teacherName + "]";
                scheduleVo.setColor(scheduleCourse.getColor()).setEnd(endTime).setHeight(300).setStart(startTime)
                        .setTitle(total).setUrl("x_course_schedule_detail.do?id=" + item.getId());
                return scheduleVo;
            }).collect(Collectors.toList());
            return scheduleVos;
        }
        List<ScheduleRecordEntity> scheduleRecord = scheduleRecordService.list();
        List<ScheduleVo> scheduleVos = scheduleRecord.stream().map(item -> {
            ScheduleVo scheduleVo = new ScheduleVo();
            CourseEntity scheduleCourse = courseService.getById(item.getCourseId());
            String teacherName = employeeService.getTeacherNameById(item.getTeacherId());
//            EmployeeEntity scheduleTeacher = employeeService.getById(item.getTeacherId());
            LocalDateTime startTime = LocalDateTime.of(item.getStartDate(), item.getClassTime());
            LocalDateTime endTime = startTime.plusMinutes(scheduleCourse.getDuration());
            String total = scheduleCourse.getName() + "[" + teacherName + "]";
            scheduleVo.setColor(scheduleCourse.getColor()).setEnd(endTime).setHeight(300).setStart(startTime)
                    .setTitle(total).setUrl("x_course_schedule_detail.do?id=" + item.getId());
            return scheduleVo;
        }).collect(Collectors.toList());
        return scheduleVos;
    }
}
