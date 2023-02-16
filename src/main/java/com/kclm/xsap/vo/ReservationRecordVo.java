package com.kclm.xsap.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ReservationRecordVo {
    /**
     * 预约ID
     */
    private Long reserveId;
    /**
     * 预约卡名
     */
    private String cardName;
    /**
     * 预约课程
     */
    private String courseName;
    /**
     * 操作时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime operateTime;
    /**
     * 操作者
     */
    private String operator;
    /**
     * 预约备注
     */
    private String reserveNote;
    /**
     * 预约人数
     */
    private Integer reserveNumbers;
    /**
     * 预约状态
     */
    private Integer reserveStatus;
    /**
     * 预约时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime reserveTime;
    /**
     * 课程消耗次数
     */
    private Integer timesCost;
}
