package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kclm.xsap.entity.EmployeeEntity;
import com.kclm.xsap.entity.MemberEntity;
import com.kclm.xsap.service.*;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.IndexHomeDateVo;
import com.kclm.xsap.vo.indexStatistics.IndexAddAndStreamInfoVo;
import com.kclm.xsap.vo.indexStatistics.IndexPieChartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequestMapping("/index")
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private ReservationRecordService reservationRecordService;
    @RequestMapping("homePageInfo.do")
    @ResponseBody
    public R homePageInfo(){
        IndexHomeDateVo indexHomeDateVo = new IndexHomeDateVo();
        LocalDate endDate  = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        Integer totalMembers = memberService.totalMembers();
        List<Integer> integerList = reservationRecordService.activeMembers(startDate,endDate);
        Integer totalReservations = reservationRecordService.totalReservations(startDate, endDate);
        indexHomeDateVo.setTotalMembers(totalMembers).setActiveMembers(integerList.size()).setTotalReservations(totalReservations);
        return R.ok().setData(indexHomeDateVo);
    }

    @RequestMapping("homePageInfo/statisticsOfMemberCard.do")
    @ResponseBody
    public R statisticsOfMemberCard(){
        List<IndexPieChartVo> indexPieChartVos = memberCardService.selectMemberCardCount();
        return R.ok().setData(indexPieChartVos);
    }

    @RequestMapping("/homePageInfo/statisticsOfDailyCharge.do")
    @ResponseBody
    public R statisticsOfDailyCharge(){
        IndexAddAndStreamInfoVo infoVo = new IndexAddAndStreamInfoVo();
        LocalDate localDate = LocalDate.now();
        LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
        List<String> time = new ArrayList<>();
        List<Integer> date = new ArrayList<>();
        while (firstDay.isBefore(localDate)||firstDay.isEqual(localDate)){
            time.add(String.valueOf(firstDay.getDayOfMonth()));
            Integer moneyByDay = rechargeRecordService.selectMoneyByDay(firstDay);
            date.add(moneyByDay);
            firstDay = firstDay.plusDays(1);
        }
        infoVo.setTime(time).setData(date).setXname("日").setTitle("当月每日收费统计");
        return R.ok().setData(infoVo);
    }

    @RequestMapping("/homePageInfo/statisticsOfNewAndLostPeople.do")
    @ResponseBody
    public R statisticsOfNewAndLostPeople(){
//        Calendar calendar = Calendar.getInstance();
//        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        IndexAddAndStreamInfoVo infoVo = new IndexAddAndStreamInfoVo();
        LocalDate localDate = LocalDate.now();
        LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
        List<String> time = new ArrayList<>();
        List<Integer> date = new ArrayList<>();
        List<Integer> date2 = new ArrayList<>();
        while (firstDay.isBefore(localDate)||firstDay.isEqual(localDate)){
            time.add(String.valueOf(firstDay.getDayOfMonth()));
            Integer addCountDay = memberService.addCountDay(firstDay);
            Integer streamCountDay = memberService.streamCountDay(firstDay);
            date.add(addCountDay);
            date2.add(streamCountDay);
            firstDay = firstDay.plusDays(1);
        }
        infoVo.setXname("/日").setData(date).setData2(date2).setTitle("当月新增与流失人数统计").setTime(time);
        return R.ok().setData(infoVo);
    }

    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "x_login";
    }

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "x_register";
    }

    @RequestMapping("/register")
    public String register(String userName,String password, String pwd2, Model model){
        log.info("注册参数：用户名-》{}  密码-》{}  二次密码-》{}",userName,password,pwd2);
        EmployeeEntity one = employeeService.getOne(new LambdaQueryWrapper<EmployeeEntity>().eq(userName != null, EmployeeEntity::getName, userName));
        if (one!=null){
            model.addAttribute("CHECK_TYPE_ERROR", 0);
            return "x_register";
        }
        if (password.equals(pwd2)) {
            EmployeeEntity employee = new EmployeeEntity().setName(userName).setRolePassword(password).setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now());
            employeeService.save(employee);
            return "x_login";
        } else {
            model.addAttribute("CHECK_TYPE_ERROR", 1);
            return "x_register";
        }
    }

    @RequestMapping("/x_index_home.do")
    public String toIndexHome(){
        return "x_index_home";
    }
}
