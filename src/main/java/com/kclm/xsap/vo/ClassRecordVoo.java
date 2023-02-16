package com.kclm.xsap.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class ClassRecordVoo {
    //卡名
    private String cardName;
    //上课与否
    private Integer checkStatus;
    //参加人数 预约表
    private Integer classNumbers;
    //上课记录id
    private Long classRecordId;
    //上课时间 排课表
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime classTime;
    //评论
    private String comment;
    //课程名 课程表
    private String courseName;
    //排课日期  排课表
    private LocalDate scheduleStartDate;
    //排课时间  排课表
    private LocalTime scheduleStartTime;
    //教师名  教师表
    private String teacherName;
    //消耗次数  课程表
    private Integer timesCost;
}
