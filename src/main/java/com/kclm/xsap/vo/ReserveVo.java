package com.kclm.xsap.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class ReserveVo {
    private String cardName;
    private Integer classCheck;
    private LocalTime classTime;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastModifyTime;
    private String memberName;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime operateTime;
    private String operator;
    private String phone;
    private Long reserveId;
    private String reserveNote;
    private Integer reserveNumbers;
    private Integer reserveStatus;
    private LocalDate startDate;
    private Integer timesCost;
}
