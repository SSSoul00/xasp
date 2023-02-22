package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.consts.OperateType;
import com.kclm.xsap.dto.convert.OperateRecordVoConvert;
import com.kclm.xsap.entity.*;
import com.kclm.xsap.service.*;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.CardBindVo;
import com.kclm.xsap.vo.CardTipVo;
import com.kclm.xsap.vo.ConsumeFormVo;
import com.kclm.xsap.vo.OperateRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/card")
@Slf4j
public class MemberCardController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private OperateRecordVoConvert operateRecordVoConvert;
    @Autowired
    private ConsumeRecordService consumeRecordService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private MemberLogService memberLogService;
    @Autowired
    private MemberBindRecordService memberBindRecordService;
    @Autowired
    private CourseCardService courseCardService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private ScheduleRecordService scheduleRecordService;
    @Autowired
    private ClassRecordService classRecordService;
    @RequestMapping("/consumeOpt.do")
    @ResponseBody
    public R consumeOpt(ConsumeFormVo consumeFormVo) {
        //扣费
        MemberBindRecordEntity bindCard = memberBindRecordService.getById(consumeFormVo.getCardBindId());
        bindCard.setValidCount(bindCard.getValidCount() - consumeFormVo.getCardCountChange()).setReceivedMoney(bindCard.getReceivedMoney().subtract(consumeFormVo.getAmountOfConsumption()))
                .setLastModifyTime(LocalDateTime.now()).setVersion(bindCard.getVersion() + 1);
        memberBindRecordService.updateById(bindCard);
        //添加未预约上课记录
        String cardName = memberCardService.getById(bindCard.getCardId()).getName();
        ClassRecordEntity classRecord = new ClassRecordEntity().setMemberId(consumeFormVo.getMemberId()).setCardName(cardName).setScheduleId(consumeFormVo.getScheduleId())
                .setNote(consumeFormVo.getNote()).setCheckStatus(1).setReserveCheck(0).setCreateTime(LocalDateTime.now()).setBindCardId(consumeFormVo.getCardBindId());
        classRecordService.save(classRecord);
        //添加消费操作记录
        MemberLogEntity memberLog = new MemberLogEntity().setType(OperateType.CLASS_DEDUCTION_OPERATION.getMsg()).setInvolveMoney(consumeFormVo.getAmountOfConsumption())
                .setOperator(consumeFormVo.getOperator()).setMemberBindId(consumeFormVo.getCardBindId()).setCreateTime(LocalDateTime.now()).setCardCountChange(consumeFormVo.getCardCountChange())
                .setNote("无预约扣费");
        memberLogService.save(memberLog);
        //添加消费记录
        ConsumeRecordEntity consumeRecord = new ConsumeRecordEntity().setOperateType(OperateType.CLASS_DEDUCTION_OPERATION.getMsg()).setCardCountChange(consumeFormVo.getCardCountChange())
                .setMoneyCost(consumeFormVo.getAmountOfConsumption()).setOperator(consumeFormVo.getOperator()).setNote("无预约扣费").setMemberBindId(consumeFormVo.getCardBindId())
                .setCreateTime(LocalDateTime.now()).setLogId(memberLog.getId()).setScheduleId(consumeFormVo.getScheduleId());
        consumeRecordService.save(consumeRecord);
        return R.ok("扣费成功");
    }

    @RequestMapping("/cardTip.do")
    @ResponseBody
    public R cardTip(Long cardId, Long scheduleId) {
        MemberBindRecordEntity memberBindRecord = memberBindRecordService.getById(cardId);
        ScheduleRecordEntity scheduleRecordServiceById = scheduleRecordService.getById(scheduleId);
        CourseEntity course = courseService.getById(scheduleRecordServiceById.getCourseId());
        CardTipVo cardTipVo = new CardTipVo().setCardTotalCount(memberBindRecord.getValidCount()).setCourseTimesCost(course.getTimesCost());
        return R.ok().setData(cardTipVo);
    }

    @RequestMapping("/toSearchByMemberId.do")
    @ResponseBody
    public R searchByMemberId(Long memberId) {
        List<MemberBindRecordEntity> memberBindRecords = memberBindRecordService.list(new LambdaQueryWrapper<MemberBindRecordEntity>().eq(MemberBindRecordEntity::getMemberId, memberId)
                .eq(MemberBindRecordEntity::getActiveStatus, 1));
        List<CardBindVo> cardBindVos = memberBindRecords.stream().map(item -> {
            MemberCardEntity card = memberCardService.getById(item.getCardId());
            return new CardBindVo().setName(card.getName()).setId(item.getId());
        }).collect(Collectors.toList());
        return R.ok().put("value", cardBindVos);
    }

    @RequestMapping("/x_member_card.do")
    public String toMemberCard() {
        return "/member/x_member_card";
    }

    @RequestMapping("/cardList.do")
    @ResponseBody
    public R getCardList() {
        List<MemberCardEntity> cardList = memberCardService.list();
        R r = new R();
        r.setData(cardList);
        return r;
    }

    @RequestMapping("/x_member_add_card.do")
    public String toMemberAddCard() {
        return "/member/x_member_add_card";
    }

    @RequestMapping("/cardAdd.do")
    @ResponseBody
    @Transactional
    public R cardAdd(MemberCardEntity card, @RequestParam("courseListStr") List<Long> ids) {
        log.info("接收数据：{},{}", card, ids.toArray());
        boolean save = memberCardService.save(card.setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now()));
        List<CourseCardEntity> courseCardEntities = ids.stream().map(item -> {
            return new CourseCardEntity().setCardId(card.getId()).setCourseId(item);
        }).collect(Collectors.toList());
        boolean b = courseCardService.saveBatch(courseCardEntities);
        return R.ok("添加成功").setData(card);
    }

    @RequestMapping("/x_member_card_edit.do")
    public String toMemberCardEdit(Integer id, Model model) {
        MemberCardEntity one = memberCardService.getOne(new LambdaQueryWrapper<MemberCardEntity>().eq(id != null, MemberCardEntity::getId, id));
        model.addAttribute("cardMsg", one);
        List<Long> courseIdList = courseCardService.list(new LambdaQueryWrapper<CourseCardEntity>().eq(id != null, CourseCardEntity::getCardId, id)).stream().map(item -> {
            return item.getCourseId();
        }).collect(Collectors.toList());
        model.addAttribute("courseCarry", courseIdList);
        return "/member/x_member_card_edit";
    }

    @RequestMapping("/cardEdit.do")
    @ResponseBody
    @Transactional
    public R cardEdit(MemberCardEntity card, @RequestParam("courseListStr") List<Long> ids) {
        memberCardService.updateById(card.setLastModifyTime(LocalDateTime.now()));
        courseCardService.removeById(card.getId());
        List<CourseCardEntity> courseCardEntities = ids.stream().map(item -> {
            return new CourseCardEntity().setCardId(card.getId()).setCourseId(item);
        }).collect(Collectors.toList());
        boolean b = courseCardService.saveBatch(courseCardEntities);
        return R.ok("更新成功");
    }

    @RequestMapping("/deleteOne.do")
    @ResponseBody
    @Transactional
    public R deleteOne(Long id) {
        courseCardService.removeById(id);
        memberCardService.removeById(id);
        return R.ok("删除成功");
    }


    //未完成
    @RequestMapping("/operateRecord.do")
    @ResponseBody
    public R operateRecord(Long memberId, Long cardId) {
        MemberBindRecordEntity one = memberBindRecordService.getOne(new LambdaQueryWrapper<MemberBindRecordEntity>()
                .eq(memberId != null, MemberBindRecordEntity::getMemberId, memberId)
                .eq(cardId != null, MemberBindRecordEntity::getId, cardId));
        List<MemberLogEntity> list = memberLogService.list(new LambdaQueryWrapper<MemberLogEntity>()
                .eq(one != null, MemberLogEntity::getMemberBindId, one.getId()));
        List<OperateRecordVo> operateRecordVos = new ArrayList<>();
        list.forEach(item -> {
            if (item.getType().contains("充值") || item.getType().contains("绑卡")) {
                RechargeRecordEntity rechargeRecord = rechargeRecordService.getOne(new LambdaQueryWrapper<RechargeRecordEntity>()
                        .eq(RechargeRecordEntity::getLogId, item.getId()));
                OperateRecordVo operateRecordVo = operateRecordVoConvert.rechargeEntity2Vo(item, rechargeRecord, "￥" + rechargeRecord.getReceivedMoney());
//                OperateRecordVo operateRecordVo = new OperateRecordVo().setId(item.getId())
//                        .setOperateType(item.getType()).setOperateTime(item.getCreateTime())
//                        .setAddCount(rechargeRecord.getAddCount()).setChangeCount(rechargeRecord.getAddCount())
//                        .setReceivedMoney(rechargeRecord.getReceivedMoney())
//                        .setOperator(rechargeRecord.getOperator()).setCardNote(item.getNote())
//                        .setCreateTime(item.getCreateTime()).setLastModifyTime(item.getLastModifyTime())
//                        .setStatus(item.getCardActiveStatus()).setChangeMoney("￥" + rechargeRecord.getReceivedMoney());
                operateRecordVos.add(operateRecordVo);
            } else if (item.getType().contains("扣费")) {
                ConsumeRecordEntity consumeRecord = consumeRecordService.getOne(new LambdaQueryWrapper<ConsumeRecordEntity>()
                        .eq(ConsumeRecordEntity::getLogId, item.getId()));
                OperateRecordVo operateRecordVo = operateRecordVoConvert.consumerEntity2Vo(item, consumeRecord, "-￥" + consumeRecord.getMoneyCost());
                Integer changeCount = operateRecordVo.getChangeCount();
                operateRecordVo.setChangeCount(changeCount - (changeCount + changeCount));
                operateRecordVos.add(operateRecordVo);
            } else {
                OperateRecordVo operateRecordVo = new OperateRecordVo().setId(item.getId()).setOperateTime(item.getCreateTime())
                        .setOperateType(item.getType()).setChangeCount(0).setChangeMoney("￥" + 0).setOperator(item.getOperator())
                        .setStatus(item.getCardActiveStatus());
                operateRecordVos.add(operateRecordVo);
            }

        });
        return R.ok().setData(operateRecordVos);
    }

    @RequestMapping("/rechargeOpt.do")
    @ResponseBody
    public R rechargeOpt(RechargeRecordEntity rechargeRecord, Long memberId) {
        System.out.println(rechargeRecord);
        //更新
        MemberBindRecordEntity one = memberBindRecordService.getOne(new LambdaQueryWrapper<MemberBindRecordEntity>()
                .eq(rechargeRecord != null, MemberBindRecordEntity::getId, rechargeRecord.getMemberBindId()));
        one.setLastModifyTime(LocalDateTime.now()).setVersion(one.getVersion() + 1).setValidCount(one.getValidCount() + rechargeRecord.getAddCount())
                .setValidDay(one.getValidDay() + rechargeRecord.getAddDay());
        memberBindRecordService.updateById(one);

        //操作记录
        MemberLogEntity m1 = new MemberLogEntity().setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now())
                .setOperator(rechargeRecord.getOperator()).setInvolveMoney(rechargeRecord.getReceivedMoney()).setCardCountChange(rechargeRecord.getAddCount())
                .setCardDayChange(rechargeRecord.getAddDay()).setType("充值").setMemberBindId(rechargeRecord.getMemberBindId())
                .setNote(rechargeRecord.getNote());
        memberLogService.save(m1);

        //充值记录
        rechargeRecord.setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now()).setLogId(m1.getId());
        rechargeRecordService.save(rechargeRecord);
        return R.ok("充值成功");
    }

    @RequestMapping("/activeOpt.do")
    @ResponseBody
    public R activeOpt(Long memberId, Long bindId, Integer status, HttpSession session) {
        //更新
        MemberBindRecordEntity one = memberBindRecordService.getOne(new LambdaQueryWrapper<MemberBindRecordEntity>()
                .eq(bindId != null, MemberBindRecordEntity::getId, bindId));
        one.setActiveStatus(status).setVersion(one.getVersion() + 1);
        memberBindRecordService.updateById(one);

        //记录
        MemberLogEntity m;
        if (status == 0) {
            m = new MemberLogEntity().setType("停用会员卡").setInvolveMoney(new BigDecimal("0"))
                    .setOperator(((EmployeeEntity) session.getAttribute("LOGIN_USER")).getName()).setMemberBindId(bindId).setCreateTime(LocalDateTime.now())
                    .setLastModifyTime(LocalDateTime.now()).setNote("停用卡").setCardActiveStatus(status);
        } else {
            m = new MemberLogEntity().setType("启用会员卡").setInvolveMoney(new BigDecimal("0"))
                    .setOperator(((EmployeeEntity) session.getAttribute("LOGIN_USER")).getName()).setMemberBindId(bindId).setCreateTime(LocalDateTime.now())
                    .setLastModifyTime(LocalDateTime.now()).setNote("启用卡").setCardActiveStatus(status);
        }
        memberLogService.save(m);
        return R.ok("更新成功").setData(status);
    }
}
