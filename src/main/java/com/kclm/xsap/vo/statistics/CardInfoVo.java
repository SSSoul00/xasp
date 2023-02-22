package com.kclm.xsap.vo.statistics;

import com.kclm.xsap.vo.MemberCardStatisticsVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CardInfoVo {
    //会员卡统计
    private List<MemberCardStatisticsVo> memberCardStatisticsVos;
    //总课时
    private Integer totalCourseTimeAll;
    //已用课时
    private Integer usedCourseTimeAll;
    //剩余课时
    private Integer remainCourseTimeAll;
    //总金额
    private Integer totalMoneyAll;
    //已用金额
    private Integer usedMoneyAll;
    //剩余金额
    private Integer remainMoneyAll;
}
