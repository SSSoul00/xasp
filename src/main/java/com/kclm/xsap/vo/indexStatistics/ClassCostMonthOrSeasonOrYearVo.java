package com.kclm.xsap.vo.indexStatistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ClassCostMonthOrSeasonOrYearVo {
    //图表名
    private String title;
    //x轴名字
    private String xname;
    //教师名字
    private List<String> tname;
    //x轴数据
    private List<String> time;
    //y轴数据
    private List<List<Integer>> data;

    private List<List<BigDecimal>> data2;
}
