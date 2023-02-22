package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.*;
import com.kclm.xsap.service.*;
import com.kclm.xsap.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
    @Autowired
    private ClassRecordService classRecordService;
    @Autowired
    private MemberBindRecordService memberBindRecordService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private ScheduleRecordService scheduleRecordService;
    @Autowired
    private ReservationRecordService reservationRecordService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseCardService courseCardService;
    @Autowired
    private GlobalReservationSetService globalReservationSetService;

    @RequestMapping("/getReserveId.do")
    @ResponseBody
    public R getReserveId(Long memberId,Long scheduleId){
        ReservationRecordEntity reservationRecord = reservationRecordService.getOne(new LambdaQueryWrapper<ReservationRecordEntity>().eq(ReservationRecordEntity::getMemberId, memberId)
                .eq(ReservationRecordEntity::getScheduleId, scheduleId));
        return R.ok().setData(reservationRecord.getId());
    }

    @RequestMapping("/cancelReserve.do")
    @ResponseBody
    public R cancelReserve(Long reserveId) {
        //获取全局预约设置
        GlobalReservationSetEntity globalReservationSet = globalReservationSetService.getById(1);
        //获取取消预约模式
        Integer appointmentCancelMode = globalReservationSet.getAppointmentCancelMode();
        ReservationRecordEntity reservationRecord = reservationRecordService.getById(reserveId);
        ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(reservationRecord.getScheduleId());
        if (LocalDateTime.now().isAfter(LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime()))) {
            return R.error("课程已经开始，无法取消！");
        }
        switch (appointmentCancelMode) {
            case 1:
                //无限制取消
                //修改预约数据
                reservationRecord.setStatus(0).setLastModifyTime(LocalDateTime.now()).setVersion(reservationRecord.getVersion() + 1)
                        .setCancelTimes(reservationRecord.getCancelTimes() + 1);
                reservationRecordService.updateById(reservationRecord);
                //删除未确认的上课数据
                classRecordService.remove(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getMemberId, reservationRecord.getMemberId())
                        .eq(ClassRecordEntity::getScheduleId, reservationRecord.getScheduleId()));
                //排课人数减少
                scheduleRecord.setOrderNums(scheduleRecord.getOrderNums() - reservationRecord.getReserveNums());
                scheduleRecordService.updateById(scheduleRecord);
                return R.ok("取消成功！");
            case 2:
                //上课前XX小时之前可以取消
                Integer cancelHour = globalReservationSet.getCancelHour();
                LocalDateTime startTime = LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime());
                LocalDateTime cancelEndTime = startTime.minusHours(cancelHour);
                if (LocalDateTime.now().isBefore(cancelEndTime)) {
                    //修改预约数据
                    reservationRecord.setStatus(0).setLastModifyTime(LocalDateTime.now()).setVersion(reservationRecord.getVersion() + 1)
                            .setCancelTimes(reservationRecord.getCancelTimes() + 1);
                    reservationRecordService.updateById(reservationRecord);
                    //删除未确认的上课数据
                    classRecordService.remove(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getMemberId, reservationRecord.getMemberId())
                            .eq(ClassRecordEntity::getScheduleId, reservationRecord.getScheduleId()));
                    //排课人数减少
                    scheduleRecord.setOrderNums(scheduleRecord.getOrderNums() - reservationRecord.getReserveNums());
                    scheduleRecordService.updateById(scheduleRecord);
                    return R.ok("取消成功！");
                }
                return R.error("已经超过取消预约截至时间，无法取消！");
            case 3:
                //上课前XX天的XX点之前可以取消
                LocalDate cancelEndDate = scheduleRecord.getStartDate().minusDays(globalReservationSet.getCancelDay());
                LocalDateTime cancelEndTime2 = LocalDateTime.of(cancelEndDate, globalReservationSet.getCancelTime());
                if (LocalDateTime.now().isBefore(cancelEndTime2)) {
                    reservationRecord.setStatus(0).setLastModifyTime(LocalDateTime.now()).setVersion(reservationRecord.getVersion() + 1)
                            .setCancelTimes(reservationRecord.getCancelTimes() + 1);
                    reservationRecordService.updateById(reservationRecord);
                    classRecordService.remove(new LambdaQueryWrapper<ClassRecordEntity>().eq(ClassRecordEntity::getMemberId, reservationRecord.getMemberId())
                            .eq(ClassRecordEntity::getScheduleId, reservationRecord.getScheduleId()));
                    //排课人数减少
                    scheduleRecord.setOrderNums(scheduleRecord.getOrderNums() - reservationRecord.getReserveNums());
                    scheduleRecordService.updateById(scheduleRecord);
                    return R.ok("取消成功！");
                }
                return R.error("已经超过取消预约截至时间，无法取消！");
        }
        return R.error("出现错误");
    }

    @RequestMapping("/addReserve.do")
    @ResponseBody
    public R addReserve(ReservationRecordEntity reservationRecord) {
        //判断课程是否开始，开始无法预约
        ScheduleRecordEntity scheduleRecord = scheduleRecordService.getById(reservationRecord.getScheduleId());
        if (LocalDateTime.now().isAfter(LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime()))) {
            return R.error("该课程已经开始或结束，无法预约！");
        }
        //判断该会员是否已经预约该节排课
        ReservationRecordEntity hasReserva = reservationRecordService.getOne(new LambdaQueryWrapper<ReservationRecordEntity>().eq(ReservationRecordEntity::getMemberId, reservationRecord.getMemberId())
                .eq(ReservationRecordEntity::getScheduleId, reservationRecord.getScheduleId()).eq(ReservationRecordEntity::getStatus,1));
        if (hasReserva!=null){
            return R.error("您已预约过该课程，请勿重复预约");
        }
        //获取全局预约设置
        GlobalReservationSetEntity globalReservationSet = globalReservationSetService.getById(1);
        //获取预约开始时间模式和预约截止时间模式
        Integer appointmentStartMode = globalReservationSet.getAppointmentStartMode();
        Integer appointmentDeadlineMode = globalReservationSet.getAppointmentDeadlineMode();
        LocalDate startDate = null;
        LocalDateTime endTime = null;
        if (appointmentStartMode == 2) {
            startDate = scheduleRecord.getStartDate().minusDays(globalReservationSet.getStartDay());
        }
        switch (appointmentDeadlineMode) {
            case 1:
                break;
            case 2:
                endTime = LocalDateTime.of(scheduleRecord.getStartDate(), scheduleRecord.getClassTime()).minusHours(globalReservationSet.getEndHour());
                break;
            case 3:
                endTime = LocalDateTime.of(scheduleRecord.getStartDate().minusDays(globalReservationSet.getEndDay()), globalReservationSet.getEndTime());
        }
        if (startDate != null) {
            if (LocalDate.now().isBefore(startDate)) {
                return R.error("预约还未开始！");
            }
        }
        if (endTime != null) {
            if (LocalDateTime.now().isAfter(endTime)) {
                return R.error("预约已经截止！");
            }
        }
        //判断该会员卡是否支持预约此课程
        CourseEntity course = courseService.getById(scheduleRecord.getCourseId());
        MemberBindRecordEntity memberBindRecord = memberBindRecordService.getById(reservationRecord.getCardId());
        Long cardId = memberBindRecord.getCardId();
        List<Long> cardIds = courseCardService.list(new LambdaQueryWrapper<CourseCardEntity>().eq(CourseCardEntity::getCourseId, course.getId()))
                .stream().map(item -> item.getCardId()).collect(Collectors.toList());
        if (!cardIds.contains(cardId)) {
            return R.error("该会员卡不支持此课程");
        }
        //判断会员卡次数是否足购
        Integer timesCost = reservationRecord.getReserveNums() * course.getTimesCost();
        if (memberBindRecord.getValidCount() < timesCost) {
            return R.error("该会员卡没有足购次数");
        }
        //判断排课人数是否已满
        Integer orderNums = scheduleRecord.getOrderNums();
        if (orderNums + reservationRecord.getReserveNums() > course.getContains()) {
            return R.error("预约人数超过课堂人数上限");
        }
        //判断之前是否有预约过这节排课
        ReservationRecordEntity one = reservationRecordService.getOne(new LambdaQueryWrapper<ReservationRecordEntity>()
                .eq(ReservationRecordEntity::getMemberId, reservationRecord.getMemberId()).eq(ReservationRecordEntity::getScheduleId, reservationRecord.getScheduleId()));

        //预约过，查看预约次数是否超过3次
        if (one != null) {
            //超过3次，拒绝预约
            if (one.getCancelTimes() >= 3) {
                return R.error("您的预约次数超过3次");
            }
            //不足3次，更新预约
            //判断当前时间是否允许预约
            reservationRecord.setLastModifyTime(LocalDateTime.now()).setStatus(1).setId(one.getId()).setVersion(one.getVersion() + 1);
            reservationRecordService.updateById(reservationRecord);
            //添加未确认的上课数据
            ClassRecordEntity classRecord = new ClassRecordEntity().setCreateTime(LocalDateTime.now()).setScheduleId(reservationRecord.getScheduleId())
                    .setReserveCheck(1).setNote("正常预约客户").setMemberId(reservationRecord.getMemberId()).setCardName(reservationRecord.getCardName())
                    .setBindCardId(reservationRecord.getCardId());
            classRecordService.save(classRecord);
            //排课人数增加
            scheduleRecord.setOrderNums(scheduleRecord.getOrderNums() + reservationRecord.getReserveNums());
            scheduleRecordService.updateById(scheduleRecord);
            return R.ok("预约成功！");
        }

        //没预约过，添加新的预约
        reservationRecordService.save(reservationRecord.setCreateTime(LocalDateTime.now()).setStatus(1));
        //添加未确认的上课数据
        ClassRecordEntity classRecord = new ClassRecordEntity().setCreateTime(LocalDateTime.now()).setScheduleId(reservationRecord.getScheduleId())
                .setReserveCheck(1).setNote("正常预约客户").setMemberId(reservationRecord.getMemberId()).setCardName(reservationRecord.getCardName())
                .setBindCardId(reservationRecord.getCardId());
        classRecordService.save(classRecord);
        //排课人数增加
        scheduleRecord.setOrderNums(scheduleRecord.getOrderNums() + reservationRecord.getReserveNums());
        scheduleRecordService.updateById(scheduleRecord);
        return R.ok("预约成功！");
    }
}
