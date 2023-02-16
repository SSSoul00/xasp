package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.*;
import com.kclm.xsap.service.MemberBindRecordService;
import com.kclm.xsap.service.MemberCardService;
import com.kclm.xsap.service.MemberLogService;
import com.kclm.xsap.service.RechargeRecordService;
import com.kclm.xsap.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/cardBind")
public class CardBindController {
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private MemberLogService memberLogService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberBindRecordService memberBindRecordService;

    @RequestMapping("/x_member_card_bind.do")
    public String toMemberCardBind() {
        return "/member/x_member_card_bind";
    }

    @RequestMapping("/memberBind.do")
    @ResponseBody
    @Transactional
    public R memberBind(@Valid MemberBindRecordEntity mbre, BindingResult result, HttpSession session) {
        //数据验证
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : fieldErrors) {
                System.out.println(fe.getField() + "=>" + fe.getDefaultMessage());
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            return R.error(400, "非法参数").put("errorMap", errorMap);
        }

        //绑卡
        MemberCardEntity one = memberCardService.getOne(new LambdaQueryWrapper<MemberCardEntity>()
                .eq(mbre != null, MemberCardEntity::getId, mbre.getCardId()));
        mbre.setValidCount(mbre.getValidCount() + one.getTotalCount()).setValidDay(mbre.getValidDay() + one.getTotalDay())
                .setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now());
        memberBindRecordService.save(mbre);

        //操作记录
        MemberLogEntity ml = new MemberLogEntity().setMemberBindId(mbre.getId()).setType("绑卡")
                .setInvolveMoney(one.getPrice()).setNote("绑卡")
                .setOperator(((EmployeeEntity) session.getAttribute("LOGIN_USER")).getName()).setCreateTime(LocalDateTime.now());
        MemberLogEntity m2 = new MemberLogEntity().setMemberBindId(mbre.getId()).setType("绑卡充值")
                .setInvolveMoney(mbre.getReceivedMoney().subtract(one.getPrice())).setNote("充值")
                .setOperator(((EmployeeEntity) session.getAttribute("LOGIN_USER")).getName()).setCreateTime(LocalDateTime.now());
        List<MemberLogEntity> memberLogEntityList = Arrays.asList(ml, m2);
        memberLogService.save(ml);
        memberLogService.save(m2);

        //充值记录
        RechargeRecordEntity r1 = new RechargeRecordEntity().setAddCount(one.getTotalCount()).setAddDay(one.getTotalDay())
                .setOperator(((EmployeeEntity) session.getAttribute("LOGIN_USER")).getName()).setCreateTime(LocalDateTime.now())
                .setReceivedMoney(one.getPrice()).setPayMode(mbre.getPayMode()).setNote("绑卡").setMemberBindId(mbre.getId()).setLogId(ml.getId());
        RechargeRecordEntity r2 = new RechargeRecordEntity().setAddCount(mbre.getValidCount() - one.getTotalCount())
                .setAddDay(mbre.getValidDay() - one.getTotalDay())
                .setOperator(((EmployeeEntity) session.getAttribute("LOGIN_USER")).getName()).setCreateTime(LocalDateTime.now())
                .setReceivedMoney(mbre.getReceivedMoney().subtract(one.getPrice())).setPayMode(mbre.getPayMode()).setNote("绑卡充值")
                .setMemberBindId(mbre.getId()).setLogId(m2.getId());
        List<RechargeRecordEntity> rechargeRecordEntities = Arrays.asList(r1, r2);
        rechargeRecordService.saveBatch(rechargeRecordEntities);
        return R.ok("绑卡成功");
    }


}
