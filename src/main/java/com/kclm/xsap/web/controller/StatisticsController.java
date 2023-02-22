package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.dto.ConsumeDTO;
import com.kclm.xsap.dto.TeacherInfoDto;
import com.kclm.xsap.entity.ConsumeRecordEntity;
import com.kclm.xsap.entity.MemberBindRecordEntity;
import com.kclm.xsap.service.*;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.MemberCardStatisticsVo;
import com.kclm.xsap.vo.indexStatistics.ClassCostMonthOrSeasonOrYearVo;
import com.kclm.xsap.vo.indexStatistics.IndexAddAndStreamInfoVo;
import com.kclm.xsap.vo.statistics.CardInfoVo;
import com.kclm.xsap.vo.statistics.StatisticsOfCardCostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private ConsumeRecordService consumeRecordService;
    @Autowired
    private ScheduleRecordService scheduleRecordService;
    @Autowired
    private MemberBindRecordService memberBindRecordService;
    @Autowired
    private MemberService memberService;

    @RequestMapping("/statistics/cardInfo.do")
    @ResponseBody
    public R cardInfo() {
        List<MemberBindRecordEntity> bindCards = memberBindRecordService.list(new LambdaQueryWrapper<MemberBindRecordEntity>().eq(MemberBindRecordEntity::getActiveStatus, 1));
        Integer totalCourseTimeAll = 0;
        Integer usedCourseTimeAll = 0;
        Integer remainCourseTimeAll = 0;
        Integer totalMoneyAll = 0;
        Integer usedMoneyAll = 0;
        Integer remainMoneyAll = 0;
//        List<MemberCardStatisticsVo> memberCardStatisticsVos = bindCards.stream().map(item -> {
//
//        }).collect(Collectors.toList());
        List<MemberCardStatisticsVo> memberCardStatisticsVos = new ArrayList<>();
        for (MemberBindRecordEntity bindCard : bindCards) {
            MemberCardStatisticsVo memberCardStatisticsVo = new MemberCardStatisticsVo();
            //绑卡用户名   !!!改为自定义sql语句 逻辑删除的也要查出来。负责删除用户之后就会报错
            String memberName = memberService.selectById(bindCard.getMemberId()).getName();
            //绑卡消费记录
            List<ConsumeRecordEntity> consumeRecordEntities = consumeRecordService.list(new LambdaQueryWrapper<ConsumeRecordEntity>().eq(ConsumeRecordEntity::getMemberBindId, bindCard.getId())
                    .like(ConsumeRecordEntity::getOperateType, "扣费"));
            Integer usedClassTimes = 0;
            BigDecimal amountUsed = new BigDecimal("0");
            for (ConsumeRecordEntity consumeRecord : consumeRecordEntities) {
                usedClassTimes += consumeRecord.getCardCountChange();
                amountUsed = amountUsed.add(consumeRecord.getMoneyCost());
            }
            Integer totalClassTimes = bindCard.getValidCount() + usedClassTimes;
            Integer remainingClassTimes = bindCard.getValidCount();
            BigDecimal lumpSumBigD = bindCard.getReceivedMoney().add(amountUsed);
            BigDecimal balanceBigD = bindCard.getReceivedMoney();
            memberCardStatisticsVo.setBindCardId(bindCard.getId()).setMemberId(bindCard.getMemberId()).setMemberName(memberName).setUsedClassTimes(usedClassTimes)
                    .setAmountUsedBigD(amountUsed).setAmountUsed(amountUsed.toString()).setTotalClassTimes(totalClassTimes).setRemainingClassTimes(remainingClassTimes)
                    .setLumpSum(lumpSumBigD.toString()).setLumpSumBigD(lumpSumBigD).setBalanceBigD(balanceBigD).setBalance(balanceBigD.toString());
            memberCardStatisticsVos.add(memberCardStatisticsVo);
            totalCourseTimeAll += totalClassTimes;
            usedCourseTimeAll += usedClassTimes;
            remainCourseTimeAll += remainingClassTimes;
            totalMoneyAll += lumpSumBigD.intValue();
            usedMoneyAll += amountUsed.intValue();
            remainMoneyAll += balanceBigD.intValue();
        }
        CardInfoVo cardInfoVo = new CardInfoVo().setMemberCardStatisticsVos(memberCardStatisticsVos).setRemainCourseTimeAll(remainCourseTimeAll)
                .setRemainMoneyAll(remainMoneyAll).setUsedMoneyAll(usedMoneyAll).setUsedCourseTimeAll(usedCourseTimeAll).setTotalCourseTimeAll(totalCourseTimeAll)
                .setTotalMoneyAll(totalMoneyAll);
        return R.ok().setData(cardInfoVo);
    }

    @RequestMapping("/user/statistics/x_card_list_stat.html")
    public String toCardListStat() {
        return "statistics/x_card_list_stat";
    }


    @RequestMapping("/statistics/addAndStreamCountMonthOrSeasonOrYear")
    @ResponseBody
    public R addAndStreamCountMonthOrSeasonOrYear(Integer unit, Integer yearOfSelect, Integer beginYear, Integer endYear) {
        if (beginYear > endYear) {
            return R.error("年限错误");
        }
        IndexAddAndStreamInfoVo infoVo = new IndexAddAndStreamInfoVo();
        List<String> time = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<Integer> data2 = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        int nowYear = localDate.getYear();
        int nowMonth = localDate.getMonthValue();
        switch (unit) {
            case 1:
                if (yearOfSelect == nowYear){
                    for (int i = 0; i < nowMonth; i++) {
                        time.add((i + 1) + "月");
                        Integer addCountMonth = memberService.addCountMonth(i + 1, yearOfSelect);
                        Integer streamCountMonth = memberService.streamCountMonth(i + 1, yearOfSelect);
                        data.add(addCountMonth);
                        data2.add(streamCountMonth);
                    }
                    infoVo.setData(data).setData2(data2).setTime(time).setTitle("月新增与流失客户数量统计").setXname("月");
                    return R.ok().setData(infoVo);
                }
                for (int i = 0; i < 12; i++) {
                    time.add((i + 1) + "月");
                    Integer addCountMonth = memberService.addCountMonth(i + 1, yearOfSelect);
                    Integer streamCountMonth = memberService.streamCountMonth(i + 1, yearOfSelect);
                    data.add(addCountMonth);
                    data2.add(streamCountMonth);
                }
                infoVo.setData(data).setData2(data2).setTime(time).setTitle("月新增与流失客户数量统计").setXname("月");
                return R.ok().setData(infoVo);
            case 2:
                if (yearOfSelect == nowYear){
                    for (int i = 0; i < (nowMonth+2)/3; i++) {
                        time.add("第" + (i+1) +"季度");
                        Integer addCountQuarter = memberService.addCountQuarter(i + 1, yearOfSelect);
                        Integer streamCountQuarter = memberService.streamCountQuarter(i + 1, yearOfSelect);
                        data.add(addCountQuarter);
                        data2.add(streamCountQuarter);
                    }
                    infoVo.setData(data).setData2(data2).setTime(time).setTitle("月新增与流失客户数量统计").setXname("月");
                    return R.ok().setData(infoVo);
                }
                for (int i = 0; i < 4; i++) {
                    time.add("第" + (i+1) +"季度");
                    Integer addCountQuarter = memberService.addCountQuarter(i + 1, yearOfSelect);
                    Integer streamCountQuarter = memberService.streamCountQuarter(i + 1, yearOfSelect);
                    data.add(addCountQuarter);
                    data2.add(streamCountQuarter);
                }
                infoVo.setData(data).setData2(data2).setTime(time).setTitle("季度新增与流失客户数量统计").setXname("季度");
                return R.ok().setData(infoVo);
            case 3:
                for (int i = beginYear; i <= endYear; i++) {
                    time.add(i +"年");
                    Integer addCountYear = memberService.addCountYear(i);
                    Integer streamCountYear = memberService.streamCountYear(i);
                    data.add(addCountYear);
                    data2.add(streamCountYear);
                }
                infoVo.setData(data).setData2(data2).setTime(time).setTitle("季度新增与流失客户数量统计").setXname("季度");
                return R.ok().setData(infoVo);
        }
        return null;
    }

    @RequestMapping("user/statistics/x_member_num_static.html")
    public String toMemberNumStatic() {
        return "/statistics/x_member_num_static";
    }

    @RequestMapping("/statistics/classCountMonthOrSeasonOrYear")
    @ResponseBody
    public R classCountMonthOrSeasonOrYear(Integer unit, Integer yearOfSelect, Integer beginYear, Integer endYear) {
        if (beginYear > endYear) {
            return R.error("年限错误");
        }
        IndexAddAndStreamInfoVo infoVo = new IndexAddAndStreamInfoVo();
        LocalDate localDate = LocalDate.now();
        int nowYear = localDate.getYear();
        int nowMonth = localDate.getMonthValue();

        List<Integer> data = new ArrayList<>();
        List<String> time = new ArrayList<>();
        switch (unit) {
            case 1:
                //按月查询
                if (yearOfSelect == nowYear) {
                    for (int i = 0; i < nowMonth; i++) {
                        time.add((i + 1) + "月");
                        Integer integer = scheduleRecordService.classCountMonth(i + 1, yearOfSelect);
                        data.add(integer);
                    }
                    infoVo.setXname("月").setTime(time).setData(data).setTitle("月课时统计");
                    return R.ok().setData(infoVo);
                }
                for (int i = 0; i < 12; i++) {
                    time.add((i + 1) + "月");
                    Integer integer = scheduleRecordService.classCountMonth(i + 1, yearOfSelect);
                    data.add(integer);
                }
                infoVo.setXname("月").setTime(time).setData(data).setTitle("月课时统计");
                return R.ok().setData(infoVo);
            case 2:
                //按季度查询
                if (yearOfSelect == nowYear) {
                    for (int i = 0; i < (nowMonth + 2) / 3; i++) {
                        time.add("第" + (i + 1) + "季度");
                        Integer integer = scheduleRecordService.classCountQuarter(i + 1, yearOfSelect);
                        data.add(integer);
                    }
                    infoVo.setXname("季度").setTime(time).setData(data).setTitle("季度课时统计");
                    return R.ok().setData(infoVo);
                }
                for (int i = 0; i < 4; i++) {
                    time.add("第" + (i + 1) + "季度");
                    Integer integer = scheduleRecordService.classCountQuarter(i + 1, yearOfSelect);
                    data.add(integer);
                }
                infoVo.setXname("季度").setTime(time).setData(data).setTitle("季度课时统计");
                return R.ok().setData(infoVo);
            case 3:
                //按年查询
                for (int i = beginYear; i <= endYear; i++) {
                    time.add(i + "年");
                    Integer integer = scheduleRecordService.classCountYear(i);
                    data.add(integer);
                }
                infoVo.setXname("季度").setTime(time).setData(data).setTitle("季度课时统计");
                return R.ok().setData(infoVo);
        }
        return null;
    }

    @RequestMapping("/user/statistics/x_class_hour_stat.html")
    public String toClassHourStat() {
        return "statistics/x_class_hour_stat";
    }

    @RequestMapping("/user/statistics/x_class_cost_stat.html")
    public String toClassCostStat() {
        return "statistics/x_class_cost_stat";
    }

    @RequestMapping("statistics/classCostMonthOrSeasonOrYear")
    @ResponseBody
    public R classCostMonthOrSeasonOrYear(Integer unit, Integer yearOfSelect, Integer beginYear, Integer endYear) {
        if (beginYear > endYear) {
            return R.error("年限错误");
        }
        List<TeacherInfoDto> teacherInfoDtos;
        if (unit == 3) {
            teacherInfoDtos = consumeRecordService.selectTeacherBetweenYear(beginYear, endYear);
        } else {
            teacherInfoDtos = consumeRecordService.selectTeacherByYear(yearOfSelect);
        }
        ClassCostMonthOrSeasonOrYearVo costMonthOrSeasonOrYearVo = new ClassCostMonthOrSeasonOrYearVo();
        LocalDate localDate = LocalDate.now();
        int nowYear = localDate.getYear();
        int nowMonth = localDate.getMonthValue();

        List<String> tname = new ArrayList<>();
        List<List<Integer>> data = new ArrayList<>();
        List<List<BigDecimal>> data2 = new ArrayList<>();
        List<String> time = new ArrayList<>();
        switch (unit) {
            case 1:
                //按月查询
                if (yearOfSelect == nowYear) {
                    for (int i = 0; i < nowMonth; i++) {
                        time.add((i + 1) + "月");
                    }
                    for (TeacherInfoDto item : teacherInfoDtos) {
                        tname.add(item.getName());
                        List<Integer> costCount = new ArrayList<>();
                        List<BigDecimal> costMoney = new ArrayList<>();
                        for (int i = 0; i < nowMonth; i++) {
                            ConsumeDTO consumeDTO = consumeRecordService.classCostMonth(item.getId(), i + 1, yearOfSelect);
                            if (consumeDTO != null) {
                                costCount.add(consumeDTO.getCostCount());
                                costMoney.add(consumeDTO.getCostMoney());
                            } else {
                                costCount.add(null);
                                costMoney.add(null);
                            }
                        }
                        data.add(costCount);
                        data2.add(costMoney);
                    }
                    costMonthOrSeasonOrYearVo.setTname(tname).setData(data).setData2(data2).setTitle("按月查询").setXname("月").setTime(time);
                    return R.ok().setData(costMonthOrSeasonOrYearVo);
                }
                for (int i = 0; i < 12; i++) {
                    time.add((i + 1) + "月");
                }
                for (TeacherInfoDto item : teacherInfoDtos) {
                    tname.add(item.getName());
                    List<Integer> costCount = new ArrayList<>();
                    List<BigDecimal> costMoney = new ArrayList<>();
                    for (int i = 0; i < 12; i++) {
                        ConsumeDTO consumeDTO = consumeRecordService.classCostMonth(item.getId(), i + 1, yearOfSelect);
                        if (consumeDTO != null) {
                            costCount.add(consumeDTO.getCostCount());
                            costMoney.add(consumeDTO.getCostMoney());
                        } else {
                            costCount.add(null);
                            costMoney.add(null);
                        }
                    }
                    data.add(costCount);
                    data2.add(costMoney);
                }
                costMonthOrSeasonOrYearVo.setTname(tname).setData(data).setData2(data2).setTitle("按月查询").setXname("月").setTime(time);
                return R.ok().setData(costMonthOrSeasonOrYearVo);
            case 2:
                //按季度查询
                if (yearOfSelect == nowYear) {
                    for (int i = 0; i < (nowMonth + 2) / 3; i++) {
                        time.add("第" + (i + 1) + "季度");
                    }
                    for (TeacherInfoDto item : teacherInfoDtos) {
                        tname.add(item.getName());
                        List<Integer> costCount = new ArrayList<>();
                        List<BigDecimal> costMoney = new ArrayList<>();
                        for (int i = 0; i < (nowMonth + 2) / 3; i++) {
                            ConsumeDTO consumeDTO = consumeRecordService.classCostQuarter(item.getId(), (i + 1), yearOfSelect);
                            if (consumeDTO != null) {
                                costCount.add(consumeDTO.getCostCount());
                                costMoney.add(consumeDTO.getCostMoney());
                            } else {
                                costCount.add(null);
                                costMoney.add(null);
                            }
                        }
                        data.add(costCount);
                        data2.add(costMoney);
                    }
                    costMonthOrSeasonOrYearVo.setTname(tname).setData(data).setData2(data2).setTitle("按季度查询").setXname("季度").setTime(time);
                    return R.ok().setData(costMonthOrSeasonOrYearVo);
                }
                for (int i = 0; i < 4; i++) {
                    time.add("第" + (i + 1) + "季度");
                }
                for (TeacherInfoDto item : teacherInfoDtos) {
                    tname.add(item.getName());
                    List<Integer> costCount = new ArrayList<>();
                    List<BigDecimal> costMoney = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        ConsumeDTO consumeDTO = consumeRecordService.classCostQuarter(item.getId(), (i + 1), yearOfSelect);
                        if (consumeDTO != null) {
                            costCount.add(consumeDTO.getCostCount());
                            costMoney.add(consumeDTO.getCostMoney());
                        } else {
                            costCount.add(null);
                            costMoney.add(null);
                        }
                    }
                    data.add(costCount);
                    data2.add(costMoney);
                }
                costMonthOrSeasonOrYearVo.setTname(tname).setData(data).setData2(data2).setTitle("按季度查询").setXname("季度").setTime(time);
                return R.ok().setData(costMonthOrSeasonOrYearVo);
            case 3:
                //按年查询
                for (int i = beginYear; i <= endYear; i++) {
                    time.add(i + "年");
                }
                for (TeacherInfoDto item : teacherInfoDtos) {
                    tname.add(item.getName());
                    List<Integer> costCount = new ArrayList<>();
                    List<BigDecimal> costMoney = new ArrayList<>();
                    for (int i = beginYear; i <= endYear; i++) {
                        ConsumeDTO consumeDTO = consumeRecordService.classCostYear(item.getId(), i);
                        if (consumeDTO != null) {
                            costCount.add(consumeDTO.getCostCount());
                            costMoney.add(consumeDTO.getCostMoney());
                        } else {
                            costCount.add(null);
                            costMoney.add(null);
                        }
                    }
                    data.add(costCount);
                    data2.add(costMoney);
                }
                costMonthOrSeasonOrYearVo.setTname(tname).setData(data).setData2(data2).setTitle("按年查询").setXname("年").setTime(time);
                return R.ok().setData(costMonthOrSeasonOrYearVo);
        }
        return null;
    }

    @RequestMapping("/user//statistics/x_card_cost_stat.html")
    public String toCardCostStat() {
        return "statistics/x_card_cost_stat";
    }

    @RequestMapping("/statistics/yearList")
    @ResponseBody
    public R yearList() {
        return R.ok().setData(rechargeRecordService.selectAllYear());
    }

    @RequestMapping("/statistics/cardCostMonthOrSeasonOrYear")
    @ResponseBody
    public R cardCostMonthOrSeasonOrYear(Integer unit, Integer yearOfSelect, Integer beginYear, Integer endYear) {
        if (beginYear > endYear) {
            return R.error("年限错误");
        }
        LocalDate localDate = LocalDate.now();
        int nowYear = localDate.getYear();
        int nowMonth = localDate.getMonthValue();

        IndexAddAndStreamInfoVo infoVo = new IndexAddAndStreamInfoVo();
        List<String> time = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        switch (unit) {
            case 1:
                //按月查询
                if (yearOfSelect == nowYear) {
                    for (int i = 0; i < nowMonth; i++) {
                        Integer costMoney = rechargeRecordService.selectMoneyByMonth(i + 1, yearOfSelect);
                        data.add(costMoney);
                        time.add((i + 1) + "月份");
                    }
                    infoVo.setTitle("月收费模式").setData(data).setTime(time).setXname("月");
                    return R.ok().setData(infoVo);
                }
                for (int i = 0; i < 12; i++) {
                    Integer costMoney = rechargeRecordService.selectMoneyByMonth(i + 1, yearOfSelect);
                    data.add(costMoney);
                    time.add((i + 1) + "月份");
                }
                infoVo.setTitle("月收费模式").setData(data).setTime(time).setXname("月");
                return R.ok().setData(infoVo);
            case 2:
                //按季度查询
                if (yearOfSelect == nowYear) {
                    for (int i = 0; i < (nowMonth + 2) / 3; i++) {
                        Integer costMoney = rechargeRecordService.selectMoneyByQuarter(i + 1, yearOfSelect);
                        data.add(costMoney);
                        time.add("第" + (i + 1) + "季度");
                    }
                    infoVo.setTitle("季度收费模式").setData(data).setTime(time).setXname("季度");
                    return R.ok().setData(infoVo);
                }
                for (int i = 0; i < 4; i++) {
                    Integer costMoney = rechargeRecordService.selectMoneyByQuarter(i + 1, yearOfSelect);
                    data.add(costMoney);
                    time.add("第" + (i + 1) + "季度");
                }
                infoVo.setTitle("季度收费模式").setData(data).setTime(time).setXname("季度");
                return R.ok().setData(infoVo);
            case 3:
                //按年查询
                for (int i = beginYear; i <= endYear; i++) {
                    Integer costMoney = rechargeRecordService.selectMoneyByYear(i);
                    data.add(costMoney);
                    time.add(i + "年");
                }
                infoVo.setTitle("年收费模式").setData(data).setTime(time).setXname("年");
                return R.ok().setData(infoVo);
        }
        return null;
    }
}
