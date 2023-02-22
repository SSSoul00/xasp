package com.kclm.xsap.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ConsumeDTO {
    private Integer costCount;
    private BigDecimal costMoney;
}
